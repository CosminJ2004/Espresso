using Processing.Models;

namespace Processing.Interfaces

{
    public interface IImageReader
    {
        RgbImage ReadFromFile(string path);
        RgbImage ReadFromStream(Stream inputStream);
        string GetFileExtension(string path);
    }
    
}
