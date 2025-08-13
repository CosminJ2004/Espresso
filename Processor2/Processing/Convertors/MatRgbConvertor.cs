using OpenCvSharp;
using Processing.Models;
using System;

namespace Processing.Convertors
{
    public class MatRgbConvertor
    {

        public static Mat RgbImageToMat(RgbImage rgb)
        {
            var mat = new Mat(rgb.Height, rgb.Width, MatType.CV_8UC3);

            for (int y = 0; y < rgb.Height; y++)
            {
                for (int x = 0; x < rgb.Width; x++)
                {
                    var px = rgb.Pixels[y, x];
                    // OpenCvSharp uses BGR order by default
                    mat.Set(y, x, new Vec3b(px.B, px.G, px.R));
                }
            }

            return mat;
        }
        public static RgbImage MatToRgbImage(Mat mat)
        {
            int width = mat.Width;
            int height = mat.Height;
            var rgb = new RgbImage(width, height);

            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    var vec = mat.At<Vec3b>(y, x);
                    rgb.Pixels[y, x] = new RgbPixel(vec.Item2, vec.Item1, vec.Item0); // R,G,B order
                }
            }

            return rgb;
        }


    }
}
