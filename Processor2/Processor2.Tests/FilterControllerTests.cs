using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using FluentAssertions;
using Microsoft.AspNetCore.Mvc.Testing;
using Xunit;

namespace Processor2.Tests
{
    public class FilterControllerTests : IClassFixture<WebApplicationFactory<Program>>
    {
        private readonly HttpClient _client;

        public FilterControllerTests(WebApplicationFactory<Program> factory)
        {
            _client = factory.CreateClient();
        }

        [Fact]
        public async Task PostFilter_MissingImage_ReturnsBadRequest()
        {
            // Arrange
            var content = new MultipartFormDataContent();
            var request = new HttpRequestMessage(HttpMethod.Post, "/filter?filter=grayscale")
            {
                Content = content
            };

            // Act
            var response = await _client.SendAsync(request);

            // Assert
            response.StatusCode.Should().Be(HttpStatusCode.BadRequest);
        }

        [Fact]
        public async Task PostFilter_WithValidImage_ReturnsOk()
        {
            // Arrange
            var content = new MultipartFormDataContent();

            // Simulăm o imagine mică dummy (de exemplu un PNG gol sau un simplu byte array)
            var imageBytes = new byte[] { 137, 80, 78, 71, 13, 10, 26, 10 }; // header PNG (doar exemplu)
            var imageContent = new ByteArrayContent(imageBytes);
            imageContent.Headers.ContentType = new System.Net.Http.Headers.MediaTypeHeaderValue("image/png");

            content.Add(imageContent, "image", "test.png");

            var request = new HttpRequestMessage(HttpMethod.Post, "/filter?filter=grayscale")
            {
                Content = content
            };

            // Act
            var response = await _client.SendAsync(request);

            // Assert
            response.StatusCode.Should().Be(HttpStatusCode.OK);

            // Opțional: verifică dacă răspunsul conține un conținut de tip imagine
            response.Content.Headers.ContentType.MediaType.Should().StartWith("image/");

            var responseBytes = await response.Content.ReadAsByteArrayAsync();

            // Verifică că răspunsul conține ceva (nu e gol)
            responseBytes.Length.Should().BeGreaterThan(0);
        }

    }
}
