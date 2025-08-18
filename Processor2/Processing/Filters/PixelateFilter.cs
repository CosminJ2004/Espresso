using Processing.Interfaces;
using Processing.Models;

namespace Processing.Filters;

public class PixelateFilter : IImageFilter
{
    public string Name => "Pixelate";

    public RgbImage Apply(RgbImage image)
    {
        int blockSize = 8;
        var result = new RgbImage(image.Width, image.Height);

        for (int y = 0; y < image.Height; y += blockSize)
        {
            for (int x = 0; x < image.Width; x += blockSize)
            {
                int rSum = 0, gSum = 0, bSum = 0;
                int pixelCount = 0;

                for (int by = y; by < Math.Min(y + blockSize, image.Height); by++)
                {
                    for (int bx = x; bx < Math.Min(x + blockSize, image.Width); bx++)
                    {
                        var pixel = image.Pixels[by, bx];
                        rSum += pixel.R;
                        gSum += pixel.G;
                        bSum += pixel.B;
                        pixelCount++;
                    }
                }

                byte avgR = (byte)(rSum / pixelCount);
                byte avgG = (byte)(gSum / pixelCount);
                byte avgB = (byte)(bSum / pixelCount);

                for (int by = y; by < Math.Min(y + blockSize, image.Height); by++)
                {
                    for (int bx = x; bx < Math.Min(x + blockSize, image.Width); bx++)
                    {
                        result.Pixels[by, bx] = new RgbPixel
                        {
                            R = avgR,
                            G = avgG,
                            B = avgB
                        };
                    }
                }
            }
        }

        return result;
    }
}