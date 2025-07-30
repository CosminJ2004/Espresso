using Microsoft.AspNetCore.Mvc.Filters;
using Processing.Interfaces;
using Processing.Models;

namespace Processing.Filters;

public class GrayscaleFilter : IImageFilter
{
    public string Name => "Grayscale";

    public RgbImage Apply(RgbImage image)
    {
        var result = new RgbImage(image.Width, image.Height);
        for (int y = 0; y < image.Height; y++)
        {
            for (int x = 0; x < image.Width; x++)
            {
                var pixel = image.Pixels[y, x];
                //byte gray = (byte)((pixel.R + pixel.G + pixel.B) / 3);

                byte gray = (byte)(0.299 * pixel.R + 0.587 * pixel.G + 0.114 * pixel.B);
                result.Pixels[y, x] = new RgbPixel
                {
                    R = gray,
                    G = gray,
                    B = gray
                };
            }
        }
        return result;
    }
}
