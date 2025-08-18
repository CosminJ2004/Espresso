using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.Hosting;
using Processing.Filters;
using Processing.Interfaces;
using Processing.Utils;
using Processor2.Controllers;



var arcface = new ArcFaceHelper("C:\\Users\\Cosmin\\IdeaProjects\\Espresso\\Processor2\\Processing\\Utils\\arcface.onnx");

var emb1 = arcface.GetEmbedding("poza1.jpeg");
var emb2 = arcface.GetEmbedding("poza2.jpeg"); // exact aceeași poză

float sim = ArcFaceHelper.CosineSimilarity(emb1, emb2);
Console.WriteLine($"Similaritate cosine: {sim}");


var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();

// DI din proiectul Processing
builder.Services.AddScoped<RgbImageReader>();
builder.Services.AddScoped<RgbImageWriter>();
builder.Services.AddScoped<FilterHandler>();

builder.Services.AddScoped<IImageFilter, GrayscaleFilter>();
builder.Services.AddScoped<IImageFilter, InvertFilter>();
builder.Services.AddScoped<IImageFilter, GaussianFilter>();
builder.Services.AddScoped<IImageFilter, DoNothingFilter>();

//builder.Services.AddScoped<IImageFilter, FaceDetection>();
builder.Services.AddScoped<IImageFilter, Yolo>();
builder.Services.AddScoped<IImageFilter, SepiaFilter>();

builder.Services.AddScoped<IImageFilter, ArcfaceFilter>();


var app = builder.Build();

app.MapControllers();

app.Run("http://0.0.0.0:80");


// Clasa Program partială pentru testare
public partial class Program { }
