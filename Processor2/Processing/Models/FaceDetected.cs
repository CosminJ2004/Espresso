using System.Drawing;

namespace Processing.Models
{
    public class FaceDetected
    {
        public RgbImage FaceImage { get; set; }
        public BoundingBox Box { get; set; } // nu System.Drawing
    }

}
