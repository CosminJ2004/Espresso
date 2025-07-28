class Program
{
    static void Main()
    {
        var imagePath = @"C:\Users\Cosmin\Desktop\Image.jpg"; 
        RgbImage image = RgbImageReader.FromFile(imagePath); 
        var imagePackage = new RgbImagePackage(image);

        Console.WriteLine($"Image dimensions: {imagePackage.Width} x {imagePackage.Height}");

        var filter = new InvertFilter();
        var processedPackage = filter.Apply(image);

        Console.WriteLine($"Processed image dimensions: {processedPackage.Width} x {processedPackage.Height}");

        var outputPath = @"C:\Users\Cosmin\Desktop\Image_invert.jpg";
        RgbImageWriter.Save(processedPackage, outputPath); 
        Console.WriteLine($"Processed image saved to: {outputPath}");
    }
}