using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting.Server;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.Extensions.Hosting;
using Processing.Filters;
using Processing.Interfaces;
using Processing.Models;
using Processing.Utils;
using System.ComponentModel;

public class Program
{
    public static void Main(string[] args)
    {

        var builder = WebApplication.CreateBuilder(args);


        //   CreateBuilder(args)
        //        ↓
        //   Configure Services, Logging, Config, DI
        //        ↓
        //      Build()
        //        ↓
        //   Compose Middleware, Routes, DI container
        //        ↓
        //       Run()
        //        ↓
        //   Start Kestrel(server)
        //        ↓
        //   Accept HTTP requests + execute mapped endpoints


        // builder.service -> access to the DI services 

        // register components
        builder.Services.AddScoped<RgbImageReader>();
        builder.Services.AddScoped<RgbImageWriter>();

        builder.Services.AddScoped<FilterHandler>();


        builder.Services.AddScoped<IImageFilter, GrayscaleFilter>();
        builder.Services.AddScoped<IImageFilter, InvertFilter>();
        builder.Services.AddScoped<IImageFilter, GaussianFilter>();
        builder.Services.AddScoped<IImageFilter, DoNothingFilter>();


        var app = builder.Build();
        app.MapGet("/test", () => "OK");

        // Map the filter endpoint
        // This endpoint expects a form-data request with an image file and a filter name
        //dependency injection  for reading and writing images, as well as applying filters
        app.MapPost("/filter", async (
            HttpContext context,
            RgbImageReader reader,
            RgbImageWriter writer,
            FilterHandler handler) =>
                {
            try
            {
                var request = context.Request;
                var filterName = request.Query["filter"].ToString();
                if (string.IsNullOrEmpty(filterName))
                    return Results.BadRequest("Missing filter name");

                if (!request.HasFormContentType)
                    return Results.BadRequest("Content-Type must be form-data.");

                var form = await request.ReadFormAsync();
                var file = form.Files["image"];
                if (file == null)
                    return Results.BadRequest("Missing image");

                using var stream = file.OpenReadStream();
                using var ms = new MemoryStream();
                await stream.CopyToAsync(ms);
                ms.Position = 0;

                var inputImage = reader.ReadFromStream(ms);

                
                var outputImage = handler.Apply(filterName, inputImage);

                var outStream = new MemoryStream();
                writer.WriteToStream(outStream, outputImage);
                outStream.Position = 0;

                return Results.File(outStream, "image/jpeg");
            }
            catch (InvalidOperationException ex)
            {
                return Results.BadRequest(new { error = ex.Message });
            }
            catch (Exception ex)
            {
              
                return Results.Problem(ex.Message+ " "+ex.StackTrace);
            }
        });
        app.Run("http://0.0.0.0:80");

         //app.Run("http://localhost:80");
        

    }
}
