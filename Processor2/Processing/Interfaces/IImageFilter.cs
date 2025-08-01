using Processing.Models;

namespace Processing.Interfaces;    
public interface IImageFilter
{
    RgbImage Apply(RgbImage image);
    string Name { get; }
}