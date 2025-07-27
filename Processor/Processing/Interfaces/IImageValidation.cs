using Processing.Models;

public interface IImageValidation
{
    public bool Validate(ImagePackage image);
    public string GetErrorMessage();
}