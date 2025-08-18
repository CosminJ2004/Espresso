import os
import cv2

def is_int(s):
    try:
        int(s)
        return True
    except:
        return False

wider_file = "wider_face_split\wider_face_split\wider_face_val_bbx_gt.txt"  # fișierul de input WIDER annotations
images_base_path = "WIDER_val/WIDER_val/images"  # folderul rădăcină al imaginilor

output_labels_dir = "labels_val"  # unde salvezi txt-urile YOLO
os.makedirs(output_labels_dir, exist_ok=True)

with open(wider_file, "r") as f:
    lines = f.readlines()

i = 0
while i < len(lines):
    img_path = lines[i].strip()
    i += 1

    # Verifică dacă linia următoare e număr
    while i < len(lines) and not is_int(lines[i].strip()):
        print(f"Linie neasteptata pentru numar fete: {lines[i].strip()}, sarim peste.")
        i += 1
    if i >= len(lines):
        break

    num_faces = int(lines[i].strip())
    i += 1

    # Încarcă imaginea ca să iei dimensiuni
    full_img_path = os.path.join(images_base_path, img_path)
    img = cv2.imread(full_img_path)
    if img is None:
        print(f"Imaginea nu exista sau nu poate fi citita: {full_img_path}")
        # Sar peste toate fețele din această intrare
        i += num_faces
        continue

    h, w = img.shape[:2]

    if num_faces == 0:
        # nu scriem fișier txt pentru imaginile fără fețe
        continue

    yolo_lines = []
    for _ in range(num_faces):
        if i >= len(lines):
            break
        bbox_line = lines[i].strip()
        i += 1
        parts = bbox_line.split()
        if len(parts) < 4:
            print(f"Bounding box invalid: {bbox_line}")
            continue

        x, y, bw, bh = map(float, parts[:4])

        # YOLO format:
        # class_id center_x_rel center_y_rel width_rel height_rel
        cx = (x + bw / 2) / w
        cy = (y + bh / 2) / h
        bw_rel = bw / w
        bh_rel = bh / h

        # Clasa 0 pentru fata
        yolo_lines.append(f"0 {cx:.6f} {cy:.6f} {bw_rel:.6f} {bh_rel:.6f}")

    # Scriem fișierul txt cu același nume ca imaginea dar cu extensia .txt
    base_filename = os.path.splitext(os.path.basename(img_path))[0]
    out_path = os.path.join(output_labels_dir, base_filename + ".txt")
    with open(out_path, "w") as out_f:
        out_f.write("\n".join(yolo_lines))

    print(f"Processed {img_path} with {num_faces} faces")
