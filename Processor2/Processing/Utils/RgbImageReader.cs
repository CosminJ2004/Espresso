using Processing.Models;
using SixLabors.ImageSharp;
using SixLabors.ImageSharp.PixelFormats;
using static System.Net.Mime.MediaTypeNames;
using Processing.Interfaces;
using System.IO;

namespace Processing.Utils;
public  class RgbImageReader: IImageReader
{
    public RgbImage ReadFromFile(string path)
    {
        using var image = SixLabors.ImageSharp.Image.Load<Rgb24>(path);
        var result = new RgbImage(image.Width, image.Height);

        for (int y = 0; y < image.Height; y++)
        {
            for (int x = 0; x < image.Width; x++)
            {
                var pixel = image[x, y];
                result.Pixels[y, x] = new RgbPixel
                {
                    R = pixel.R,
                    G = pixel.G,
                    B = pixel.B
                };
            }
        }

        return result;
    }
   public RgbImage ReadFromStream(Stream stream)
{
    using var image = SixLabors.ImageSharp.Image.Load<Rgb24>(stream);
    var rgbImage = new RgbImage(image.Width, image.Height);

    image.ProcessPixelRows(accessor =>
    {
        for (int y = 0; y < image.Height; y++)
        {
            var row = accessor.GetRowSpan(y);
            for (int x = 0; x < image.Width; x++)
            {
                var pixel = row[x];
                rgbImage.Pixels[y, x] = new RgbPixel(pixel.R, pixel.G, pixel.B);
            }
        }
    });

    return rgbImage;
}

    public string GetFileExtension(string path)
    {
        return System.IO.Path.GetExtension(path).ToLowerInvariant();
    }
}
