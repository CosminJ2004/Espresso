namespace Processing.Models;
public class RgbImage
{
    public int Width { get; }
    public int Height { get; }
    public RgbPixel[,] Pixels { get; }

    public RgbImage(int width, int height)
    {
        Width = width;
        Height = height;
        Pixels = new RgbPixel[height, width];
    }
}

public struct RgbPixel
{
    public byte R;
    public byte G;
    public byte B;


    public RgbPixel(byte r, byte g, byte b) : this()
    {
        R = r;
        G = g;
        B = b;
    }
}