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

        private readonly YoloDetector yolo;

        private readonly InferenceSession arcSession;
        private readonly Dictionary<string, List<float[]>> knownFaces;

        public ArcfaceFilter(YoloDetector yolo)
        {

            
            if (!File.Exists(modelPath) || !File.Exists(facesDbPath))
                throw new FileNotFoundException("Modelul ArcFace sau baza de date nu au fost găsite.");

            arcSession = new InferenceSession(modelPath);
            knownFaces = System.Text.Json.JsonSerializer.Deserialize<Dictionary<string, List<float[]>>>(File.ReadAllText(facesDbPath));
            this.yolo = yolo;
        }

        //private static float CosineSimilarity(float[] a, float[] b)
        //{
        //    float dot = 0f, magA = 0f, magB = 0f;
        //    for (int i = 0; i < a.Length; i++)
        //    {
        //        dot += a[i] * b[i];
        //        magA += a[i] * a[i];
        //        magB += b[i] * b[i];
        //    }
        //    return dot / (float)(Math.Sqrt(magA) * Math.Sqrt(magB));
        //}


        float CosineSimilarity(float[] a, float[] b)
        {
            float sim = 0;
            for (int i = 0; i < a.Length; i++)
                sim += a[i] * b[i];  // nu mai trebuie divizare, sunt normalizate
            return sim;
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
            //var knownFaces = System.Text.Json.JsonSerializer.Deserialize<Dictionary<string, List<float[]>>>(File.ReadAllText(facesDbPath));

            var imgSharp = ConvertToImageSharp(image);

           
            List<FaceDetected> facesDetected = yolo.ApplyAndExtract(image);

            //using var arcSession = new InferenceSession(modelPath);
            //var font = SystemFonts.CreateFont("Noto Sans", 16);
            var font = SystemFonts.CreateFont("DejaVu Sans", 16);

            foreach (var fam in SystemFonts.Families)
            {
                Console.WriteLine($"Font: {fam.Name}");
            }


            var faceMatches = new List<(FaceDetected Face, string Name)>();

            foreach (var face in facesDetected)
            {
                var faceBox = face.Box;
                var faceImgSharp = imgSharp.Clone(ctx => ctx.Crop(new Rectangle(faceBox.X, faceBox.Y, faceBox.Width, faceBox.Height)));
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

                float maxSim = -1f;
                string matchedName = "Unknown";

                foreach (var kv in knownFaces)
                {
                    foreach (var emb in kv.Value)
                    {
                        float sim = CosineSimilarity(embedding, emb);
                        if (sim > 0.55f && sim > maxSim)
                        {
                            maxSim = sim;
                            matchedName = kv.Key;
                        }
                    }
                }

                faceMatches.Add((face, matchedName));
                //Console.WriteLine($"Face at ({faceBox.X},{faceBox.Y}) matched with {matchedName} ({maxSim})");
            }

            // Desenăm toate fețele cu numele lor
            imgSharp.Mutate(ctx =>
            {
                foreach (var (face, name) in faceMatches)
                {
                    var box = face.Box;
                    ctx.Draw(Color.Red, 2, new RectangularPolygon(box.X, box.Y, box.Width, box.Height));
                    ctx.DrawText(name, font, Color.Yellow, new PointF(box.X, Math.Max(box.Y - 20, 0)));
                }
            });

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
