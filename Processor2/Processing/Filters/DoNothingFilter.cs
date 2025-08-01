using Processing.Interfaces;
using Processing.Models;

namespace Processing.Filters
{
    public class DoNothingFilter : IImageFilter
    {
        public string Name => "nothing";

        public RgbImage Apply(RgbImage image)
        {
            return image;
        }
    }
}
