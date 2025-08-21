import onnxruntime as ort
import numpy as np
import cv2
import os
import json

# Încarcă modelul ArcFace
session = ort.InferenceSession("arcface.onnx", providers=["CPUExecutionProvider"])

def preprocess_face(image_path):
    img = cv2.imread(image_path)
    img = cv2.resize(img, (112, 112))           # Dimensiunea standard ArcFace
    img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    img = img.astype(np.float32) / 127.5 - 1.0  # Normalizare [-1,1]
    img = np.transpose(img, (2, 0, 1))          # [C,H,W]
    img = np.expand_dims(img, axis=0)           # [1,3,112,112]
    return img

def get_face_embedding(image_path):
    input_tensor = preprocess_face(image_path)
    inputs = {session.get_inputs()[0].name: input_tensor}
    output = session.run(None, inputs)[0][0]    # vector de embedding
    norm = np.linalg.norm(output)
    return (output / norm).tolist()

# Generează embedding-uri pentru toate imaginile
known_faces = {}
for file in os.listdir("image_folder"):
    if file.lower().endswith((".jpg", ".png", ".jpeg")):
        name = os.path.splitext(file)[0]
        embedding = get_face_embedding(os.path.join("image_folder", file))
        known_faces[name] = embedding

# Salvează în JSON
with open("faces_db.json", "w") as f:
    json.dump(known_faces, f)
