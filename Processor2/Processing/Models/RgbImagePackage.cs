namespace Processing.Models;
public class RgbImagePackage
{
    private readonly byte[] _bytes;
    private readonly RgbImage _image;
    public byte[] Bytes => _bytes;
    public RgbImage Image => _image;
    public int Width => _image.Width;
    public int Height => _image.Height;

    public RgbImagePackage(byte[] bytes, RgbImage image)
    {
        _bytes = bytes;
        _image = image;
    }

    public RgbImagePackage(RgbImage image)
    {
        _image = image;
        _bytes = null;
    }
}