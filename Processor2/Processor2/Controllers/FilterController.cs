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

    public FilterController(RgbImageReader reader, RgbImageWriter writer, FilterHandler handler)
    {
        _reader = reader;
        _writer = writer;
        _handler = handler;
    }

    [HttpPost]
    public async Task<IActionResult> ApplyFilter([FromQuery] string filter)
    {
        if (string.IsNullOrEmpty(filter))
            return BadRequest("Missing filter name");

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
            var outputImage = _handler.Apply(filter, inputImage);

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
            return Problem(ex.Message + " " + ex.StackTrace);
        }
    }
}
