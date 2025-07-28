using SixLabors.ImageSharp;
using SixLabors.ImageSharp.PixelFormats;
using SixLabors.ImageSharp.Formats.Jpeg;

public static class RgbImageWriter
{
    public static void Save(RgbImage image, string path)
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

    public static void Save(object image, string outputPath)
    {
        if (image is RgbImage rgbImage)
        {
            Save(rgbImage, outputPath);
        }
        else
        {
            throw new ArgumentException("Object must be of type RgbImage.", nameof(image));
        }
    }
}