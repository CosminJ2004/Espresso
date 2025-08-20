!pip install ultralytics onnx
!pip install onnxruntime
!pip install numpy
!pip install Pillow

from PIL import Image
import onnxruntime as ort
import numpy as np
import os
import json

image_folder = '/content/drive/MyDrive/image_folder/0_000000'
files = os.listdir(image_folder)
print("Files in folder:", files)

# Încarcă modelul ArcFace
session = ort.InferenceSession("/content/arcface.onnx", providers=["CPUExecutionProvider"])
print("Model loaded:", session)

def preprocess_face(image_path):
    img = Image.open(image_path).convert('RGB')
    img = img.resize((112, 112))
    img = np.array(img).astype(np.float32)
    img = (img / 127.5) - 1.0         # normalize [-1,1]
    img = np.expand_dims(img, axis=0) # [1,112,112,3]
    img = np.ascontiguousarray(img)
    return img

def get_face_embedding(image_path):
    input_tensor = preprocess_face(image_path)
    inputs = {session.get_inputs()[0].name: input_tensor.astype(np.float32)}
    output = session.run(None, inputs)[0][0]    # vector de embedding
    normalized = output / np.linalg.norm(output)
    return normalized.tolist()

# Generează embedding-uri pentru toate imaginile
known_faces = {}

for file in os.listdir(image_folder):
    if file.lower().endswith((".jpg", ".png", ".jpeg")):
        # presupunem că numele persoanei e partea din filename înainte de "_"
        person_name = "Cosmin"
        embedding = get_face_embedding(os.path.join(image_folder, file))

        if person_name not in known_faces:
            known_faces[person_name] = []
        known_faces[person_name].append(embedding)

print(f"\nAll embeddings generated. Total people: {len(known_faces)}")

# Salvează în JSON
output_path = '/content/drive/MyDrive/faces_db.json'
with open(output_path, "w") as f:
    json.dump(known_faces, f)
print(f"faces_db.json saved successfully at {output_path}!")
