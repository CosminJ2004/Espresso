    using Microsoft.AspNetCore.Mvc;
    using Processing.Filters;
    using Processing.Models;
    using Processing.Utils;

    namespace Processor2.Controllers;

    [ApiController]
    [Route("[controller]")]
    public class FilterController : ControllerBase
    {
        private readonly RgbImageReader _reader;
        private readonly RgbImageWriter _writer;
        private readonly FilterHandler _handler;
        private readonly ILogger<FilterController> _logger;

    public FilterController(RgbImageReader reader, RgbImageWriter writer, FilterHandler handler, ILogger<FilterController> logger)
    {
        _reader = reader;
        _writer = writer;
        _handler = handler;
        _logger = logger;
    }
    [HttpPost]
    public async Task<IActionResult> ApplyFilter([FromQuery] List<string> filter)
    {
        if (filter == null || filter.Count == 0)
            return BadRequest("Missing filter names");

        if (!Request.HasFormContentType)
            return BadRequest("Content-Type must be form-data.");

        var form = await Request.ReadFormAsync();
        var file = form.Files["image"];
        if (file == null)
            return BadRequest("Missing image");

        try
        {
            using var stream = file.OpenReadStream();
            using var ms = new MemoryStream();
            await stream.CopyToAsync(ms);
            ms.Position = 0;

            var inputImage = _reader.ReadFromStream(ms);

            // Aplica filtrele pe rând
            var outputImage = inputImage;
            foreach (var filterName in filter)
            {
                outputImage = _handler.Apply(filterName, outputImage);
            }

            var outStream = new MemoryStream();
            _writer.WriteToStream(outStream, outputImage);
            outStream.Position = 0;

            return File(outStream, "image/jpeg");
        }
        catch (InvalidOperationException ex)
        {
            return BadRequest(new { error = ex.Message });
        }
        catch (Exception ex)
        {
            _logger.LogError(ex, "Error applying filters: {Filters}", string.Join(",", filter));
            return Problem(detail: ex.Message);
        }

    }

}
