using Processing.Models;
using Processing.Utils;

class Program
{
    static void Main()
    {
        var imagePath = @"C:\Users\USER\Desktop\Image.jpg";
        ImagePackage image = ImageReader.FromFile(imagePath);
    }
}
