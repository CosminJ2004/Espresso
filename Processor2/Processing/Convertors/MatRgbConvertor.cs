using OpenCvSharp;
using Processing.Models;
using System;

namespace Processing.Convertors
{
    public class MatRgbConvertor
    {

        public static byte[] RgbImageToByteArray(RgbImage rgb)
        {
            byte[] data = new byte[rgb.Width * rgb.Height * 3];
            int index = 0;

            for (int y = 0; y < rgb.Height; y++)
            {
                for (int x = 0; x < rgb.Width; x++)
                {
                    var px = rgb.Pixels[y, x];
                    data[index++] = px.B; // B
                    data[index++] = px.G; // G
                    data[index++] = px.R; // R
                }
            }

            return data;
        }

        public static RgbImage ByteArrayToRgbImage(byte[] data, int width, int height)
        {
            var rgb = new RgbImage(width, height);
            int index = 0;

            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    byte b = data[index++];
                    byte g = data[index++];
                    byte r = data[index++];
                    rgb.Pixels[y, x] = new RgbPixel(r, g, b);
                }
            }

            return rgb;
        }


    }
}
