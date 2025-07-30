using Microsoft.AspNetCore.Mvc.Filters;
using Processing.Interfaces;
using Processing.Models;
using SixLabors.ImageSharp;
using SixLabors.ImageSharp.PixelFormats;
using SixLabors.ImageSharp.Processing;


namespace Processing.Filters;
public class InvertFilter : IImageFilter
{
    public string Name => "Invert";

    public RgbImage Apply(RgbImage image)
    {
        var result = new RgbImage(image.Width, image.Height);
        for (int y = 0; y < image.Height; y++)
        {
            for (int x = 0; x < image.Width; x++)
            {
                var pixel = image.Pixels[y, x];
                result.Pixels[y, x] = new RgbPixel
                {
                    R = (byte)(255 - pixel.R),
                    G = (byte)(255 - pixel.G),
                    B = (byte)(255 - pixel.B)
                };
            }
        }
        return result;
    }
}