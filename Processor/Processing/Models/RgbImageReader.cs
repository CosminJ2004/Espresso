using SixLabors.ImageSharp;
using SixLabors.ImageSharp.PixelFormats;

public static class RgbImageReader
{
    public static RgbImage FromFile(string path)
    {
        using var image = Image.Load<Rgb24>(path);
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
}
