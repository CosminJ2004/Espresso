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
        private readonly string facesDbPath = Path.Combine(AppContext.BaseDirectory, "Utils", "faces_db.json");

        private readonly YoloDetector yolo;

        private readonly InferenceSession arcSession;
        private readonly Dictionary<string, List<float[]>> knownFaces;

        public ArcfaceFilter(YoloDetector yolo)
        {

            
            if (!File.Exists(modelPath) || !File.Exists(facesDbPath))
                throw new FileNotFoundException("Arcface model or faces db were not found");

            arcSession = new InferenceSession(modelPath);
            knownFaces = System.Text.Json.JsonSerializer.Deserialize<Dictionary<string, List<float[]>>>(File.ReadAllText(facesDbPath));
            this.yolo = yolo;
        }

   

        float CosineSimilarity(float[] a, float[] b)
        {
            float sim = 0;
            for (int i = 0; i < a.Length; i++)
                sim += a[i] * b[i];  // no need for divide. they are both normalized vectors
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


            var imgSharp = ConvertToImageSharp(image);

           //extracting the faces in the photo
            List<FaceDetected> facesDetected = yolo.ApplyAndExtract(image);


            //for both linux and windows
            Font font;
            try
            {
                font = SystemFonts.CreateFont("DejaVu Sans", 18);
            }
            catch
            {
                font = SystemFonts.CreateFont("Arial", 18);
            }

        


            var faceMatches = new List<(FaceDetected Face, string Name)>();

            foreach (var face in facesDetected)
            {
                //extracting the bounding box
                var faceBox = face.Box;
                //extracting a new image from that box 
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

                        // Debug: allowing to see the similarity scores
                        Console.WriteLine($"Comparing with {kv.Key} => similarity = {sim}");

                        if (sim > 0.55f && sim > maxSim)
                        {
                            maxSim = sim;
                            matchedName = kv.Key;
                           
                        }
                    }
                }
                faceMatches.Add((face, matchedName));

                if (matchedName == "Unknown")
                {
                    var box = face.Box;

                    int x = Math.Max(0, box.X);
                    int y = Math.Max(0, box.Y);
                    int w = Math.Min(box.Width, imgSharp.Width - x);
                    int h = Math.Min(box.Height, imgSharp.Height - y);

                    if (w > 0 && h > 0)
                    {
                        var rect = new Rectangle(x, y, w, h);

                        // aplicăm blur + noise pe acea zonă
                        var blurred = IrreversibleGaussian.BlurArea(
                            image,
                            new Point(rect.X, rect.Y),
                            new Point(rect.Right, rect.Bottom),
                            radius: 9,
                            noiseStrength:0.1f
                           
                        );

                        // suprascriem imgSharp cu imaginea procesată
                        imgSharp = ConvertToImageSharp(blurred);
                    }
                }


                Console.WriteLine($"Face at ({faceBox.X},{faceBox.Y}) matched with {matchedName} ({maxSim})");
            }

            // showing the results on the image
            imgSharp.Mutate(ctx =>
            {
                foreach (var (face, name) in faceMatches)
                {
                    var box = face.Box;
                    ctx.Draw(Color.Red, 3, new RectangularPolygon(box.X, box.Y, box.Width, box.Height));
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
