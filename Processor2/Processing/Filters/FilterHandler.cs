using Processing.Models;
using Processing.Interfaces;
using System.Diagnostics;
namespace Processing.Filters
{
  
           public class FilterHandler
        {
            private readonly Dictionary<string, IImageFilter> _filters;
            private readonly ILogger<FilterHandler> _logger;

            public FilterHandler(IEnumerable<IImageFilter> filters, ILogger<FilterHandler> logger)
            {
                _logger = logger;
                _filters = filters
                    .ToDictionary(
                        f => NormalizeName(f.GetType().Name),
                        f => f,
                        StringComparer.OrdinalIgnoreCase
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

                var filter = _filters[filterName];
                var sw = Stopwatch.StartNew();

                var output = filter.Apply(image);

                sw.Stop();
                _logger.LogInformation("Filter {FilterName} took {ElapsedMilliseconds} ms", filterName, sw.ElapsedMilliseconds);

                return output;
            }
        }
   }


