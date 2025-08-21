using Processing.Models;
using SixLabors.ImageSharp;
using SixLabors.ImageSharp.PixelFormats;
using SixLabors.ImageSharp.Processing;

namespace Processing.Utils
{
    public class IrreversibleGaussian
    {

        private static readonly Random rng = new Random();

        /// <summary>
        /// Apply a blur gaussian + noise  ireversible
        /// </summary>
        public static RgbImage BlurArea(RgbImage image, Point leftCorner, Point rightCorner, int radius = 15, float noiseStrength = 0.05f)
        {
            int regionWidth = rightCorner.X - leftCorner.X;
            int regionHeight = rightCorner.Y - leftCorner.Y;

            if (regionWidth <= 0 || regionHeight <= 0) return image;

            // Crop regiunea
            using var sharpImg = new Image<Rgb24>(image.Width, image.Height);
            for (int y = 0; y < image.Height; y++)
                for (int x = 0; x < image.Width; x++)
                    sharpImg[x, y] = new Rgb24(image.Pixels[y, x].R, image.Pixels[y, x].G, image.Pixels[y, x].B);

            var rect = new Rectangle(leftCorner.X, leftCorner.Y, regionWidth, regionHeight);
            
            if (radius < 1) radius = 1;

            // Aplicăm Gaussian blur
            sharpImg.Mutate(ctx =>
            {
                ctx.GaussianBlur(radius, rect);
            });

            // Adăugăm noise în regiune pentru ireversibilitate
            sharpImg.ProcessPixelRows(accessor =>
            {
                for (int y = rect.Top; y < rect.Bottom; y++)
                {
                    var row = accessor.GetRowSpan(y);
                    for (int x = rect.Left; x < rect.Right; x++)
                    {
                        ref Rgb24 px = ref row[x];
                        px.R = ClampByte(px.R + (int)((rng.NextDouble() - 0.5) * 255 * noiseStrength));
                        px.G = ClampByte(px.G + (int)((rng.NextDouble() - 0.5) * 255 * noiseStrength));
                        px.B = ClampByte(px.B + (int)((rng.NextDouble() - 0.5) * 255 * noiseStrength));
                    }
                }
            });

            // Copiem rezultatul înapoi în RgbImage
            sharpImg.ProcessPixelRows(accessor =>
            {
                for (int y = 0; y < image.Height; y++)
                    for (int x = 0; x < image.Width; x++)
                    {
                        var px = accessor.GetRowSpan(y)[x];
                        image.Pixels[y, x] = new Processing.Models.RgbPixel(px.R, px.G, px.B);
                    }
            });
            return image;
        }

        private static byte ClampByte(int value) => (byte)Math.Clamp(value, 0, 255);
        public static Rectangle ClampRectangle(BoundingBox rect, int imageWidth, int imageHeight)
        {
            int x = Math.Max(0, rect.X);
            int y = Math.Max(0, rect.Y);
            int w = Math.Min(rect.Width, imageWidth - x);
            int h = Math.Min(rect.Height, imageHeight - y);

            if (w <= 0 || h <= 0)
                return Rectangle.Empty; // nimic valid

            return new Rectangle(x, y, w, h);
        }



    }
}
