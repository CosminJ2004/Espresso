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
builder.Services.AddSingleton(sp => new InferenceSession(Path.Combine(AppContext.BaseDirectory, "Utils", "best.onnx")));



builder.Services.AddTransient<RgbImageReader>();
builder.Services.AddTransient<RgbImageWriter>();
builder.Services.AddSingleton<YoloDetector>();

//builder.Services.AddScoped<IImageFilter, FaceDetection>();
builder.Services.AddScoped<IImageFilter, Yolo>();
builder.Services.AddScoped<IImageFilter, SepiaFilter>();
builder.Services.AddScoped<IImageFilter, PixelateFilter>();

builder.Services.AddSingleton<IImageFilter, GrayscaleFilter>();
builder.Services.AddSingleton<IImageFilter, InvertFilter>();
builder.Services.AddSingleton<IImageFilter, GaussianFilter>();
builder.Services.AddSingleton<IImageFilter, DoNothingFilter>();
builder.Services.AddSingleton<IImageFilter>(sp => new ArcfaceFilter(sp.GetRequiredService<YoloDetector>()));

builder.Services.AddScoped<FilterHandler>();


var app = builder.Build();

app.MapControllers();

app.Run("http://0.0.0.0:80");


// Clasa Program partială pentru testare
public partial class Program { }
