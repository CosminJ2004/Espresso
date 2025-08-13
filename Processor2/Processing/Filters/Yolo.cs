using OpenCvSharp;
using Processing.Models;
using Processing.Utils;
using Processing.Convertors;
using Processing.Interfaces;
namespace Processing.Filters
{
    public class Yolo: IImageFilter
    {


        public string Name => "yolo";
        private readonly YoloDetector _detector;

        public Yolo()
        {
            string modelPath="C:\\Users\\Cosmin\\IdeaProjects\\Espresso\\Processor2\\Processing\\Utils\\best.onnx";
            string[] labels ={ "face"};
            _detector = new YoloDetector(modelPath, labels);
        }

        public RgbImage Apply(RgbImage image)
        {
            // Convert RgbImage -> OpenCv Mat
            Mat mat = MatRgbConvertor.RgbImageToMat(image);

            // Rulează detectarea
            var detections = _detector.Detect(mat);

            // Desenează dreptunghiurile detectate
            foreach (var det in detections)
            {
                Cv2.Rectangle(mat, det.Box, Scalar.Red, 2);
                Cv2.PutText(mat, $"{det.Label} {det.Confidence:P1}",
                    new Point(det.Box.X, det.Box.Y - 5),
                    HersheyFonts.HersheySimplex, 0.5, Scalar.Yellow, 1);
            }

            // Convert Mat -> RgbImage
            return MatRgbConvertor.MatToRgbImage(mat);
        }

    }
}
