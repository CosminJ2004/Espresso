using Processing.Models;

namespace Processing.Interfaces

{
    public interface IImageWriter
    {
        void WriteToFile(string path, RgbImage image);
        void WriteToStream(Stream outputStream, RgbImage image);
       
        string GetFileExtension(string filePath);

    }
}
