using Microsoft.ML.OnnxRuntime;
using Microsoft.ML.OnnxRuntime.Tensors;

using Processing.Models;
using SixLabors.ImageSharp;
using SixLabors.ImageSharp.PixelFormats;
using System;
using System.Collections.Generic;
using System.Linq;
using System.IO;
using System.Reflection;
using System.Reflection.Emit;
using System.Text;
using Processing.Convertors;
using SixLabors.ImageSharp.Processing;

namespace Processing.Utils
{
    public class YoloDetector
    {
        private InferenceSession _session;
        private readonly string[] _labels;
        private readonly int _inputWidth = 320;
        private readonly int _inputHeight = 320;
        private readonly float _confThreshold = 0.7f;
        private readonly float _nmsThreshold = 0.45f;


        public YoloDetector(InferenceSession session)
        {
            _session = session; // folosim sesiunea singleton
        }



        private static readonly string modelPath = Path.Combine(AppContext.BaseDirectory, "Utils", "best.onnx");

        private static readonly string[] labels = { "face" };

   



        public List<FaceDetected> ApplyAndExtract(RgbImage image)
        {
            byte[] img = MatRgbConvertor.RgbImageToByteArray(image);
            int origW = image.Width;
            int origH = image.Height;

            // 1) Creare ImageSharp
            Image<Rgb24> sharpImage = new Image<Rgb24>(origW, origH);
            for (int y = 0; y < origH; y++)
                for (int x = 0; x < origW; x++)
                {
                    var px = image.Pixels[y, x];
                    sharpImage[x, y] = new Rgb24(px.R, px.G, px.B);
                }

            // 2) Resize pentru model
            int modelWidth = 320, modelHeight = 320;
            sharpImage.Mutate(ctx => ctx.Resize(modelWidth, modelHeight));

            // sharpImage este deja redimensionat la modelWidth x modelHeight
            byte[] pixelBytes = new byte[modelWidth * modelHeight * 3];
            sharpImage.CopyPixelDataTo(pixelBytes); // pixelBytes = RGB intercalat

            // Convertim direct în float tensor [1,3,H,W]
            var inputTensor = new DenseTensor<float>(new[] { 1, 3, modelHeight, modelWidth });

            int pixelIndex = 0;
            for (int y = 0; y < modelHeight; y++)
            {
                for (int x = 0; x < modelWidth; x++)
                {
                    inputTensor[0, 0, y, x] = pixelBytes[pixelIndex++] / 255f; // R
                    inputTensor[0, 1, y, x] = pixelBytes[pixelIndex++] / 255f; // G
                    inputTensor[0, 2, y, x] = pixelBytes[pixelIndex++] / 255f; // B
                }
            }

            // 4) Run ONNX
            using var session = new InferenceSession(modelPath);
            var inputs = new List<NamedOnnxValue>
    {
        NamedOnnxValue.CreateFromTensor(session.InputMetadata.Keys.First(), inputTensor)
    };
            using var results = session.Run(inputs);
            var output = results.First().AsTensor<float>();
            var dims = output.Dimensions.ToArray();
            int anchors = dims[2];

            List<BoundingBox> boxes = new List<BoundingBox>();
            List<float> scores = new List<float>();

            for (int i = 0; i < anchors; i++)
            {
                float x = output[0, 0, i];
                float y = output[0, 1, i];
                float w = output[0, 2, i];
                float h = output[0, 3, i];
                float conf = output[0, 4, i];

                if (conf < _confThreshold) continue;

                int x1 = (int)((x - w / 2) * origW / modelWidth);
                int y1 = (int)((y - h / 2) * origH / modelHeight);
                int width = (int)(w * origW / modelWidth);
                int height = (int)(h * origH / modelHeight);

                boxes.Add(new BoundingBox(x1, y1, width, height));
                scores.Add(conf);
            }

            var indices = Nms(boxes, scores, _nmsThreshold);

            List<FaceDetected> crops = new List<FaceDetected>();

            foreach (var idx in indices)
            {
                var box = boxes[idx];

                // crop bounding box
                RgbImage crop = new RgbImage(box.Width, box.Height);
                for (int y = 0; y < box.Height; y++)
                    for (int x = 0; x < box.Width; x++)
                    {
                        int srcX = box.X + x;
                        int srcY = box.Y + y;
                        if (srcX >= 0 && srcX < origW && srcY >= 0 && srcY < origH)
                            crop.Pixels[y, x] = image.Pixels[srcY, srcX];
                    }

                crops.Add(new FaceDetected { FaceImage = crop, Box = box });
            }

            //Console.WriteLine($"Detected {boxes.Count} boxes, extracted {crops.Count} crops after NMS");

            return crops;
        }



        private List<int> Nms(List<BoundingBox> boxes, List<float> scores, float iouThreshold)
        {
            var indices = Enumerable.Range(0, boxes.Count)
                .OrderByDescending(i => scores[i]).ToList();
            var keep = new List<int>();

            while (indices.Count > 0)
            {
                int current = indices[0];
                keep.Add(current);
                indices.RemoveAt(0);

                indices = indices.Where(i => IoU(boxes[current], boxes[i]) < iouThreshold).ToList();
            }

            return keep;
        }

        // Functie IoU pentru BoundingBox
        private float IoU(BoundingBox a, BoundingBox b)
        {
            int x1 = Math.Max(a.X, b.X);
            int y1 = Math.Max(a.Y, b.Y);
            int x2 = Math.Min(a.X + a.Width, b.X + b.Width);
            int y2 = Math.Min(a.Y + a.Height, b.Y + b.Height);

            int intersectionWidth = Math.Max(0, x2 - x1);
            int intersectionHeight = Math.Max(0, y2 - y1);
            int intersectionArea = intersectionWidth * intersectionHeight;

            int unionArea = a.Width * a.Height + b.Width * b.Height - intersectionArea;

            return (float)intersectionArea / unionArea;
        }


    }
}
