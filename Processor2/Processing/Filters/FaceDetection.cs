using OpenCvSharp;
using Processing.Convertors;
using Processing.Interfaces;
using Processing.Models;
using System.Reflection.Metadata.Ecma335;
namespace Processing.Filters
{
    public class FaceDetection : IImageFilter
    {
        public string Name => "faceblur";

        public void DetectFacesAndPlates(Mat image)
        {
            //var faceCascade = new CascadeClassifier("Datasets/haarcascade_frontalface_default.xml");
            //string faceCascadePath = @"C:\Users\Cosmin\IdeaProjects\Espresso\Processing\Datasets\haarcascade_frontalface_default.xml";
            string baseDir = AppDomain.CurrentDomain.BaseDirectory;
            //string faceCascadePath = Path.Combine(baseDir,"Processing", "Datasets", "haarcascade_frontalface_default.xml");


            string faceCascadePath = @"C:\Users\Cosmin\IdeaProjects\Espresso\Processor2\Processing\Datasets\haarcascade_frontalface_default.xml";


            if (!File.Exists(faceCascadePath))
            {
                throw new FileNotFoundException($"File not found: {faceCascadePath}");
            }

            string plateCascadePath = @"C:\Users\Cosmin\IdeaProjects\Espresso\Processor2\Processing\Datasets\haarcascade_russian_plate_number.xml";

            if (!File.Exists(faceCascadePath))
            {
                throw new FileNotFoundException($"File not found: {plateCascadePath}");
            }

            CascadeClassifier faceCascade = new CascadeClassifier(faceCascadePath);

            CascadeClassifier plateCascade = new CascadeClassifier(plateCascadePath);


            //var plateCascade = new CascadeClassifier("path/to/haarcascade_russian_plate_number.xml");

            var faces = faceCascade.DetectMultiScale(
                image,
                 scaleFactor: 1.1,       // cât de mult scazi dimensiunea imaginii la fiecare pas (ex: 10%)
                minNeighbors: 5,        // câte detecții trebuie să existe ca să confirmi o față (mai mare = mai puține false
                 flags: HaarDetectionTypes.ScaleImage,
                minSize: new Size(30, 30)  // dimensiunea minimă a feței detectate
  );
            var plates = plateCascade.DetectMultiScale(image, 1.1, 5);

            foreach (var face in faces)
                Cv2.Rectangle(image, face, Scalar.Red, 2);

            foreach (var plate in plates)
                Cv2.Rectangle(image, plate, Scalar.Green, 2);

            //Cv2.ImShow("Detection Result", image);
            Cv2.WaitKey(0);
            Cv2.DestroyAllWindows();
        }

        public RgbImage Apply(RgbImage image)
        {
            //var mat = MatRgbConvertor.RgbImageToMat(image);
            //DetectFacesAndPlates(mat);
            //return MatRgbConvertor.MatToRgbImage(mat);/
            return image; // Placeholder, as the actual implementation is not provided
        }

    }
}







