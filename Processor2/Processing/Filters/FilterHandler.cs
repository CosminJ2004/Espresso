using Processing.Models;
using Processing.Interfaces;
namespace Processing.Filters
{
    public class FilterHandler
    {
        private readonly IEnumerable<IImageFilter> _filters;

        public FilterHandler(IEnumerable<IImageFilter> filters)
        {
            _filters = filters;
        }

        public RgbImage Apply(string filterName, RgbImage image)
        {
            var filter = _filters.FirstOrDefault(f => f.Name.Equals(filterName, StringComparison.OrdinalIgnoreCase));
            if (filter == null)
                throw new InvalidOperationException($"Filter '{filterName}' does not exist.");

            return filter.Apply(image);

        }


        public RgbImage ApplyMultiple(IEnumerable<string> filterNames, RgbImage image)
        {
            var result = image;
            foreach (var name in filterNames)
            {
                result = Apply(name, result);
            }
            return result;
        }

    }

}
