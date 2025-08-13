using Microsoft.ML.OnnxRuntime;
using Microsoft.ML.OnnxRuntime.Tensors;
using OpenCvSharp;
using Processing.Convertors;
using Processing.Interfaces;
using Processing.Models;
using Processing.Utils;
using System.Reflection;
using System.Reflection.Emit;
namespace Processing.Filters
{
    public class Yolo: IImageFilter
    {


        public string Name => "yolo";
        //private static readonly string modelPath = "C:\\Users\\Cosmin\\IdeaProjects\\Espresso\\Processor2\\Processing\\Utils\\best.onnx";
        //private static readonly string modelPath = Path.Combine(AppContext.BaseDirectory, "Utils", "best.onnx");
        //private static readonly string projectRoot = Path.Combine(AppContext.BaseDirectory, "..", "..", "..");
        //private static readonly string modelPath = Path.Combine(projectRoot, "Processing", "Utils", "best.onnx");
        private static readonly string modelPath = Path.Combine(AppContext.BaseDirectory, "Utils", "best.onnx");

        private static readonly string[] labels = { "face" };
        private readonly YoloDetector _detector = new YoloDetector(modelPath, labels);

        public Yolo() { }


        public RgbImage Apply(RgbImage image)
        {
            // 1) from rgb(what we get) to mat -> used for processing
            Mat img = MatRgbConvertor.RgbImageToMat(image);

            int origW = img.Cols;
            int origH = img.Rows;

            int modelWidth = 320;
            int modelHeight = 320;
            float confThreshold = 0.4f;
            float iouThreshold = 0.45f;

            // 2) resizing the image for yolo
            Mat resized = new Mat();
            Cv2.Resize(img, resized, new OpenCvSharp.Size(modelWidth, modelHeight));
            resized.ConvertTo(resized, MatType.CV_32FC3, 1.0 / 255);
            Cv2.CvtColor(resized, resized, ColorConversionCodes.BGR2RGB);

            // 3) making it a tensor [1,3,320,320]
            var inputTensor = new DenseTensor<float>(new[] { 1, 3, modelHeight, modelWidth });
            var idx = 0;
            for (int c = 0; c < 3; c++)
                for (int y = 0; y < modelHeight; y++)
                    for (int x = 0; x < modelWidth; x++)
                        inputTensor[0, c, y, x] = resized.At<Vec3f>(y, x)[c];

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

            List<Rect> boxes = new List<Rect>();
            List<float> scores = new List<float>();

            for (int i = 0; i < anchors; i++)
            {
                float x = output[0, 0, i];
                float y = output[0, 1, i];
                float w = output[0, 2, i];
                float h = output[0, 3, i];
                float conf = output[0, 4, i];

                if (conf < confThreshold) continue;

                int x1 = (int)((x - w / 2) * origW / modelWidth);
                int y1 = (int)((y - h / 2) * origH / modelHeight);
                int x2 = (int)((x + w / 2) * origW / modelWidth);
                int y2 = (int)((y + h / 2) * origH / modelHeight);

                boxes.Add(new Rect(x1, y1, x2 - x1, y2 - y1));
                scores.Add(conf);
            }

            // 6) apply NMS
            var indices = Nms(boxes, scores, iouThreshold);

            // 7) creating  boxes
            foreach (var i in indices)
            {
                Cv2.Rectangle(img, boxes[i], Scalar.Red, 2);
                Cv2.PutText(img, $"{scores[i]:0.00}",
                            new Point(boxes[i].X, boxes[i].Y - 5),
                            HersheyFonts.HersheySimplex, 0.5, Scalar.Yellow, 2);
            }

            // 8) converting back to rgbimage
            return MatRgbConvertor.MatToRgbImage(img);
        }

        private List<int> Nms(List<Rect> boxes, List<float> scores, float iouThreshold)
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

        private float IoU(Rect a, Rect b)
        {
            int x1 = Math.Max(a.X, b.X);
            int y1 = Math.Max(a.Y, b.Y);
            int x2 = Math.Min(a.X + a.Width, b.X + b.Width);
            int y2 = Math.Min(a.Y + a.Height, b.Y + b.Height);

            int interArea = Math.Max(0, x2 - x1) * Math.Max(0, y2 - y1);
            int unionArea = a.Width * a.Height + b.Width * b.Height - interArea;

            return unionArea == 0 ? 0 : (float)interArea / unionArea;
        }

    }
}
