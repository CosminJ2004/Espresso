using Processing.Models;
using SixLabors.ImageSharp;
using SixLabors.ImageSharp.Formats.Jpeg;
using SixLabors.ImageSharp.PixelFormats;
using Processing.Interfaces;

namespace Processing.Utils;
public class RgbImageWriter : IImageWriter
{
    public void WriteToFile(string path, RgbImage image)
    {
     
    }

    public void WriteToStream(Stream outputStream, RgbImage image)
    {
        using var img = new Image<Rgba32>(image.Width, image.Height);

        // Folosim ProcessPixelRows, dar fara operații suplimentare
        img.ProcessPixelRows(accessor =>
        {
            for (int y = 0; y < image.Height; y++)
            {
                var row = accessor.GetRowSpan(y);
                for (int x = 0; x < image.Width; x++)
                {
                    var pixel = image.Pixels[y, x];
                    // Copiem direct valorile RGB, ignoram A pentru ca e 255 implicit
                    row[x] = new Rgba32(pixel.R, pixel.G, pixel.B);
                }
            }
        });

        // Salvare JPEG cu calitate optimizata
        img.Save(outputStream, new JpegEncoder { Quality = 75 });
    }
    public string GetFileExtension(string filePath)
    {
        return System.IO.Path.GetExtension(filePath).ToLowerInvariant();
    }
}
