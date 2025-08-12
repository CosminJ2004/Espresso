using Processing.Interfaces;
using Processing.Models;

namespace Processing.Filters;

public class SepiaFilter : IImageFilter
{
    public string Name => "Sepia";

    public RgbImage Apply(RgbImage image)
    {
        var result = new RgbImage(image.Width, image.Height);
        for (int y = 0; y < image.Height; y++)
        {
            for (int x = 0; x < image.Width; x++)
            {
                var pixel = image.Pixels[y, x];

                int tr = (int)(0.393 * pixel.R + 0.769 * pixel.G + 0.189 * pixel.B);
                int tg = (int)(0.349 * pixel.R + 0.686 * pixel.G + 0.168 * pixel.B);
                int tb = (int)(0.272 * pixel.R + 0.534 * pixel.G + 0.131 * pixel.B);

                byte r = (byte)(tr > 255 ? 255 : tr);
                byte g = (byte)(tg > 255 ? 255 : tg);
                byte b = (byte)(tb > 255 ? 255 : tb);

                result.Pixels[y, x] = new RgbPixel
                {
                    R = r,
                    G = g,
                    B = b
                };
            }
        }
        return result;
    }
}
