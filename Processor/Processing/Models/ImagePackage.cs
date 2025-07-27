using SixLabors.ImageSharp;
using SixLabors.ImageSharp.Formats;

namespace Processing.Models;

public class ImagePackage
{
    private readonly byte[] _bytes;
    private readonly Image _image;
    private readonly IImageFormat _imageFormat;

    public byte[] Bytes => _bytes;
    public Image Image => _image;
    public int Width => _image.Width;
    public int Height => _image.Height;
    public IImageFormat ImageFormat => _imageFormat;

    public ImagePackage(byte[] bytes, Image image)
    {
        _bytes = bytes;
        _image = image;
        _imageFormat = Image.DetectFormat(bytes);
    }
}

