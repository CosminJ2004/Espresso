using Microsoft.ML.OnnxRuntime;
using Microsoft.ML.OnnxRuntime.Tensors;
using Newtonsoft.Json;
using Processing.Interfaces;
using Processing.Models;
using Processing.Utils;
using SixLabors.Fonts;
using SixLabors.ImageSharp;
using SixLabors.ImageSharp.Drawing;
using SixLabors.ImageSharp.Drawing.Processing;
using SixLabors.ImageSharp.PixelFormats;
using SixLabors.ImageSharp.Processing;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.Json;

using Path = System.IO.Path;
using SystemFonts = SixLabors.Fonts.SystemFonts;

namespace Processing.Filters
{
    public class ArcfaceFilter : IImageFilter
    {
        public string Name => "arcface";
        private readonly string modelPath = Path.Combine(AppContext.BaseDirectory, "Utils", "arcface.onnx");
        private readonly string facesDbPath = Path.Combine(AppContext.BaseDirectory, "Utils", "faces_db4.json");

        private static float CosineSimilarity(float[] a, float[] b)
        {
            float dot = 0f, magA = 0f, magB = 0f;
            for (int i = 0; i < a.Length; i++)
            {
                dot += a[i] * b[i];
                magA += a[i] * a[i];
                magB += b[i] * b[i];
            }
            return dot / (float)(Math.Sqrt(magA) * Math.Sqrt(magB));
        }

        private static float[,,,] PreprocessFace(Image<Rgb24> faceImage)
        {
            faceImage.Mutate(ctx => ctx.Resize(112, 112));
            float[,,,] tensor = new float[1, 112, 112, 3];

            faceImage.ProcessPixelRows(accessor =>
            {
                for (int y = 0; y < 112; y++)
                {
                    var row = accessor.GetRowSpan(y);
                    for (int x = 0; x < 112; x++)
                    {
                        tensor[0, y, x, 0] = row[x].B / 127.5f - 1f;
                        tensor[0, y, x, 1] = row[x].G / 127.5f - 1f;
                        tensor[0, y, x, 2] = row[x].R / 127.5f - 1f;
                    }
                }
            });

            return tensor;
        }

        private static Image<Rgb24> ConvertToImageSharp(RgbImage rgbImage)
        {
            var imgSharp = new Image<Rgb24>(rgbImage.Width, rgbImage.Height);
            for (int y = 0; y < rgbImage.Height; y++)
                for (int x = 0; x < rgbImage.Width; x++)
                {
                    var px = rgbImage.Pixels[y, x];
                    imgSharp[x, y] = new Rgb24(px.R, px.G, px.B);
                }
            return imgSharp;
        }

        public RgbImage Apply(RgbImage image)
        {
            if (!File.Exists(modelPath) || !File.Exists(facesDbPath))
            {
                Console.WriteLine("Modelul ArcFace sau baza de date nu au fost găsite.");
                return image;
            }

            //var knownFaces = JsonConvert.DeserializeObject<Dictionary<string, float[]>>(File.ReadAllText(facesDbPath));
            var knownFaces = System.Text.Json.JsonSerializer.Deserialize<Dictionary<string, List<float[]>>>(File.ReadAllText(facesDbPath));

            var imgSharp = ConvertToImageSharp(image);

            YoloDetector yolo = new YoloDetector();
            List<FaceDetected> facesDetected = yolo.ApplyAndExtract(image);

            using var arcSession = new InferenceSession(modelPath);
            var font = SystemFonts.CreateFont("Arial", 16);

            foreach (var face in facesDetected)
            {
                var faceImgSharp = ConvertToImageSharp(face.FaceImage);
                var tensorData = PreprocessFace(faceImgSharp);
                var flattened = tensorData.Cast<float>().ToArray();
                var tensor = new DenseTensor<float>(flattened, new int[] { 1, 112, 112, 3 });

                var inputs = new List<NamedOnnxValue>
                {
                    NamedOnnxValue.CreateFromTensor("input_1", tensor)
                };

                using var results = arcSession.Run(inputs);
                var embedding = results.First().AsEnumerable<float>().ToArray();
                float norm = (float)Math.Sqrt(embedding.Sum(x => x * x));
                embedding = embedding.Select(x => x / norm).ToArray();

                string matchedName = "Unknown";
                float maxSim = -1f;


                foreach (var kv in knownFaces) // kv.Value este acum List<List<float>>
                {
                    foreach (var emb in kv.Value) // iterăm prin fiecare embedding al persoanei
                    {
                        float sim = CosineSimilarity(embedding, emb);
                        if (sim > maxSim)
                        {
                            maxSim = sim;
                            matchedName = sim > 0.55f ? kv.Key : "nu stim";
                        }
                    }
                }

                Console.WriteLine($"Best match: {matchedName} with similarity {maxSim}");

                imgSharp.Mutate(ctx =>
                {
                    var box = face.Box;
                    ctx.Draw(Color.Red, 2, new RectangularPolygon(box.X, box.Y, box.Width, box.Height));
                    ctx.DrawText(matchedName, font, Color.Yellow, new PointF(box.X, box.Y - 20));
                });
            }

            var output = new RgbImage(image.Width, image.Height);
            for (int y = 0; y < imgSharp.Height; y++)
                for (int x = 0; x < imgSharp.Width; x++)
                {
                    var px = imgSharp[x, y];
                    output.Pixels[y, x] = new RgbPixel { R = px.R, G = px.G, B = px.B };
                }

            return output;
        }
    }
}
