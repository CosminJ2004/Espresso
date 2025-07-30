using Processing.Models;
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
    public static RgbImage FromStream(Stream stream)
    {
        using var image = Image.Load<Rgb24>(stream);
        var rgbImage = new RgbImage(image.Width, image.Height);

        for (int y = 0; y < image.Height; y++)
        {
            for (int x = 0; x < image.Width; x++)
            {
                var pixel = image[x, y];
                rgbImage.Pixels[y, x] = new RgbPixel(pixel.R, pixel.G, pixel.B);
            }
        }
        return rgbImage;
    }
}
