using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.Hosting;
using Microsoft.ML.OnnxRuntime;
using Processing.Filters;
using Processing.Interfaces;
using Processing.Utils;
using Processor2.Controllers;





var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();

// DI din proiectul Processing


builder.Services.AddSingleton(sp =>
{
    var session = new InferenceSession(Path.Combine(AppContext.BaseDirectory, "Utils", "best.onnx"));
    // warmup: rulează o dată o intrare dummy
    return session;
});


builder.Services.AddTransient<RgbImageReader>();
builder.Services.AddTransient<RgbImageWriter>();
builder.Services.AddSingleton<YoloDetector>();

//builder.Services.AddScoped<IImageFilter, FaceDetection>();
builder.Services.AddTransient<IImageFilter, Yolo>();
builder.Services.AddTransient<IImageFilter, SepiaFilter>();
builder.Services.AddTransient<IImageFilter, PixelateFilter>();

builder.Services.AddTransient<IImageFilter, GrayscaleFilter>();
builder.Services.AddTransient<IImageFilter, InvertFilter>();
builder.Services.AddTransient<IImageFilter, GaussianFilter>();
builder.Services.AddTransient<IImageFilter, DoNothingFilter>();
builder.Services.AddSingleton<IImageFilter>(sp => new ArcfaceFilter(sp.GetRequiredService<YoloDetector>()));

builder.Services.AddScoped<FilterHandler>();


var app = builder.Build();

app.MapControllers();

app.Run("http://0.0.0.0:80");


// Clasa Program partială pentru testare
public partial class Program { }
