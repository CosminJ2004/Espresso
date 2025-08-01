using Processing.Models;
using SixLabors.ImageSharp;
using SixLabors.ImageSharp.Formats.Jpeg;
using SixLabors.ImageSharp.Formats.Png;
using SixLabors.ImageSharp.PixelFormats;
using System.Drawing;
using System.Drawing.Imaging;
using Processing.Interfaces;

namespace Processing.Utils;
public class RgbImageWriter: IImageWriter
{
    public void WriteToFile(string path, RgbImage image)
    {
        using var img = new Image<Rgba32>(image.Width, image.Height);
        for (int y = 0; y < image.Height; y++)
        {
            for (int x = 0; x < image.Width; x++)
            {
                var pixel = image.Pixels[y, x];
                img[x, y] = new Rgba32(pixel.R, pixel.G, pixel.B);
            }
        }
        img.Save(path, new JpegEncoder());
    }

  

    public void WriteToStream( Stream outputStream, RgbImage image)
    {
        using var img = new Image<Rgba32>(image.Width, image.Height);
        for (int y = 0; y < image.Height; y++)
        {
            for (int x = 0; x < image.Width; x++)
            {
                var pixel = image.Pixels[y, x];
                img[x, y] = new Rgba32(pixel.R, pixel.G, pixel.B);
            }
        }

        img.Save(outputStream, new PngEncoder());
    }
    public string GetFileExtension(string filePath)
    {
        return System.IO.Path.GetExtension(filePath).ToLowerInvariant();
    }

}