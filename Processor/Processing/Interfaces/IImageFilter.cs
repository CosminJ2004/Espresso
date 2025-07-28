using Processing.Models;

public interface IImageFilter
{
    RgbImage Apply(RgbImage image);
    string Name { get; }
}