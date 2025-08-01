using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Hosting;
using Processing.Models;
using Processing.Filters;
using Processing.Utils;

public class Program
{
    public static void Main(string[] args)
    {
        RgbImageReader rgbImageReader = new RgbImageReader();
        RgbImageWriter rgbImageWriter = new RgbImageWriter();
        var builder = WebApplication.CreateBuilder(args);
        var app = builder.Build();
        app.MapGet("/test", () => "OK");


        app.MapPost("/filter", async (HttpRequest request) =>
        {
            var filterName = request.Query["filter"].ToString();
            if (string.IsNullOrEmpty(filterName))
                return Results.BadRequest("Missing filter name.");

            var form = await request.ReadFormAsync();
            var file = form.Files["image"];
            if (file == null)
                return Results.BadRequest("Missing image file.");

            using var stream = file.OpenReadStream();
            using var ms = new MemoryStream();
            await stream.CopyToAsync(ms);
            ms.Position = 0;

            var inputImage = rgbImageReader.ReadFromStream(ms);


            var outputImage = filterName.ToLower() switch
            {
                "invert" => new InvertFilter().Apply(inputImage),

                "grayscale" => new GrayscaleFilter().Apply(inputImage),
                _ => inputImage

            };

            var outStream = new MemoryStream();
            rgbImageWriter.WriteToStream(outStream,outputImage);
            outStream.Position = 0;

            return Results.File(outStream, "image/jpeg");
        });
        app.Run("http://0.0.0.0:80");

    }
}
