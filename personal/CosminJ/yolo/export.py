import torch
from ultralytics import YOLO

# Încarcă modelul YOLO
model = YOLO("runs/detect/train2/weights/best.pt")

# Ia modelul PyTorch brut
torch_model = model.model

# Creează un input fals (dimensiuni: batch=1, 3 canale, 640x640)
dummy_input = torch.randn(1, 3, 640, 640)

# Exportă în ONNX fără onnxsim
torch.onnx.export(
    torch_model,
    dummy_input,
    "best.onnx",
    opset_version=12,
    input_names=["images"],
    output_names=["output"],
    dynamic_axes={"images": {0: "batch_size"}, "output": {0: "batch_size"}}
)

print("✅ Model exportat în best.onnx")
