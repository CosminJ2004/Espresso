using Microsoft.ML.OnnxRuntime;
using Microsoft.ML.OnnxRuntime.Tensors;

using Processing.Convertors;
using Processing.Interfaces;
using Processing.Models;
using Processing.Utils;
using SixLabors.ImageSharp;
using SixLabors.ImageSharp.PixelFormats;
using SixLabors.ImageSharp.Processing;
using System.Reflection;
using System.Reflection.Emit;
namespace Processing.Filters
{

    public struct BoundingBox
    {
        public int X, Y, Width, Height;
        public BoundingBox(int x, int y, int w, int h)
        {
            X = x; Y = y; Width = w; Height = h;
        }
    }
    public class Yolo : IImageFilter
    {


        public string Name => "yolo";
        //private static readonly string modelPath = "C:\\Users\\Cosmin\\IdeaProjects\\Espresso\\Processor2\\Processing\\Utils\\best.onnx";
        //private static readonly string modelPath = Path.Combine(AppContext.BaseDirectory, "Utils", "best.onnx");
        //private static readonly string projectRoot = Path.Combine(AppContext.BaseDirectory, "..", "..", "..");
        //private static readonly string modelPath = Path.Combine(projectRoot, "Processing", "Utils", "best.onnx");
        private static readonly string modelPath = Path.Combine(AppContext.BaseDirectory, "Utils", "best.onnx");

        private static readonly string[] labels = { "face" };
        //private readonly YoloDetector _detector = new YoloDetector(modelPath, labels);

        public Yolo() { }


        public RgbImage Apply(RgbImage image)
        {
            // 1) from rgb(what we get) to mat -> used for processing
            byte[] img = MatRgbConvertor.RgbImageToByteArray(image);
            int origW = image.Width;
            int origH = image.Height;

            int modelWidth = 320;
            int modelHeight = 320;
            float confThreshold = 0.4f;
            float iouThreshold = 0.45f;


            // 1) Create Image<Rgb24> and copy your RgbImage pixels
            Image<Rgb24> sharpImage = new Image<Rgb24>(image.Width, image.Height);

            for (int y = 0; y < image.Height; y++)
            {
                for (int x = 0; x < image.Width; x++)
                {
                    var px = image.Pixels[y, x];
                    sharpImage[x, y] = new Rgb24(px.R, px.G, px.B);
                }
            }

            // 2) Resize to model dimensions
            sharpImage.Mutate(ctx => ctx.Resize(modelWidth, modelHeight));

            // 3) Copy pixel data directly to a byte array
            byte[] pixelBytes = new byte[modelWidth * modelHeight * 3]; // RGB24
            sharpImage.CopyPixelDataTo(pixelBytes);

            // 4) Convert bytes to floats in [0,1] range
            float[] input = new float[pixelBytes.Length];
            for (int i = 0; i < pixelBytes.Length; i++)
            {
                input[i] = pixelBytes[i] / 255f;
            }



            // Create tensor [1,3,H,W]
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

            // 4) running the ONNX inference
            using var session = new InferenceSession(modelPath);
            var inputs = new List<NamedOnnxValue>
    {
        NamedOnnxValue.CreateFromTensor(session.InputMetadata.Keys.First(), inputTensor)
    };
            using var results = session.Run(inputs);
            // 5) Output: [1, 5, anchors]
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

                if (conf < confThreshold)
                    continue;

                // Convert from model [320x320] coordinates back to original image size
                int x1 = (int)((x - w / 2) * origW / modelWidth);
                int y1 = (int)((y - h / 2) * origH / modelHeight);
                int width = (int)(w * origW / modelWidth);
                int height = (int)(h * origH / modelHeight);

                boxes.Add(new BoundingBox(x1, y1, width, height));
                scores.Add(conf);
            }


            // 6) apply NMS
            var indices = Nms(boxes, scores, iouThreshold);

            // img: byte[] în format RGB
            // width, height: dimensiunile originale
            for (int idx2 = 0; idx2 < indices.Count; idx2++)
            {
                int i = indices[idx2];
                var box = boxes[i];

                // desenăm marginile dreptunghiului
                for (int x = box.X; x < box.X + box.Width && x < origW; x++)
                {
                    if (box.Y >= 0 && box.Y < origH)
                    {
                        int topIdx = (box.Y * origW + x) * 3;
                        img[topIdx] = 255;     // R
                        img[topIdx + 1] = 0;   // G
                        img[topIdx + 2] = 0;   // B
                    }
                    if (box.Y + box.Height >= 0 && box.Y + box.Height < origH)
                    {
                        int bottomIdx = ((box.Y + box.Height) * origW + x) * 3;
                        img[bottomIdx] = 255;
                        img[bottomIdx + 1] = 0;
                        img[bottomIdx + 2] = 0;
                    }
                }

                for (int y = box.Y; y < box.Y + box.Height && y < origH; y++)
                {
                    if (box.X >= 0 && box.X < origW)
                    {
                        int leftIdx = (y * origW + box.X) * 3;
                        img[leftIdx] = 255;
                        img[leftIdx + 1] = 0;
                        img[leftIdx + 2] = 0;
                    }
                    if (box.X + box.Width >= 0 && box.X + box.Width < origW)
                    {
                        int rightIdx = (y * origW + box.X + box.Width) * 3;
                        img[rightIdx] = 255;
                        img[rightIdx + 1] = 0;
                        img[rightIdx + 2] = 0;
                    }
                }
            }


            Console.WriteLine($"Detected {boxes.Count} boxes, kept {indices.Count} after NMS");


            // img rămâne tot un byte[] în format RGB
            return MatRgbConvertor.ByteArrayToRgbImage(img,origW,origH);


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
