using Processing.Interfaces;
using Processing.Models;

namespace Processing.Filters
{
    public class GaussianFilter : IImageFilter
    {
        public string Name => "Gaussian";
        public RgbImage Apply(RgbImage image)
        {

            int kernelSize = 9;
            double sigma = 3.0;
            var result = new RgbImage(image.Width, image.Height);
            int halfKernel = kernelSize / 2;
            for (int y = 0; y < image.Height; y++)
            {
                for (int x = 0; x < image.Width; x++)
                {
                    double rSum = 0, gSum = 0, bSum = 0;
                    double weightSum = 0;
                    for (int ky = -halfKernel; ky <= halfKernel; ky++)
                    {
                        for (int kx = -halfKernel; kx <= halfKernel; kx++)
                        {
                            int nx = x + kx;
                            int ny = y + ky;
                            if (nx >= 0 && nx < image.Width && ny >= 0 && ny < image.Height)
                            {
                                double weight = Math.Exp(-(kx * kx + ky * ky) / (2 * sigma * sigma));
                                weight /= (2 * Math.PI * sigma * sigma);
                                var pixel = image.Pixels[ny, nx];
                                rSum += pixel.R * weight;
                                gSum += pixel.G * weight;
                                bSum += pixel.B * weight;
                                weightSum += weight;
                            }
                        }
                    }
                    result.Pixels[y, x] = new RgbPixel(
                        (byte)(rSum / weightSum),
                        (byte)(gSum / weightSum),
                        (byte)(bSum / weightSum));
                }
            }
            return result;
        }
    }
}
