using Processing.Models;

public class DimensionValidation : IImageValidation
{
    private const int MinWidth = 300;
    private const int MinHeight = 300;
    private const int MaxWidth = 1920;
    private const int MaxHeight = 1080;
    private const string ErrorMessage = "Image dimensions do not meet the required constraints.";
    
    public bool Validate(ImagePackage image)
    {
        return image.Width >= MinWidth && image.Width <= MaxWidth &&
               image.Height >= MinHeight && image.Height <= MaxHeight;
    }

    public string GetErrorMessage()
    {
        return ErrorMessage;
    }
}
