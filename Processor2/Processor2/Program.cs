using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.Hosting;
using Processing.Filters;
using Processing.Interfaces;
using Processing.Utils;
using Processor2.Controllers;

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
builder.Services.AddScoped<IImageFilter, SepiaFilter>();

var app = builder.Build();

app.MapControllers();

app.Run("http://0.0.0.0:80");


// Clasa Program partială pentru testare
public partial class Program { }
