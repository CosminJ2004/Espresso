package infra.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;

public final class ApiClient {
    private final HttpClient httpClient;
    private final URI baseUri;
    private final ObjectMapper objectMapper;
    //client generic
    public ApiClient(String baseUrl, ObjectMapper objectMapper) {
        this.baseUri = URI.create(Objects.requireNonNull(baseUrl, "baseUrl"));
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper");
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    //metode generice, orice entitate are endpointul simplu de GET la base url
    public <T> ApiResult<T> get(String path, TypeReference<ApiResult<T>> type)
            throws IOException, InterruptedException {

        URI fullUri = baseUri.resolve("");

        HttpRequest request = HttpRequest.newBuilder(fullUri)
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), type);
    }
}
