using Microsoft.ML.OnnxRuntime;
using Microsoft.ML.OnnxRuntime.Tensors;
using OpenCvSharp;
using Processing.Models;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Processing.Utils
{
    public class YoloDetector
    {
        private InferenceSession _session;
        private readonly string[] _labels;
        private readonly int _inputWidth = 320;
        private readonly int _inputHeight = 320;
        private readonly float _confThreshold = 0.4f;
        private readonly float _nmsThreshold = 0.45f;

        public YoloDetector(string modelPath, string[] labels)
        {
            _session = new InferenceSession(modelPath);
            _labels = labels;
        }

        public List<Detection> Detect(Mat image)
        {
            var inputTensor = Preprocess(image);
            var inputs = new List<NamedOnnxValue>
            {
                NamedOnnxValue.CreateFromTensor("images", inputTensor)
            };

            using var results = _session.Run(inputs);
            var output = results.First().AsEnumerable<float>().ToArray();

            return Postprocess(output, image.Width, image.Height);
        }

        private DenseTensor<float> Preprocess(Mat image)
        {
            Mat resized = new Mat();
            Cv2.Resize(image, resized, new Size(_inputWidth, _inputHeight));
            Cv2.CvtColor(resized, resized, ColorConversionCodes.BGR2RGB);
            resized.ConvertTo(resized, MatType.CV_32FC3, 1.0 / 255);

            var chw = new float[3 * _inputWidth * _inputHeight];
            int index = 0;
            for (int c = 0; c < 3; c++)
            {
                for (int y = 0; y < _inputHeight; y++)
                {
                    for (int x = 0; x < _inputWidth; x++)
                    {
                        chw[index++] = resized.At<Vec3f>(y, x)[c];
                    }
                }
            }

            return new DenseTensor<float>(chw, new[] { 1, 3, _inputHeight, _inputWidth });
        }

        private List<Detection> Postprocess(float[] output, int originalWidth, int originalHeight)
        {
            var detections = new List<Detection>();

            // 6 valori pe box: x, y, w, h, obj_conf, class_prob (pentru o singură clasă)
            int dimensions = 6;
            int rows = output.Length / dimensions;

            for (int i = 0; i < rows; i++)
            {
                float objConf = output[i * dimensions + 4];
                float classProb = output[i * dimensions + 5];
                float confidence = objConf * classProb;

                if (confidence < _confThreshold)
                    continue;

                float cx = output[i * dimensions + 0];
                float cy = output[i * dimensions + 1];
                float w = output[i * dimensions + 2];
                float h = output[i * dimensions + 3];

                float x = cx - w / 2;
                float y = cy - h / 2;

                // Scale la dimensiunile originale
                x = x / _inputWidth * originalWidth;
                y = y / _inputHeight * originalHeight;
                w = w / _inputWidth * originalWidth;
                h = h / _inputHeight * originalHeight;

                detections.Add(new Detection
                {
                    Label = _labels[0], // o singură clasă, index 0
                    Box = new Rect((int)x, (int)y, (int)w, (int)h),
                    Confidence = confidence
                });
            }

            return ApplyNms(detections);
        }

        private List<Detection> ApplyNms(List<Detection> detections)
        {
            var result = new List<Detection>();

            foreach (var group in detections.GroupBy(d => d.Label))
            {
                var boxes = group.OrderByDescending(d => d.Confidence).ToList();

                while (boxes.Count > 0)
                {
                    var best = boxes[0];
                    result.Add(best);
                    boxes.RemoveAt(0);

                    boxes = boxes.Where(b => IoU(best.Box, b.Box) < _nmsThreshold).ToList();
                }
            }
            return result;
        }

        private float IoU(Rect a, Rect b)
        {
            int interX = Math.Max(a.X, b.X);
            int interY = Math.Max(a.Y, b.Y);
            int interW = Math.Min(a.X + a.Width, b.X + b.Width) - interX;
            int interH = Math.Min(a.Y + a.Height, b.Y + b.Height) - interY;

            if (interW <= 0 || interH <= 0) return 0;

            float interArea = interW * interH;
            float unionArea = a.Width * a.Height + b.Width * b.Height - interArea;

            return interArea / unionArea;
        }
    }
}
