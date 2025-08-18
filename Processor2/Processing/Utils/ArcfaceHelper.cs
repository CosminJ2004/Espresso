using System;
using System.Linq;
using System.Collections.Generic;
using System.Drawing;
using Microsoft.ML.OnnxRuntime;
using Microsoft.ML.OnnxRuntime.Tensors;

namespace Processing.Utils
{
    public class ArcFaceHelper
    {
        private InferenceSession session;

        public ArcFaceHelper(string modelPath)
        {
            session = new InferenceSession(modelPath);
        }

        // Preprocess image: resize 112x112, normalize [-1,1]
        private Tensor<float> Preprocess(string imagePath)
        {
            using Bitmap original = new Bitmap(imagePath);
            using Bitmap resized = new Bitmap(112, 112);

            using (Graphics g = Graphics.FromImage(resized))
            {
                g.InterpolationMode = System.Drawing.Drawing2D.InterpolationMode.Bilinear;
                g.DrawImage(original, 0, 0, 112, 112);
            }

            var input = new DenseTensor<float>(new[] { 1, 112, 112, 3 }); // NHWC

            for (int y = 0; y < 112; y++)
            {
                for (int x = 0; x < 112; x++)
                {
                    Color pixel = resized.GetPixel(x, y);
                    // Normalize RGB to [-1, 1]
                    input[0, y, x, 0] = pixel.R / 127.5f - 1.0f;
                    input[0, y, x, 1] = pixel.G / 127.5f - 1.0f;
                    input[0, y, x, 2] = pixel.B / 127.5f - 1.0f;
                }
            }

            return input;
        }

        // Get embedding
        public float[] GetEmbedding(string imagePath)
        {
            var inputTensor = Preprocess(imagePath);
            var inputs = new List<NamedOnnxValue> { NamedOnnxValue.CreateFromTensor("input_1", inputTensor) };

            using var results = session.Run(inputs);
            var embedding = results.First().AsEnumerable<float>().ToArray();
            return embedding;
        }

        // Cosine similarity
        public static float CosineSimilarity(float[] a, float[] b)
        {
            float dot = 0f, normA = 0f, normB = 0f;
            for (int i = 0; i < a.Length; i++)
            {
                dot += a[i] * b[i];
                normA += a[i] * a[i];
                normB += b[i] * b[i];
            }
            return dot / (MathF.Sqrt(normA) * MathF.Sqrt(normB));
        }
    }
}
