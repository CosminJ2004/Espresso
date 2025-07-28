using SixLabors.ImageSharp;
using SixLabors.ImageSharp.PixelFormats;
using Processing.Models;

namespace Processing.Utils;

public static class ImageReader
{
    public static ImagePackage FromFile(string path)
    {
        // ToDo: Log.
        if (!File.Exists(path))
        {
            Environment.Exit(0);
            // ToDo: Log.
        }

        byte[] bytes = File.ReadAllBytes(path);
        using var stream = new MemoryStream(bytes);
        var image = Image.Load<Rgba32>(stream); 

        // ToDo: Log.
        return new ImagePackage(bytes, image);
    }
}
