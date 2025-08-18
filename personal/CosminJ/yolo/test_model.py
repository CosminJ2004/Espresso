from ultralytics import YOLO
import cv2
import os

# Încarcă modelul antrenat
model_path = r"runs/detect/train2/weights/best.pt"  # schimbă dacă e altă locație
model = YOLO(model_path)

# Folder cu imaginile pe care vrei să testezi
test_images_dir = r"dataset/images/val"  # schimbă dacă ai alt folder

# Creează folderul de rezultate
output_dir = "results"
os.makedirs(output_dir, exist_ok=True)

# Rulează detecția pe fiecare imagine
for img_name in os.listdir(test_images_dir):
    if img_name.lower().endswith((".jpg", ".png", ".jpeg")):
        img_path = os.path.join(test_images_dir, img_name)

        # Rulează detecția
        results = model(img_path)

        # Salvează imaginea cu predicțiile desenate
        for r in results:
            annotated = r.plot()  # imagine cu bounding box-urile desenate
            out_path = os.path.join(output_dir, img_name)
            cv2.imwrite(out_path, annotated)

print(f"Rezultatele salvate în folderul: {output_dir}")
