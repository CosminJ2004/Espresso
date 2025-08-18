using Processing.Models;
using Processing.Interfaces;
namespace Processing.Filters
{
    public class FilterHandler
    {
        private readonly Dictionary<string, IImageFilter> _filters;

        public FilterHandler(IEnumerable<IImageFilter> filters)
        {
            _filters = filters
                .ToDictionary(
                    f => NormalizeName(f.GetType().Name), // cheie fara "Filter"
                    f => f,
                    StringComparer.OrdinalIgnoreCase    // sa nu conteze majusculele
                );
        }

        private string NormalizeName(string className)
        {
            return className.EndsWith("Filter", StringComparison.OrdinalIgnoreCase)
                ? className.Substring(0, className.Length - "Filter".Length)
                : className;
        }

        public RgbImage Apply(string filterName, RgbImage image)
        {
            if (!_filters.ContainsKey(filterName))
                throw new InvalidOperationException($"Unknown filter: {filterName}");

            return _filters[filterName].Apply(image);
        }
    }
}
