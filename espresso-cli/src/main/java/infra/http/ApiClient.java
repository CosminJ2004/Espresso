package infra.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;
//TO DO:: error handling mai bun, revin
public final class ApiClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final Duration timeout;

    //client generic
    public ApiClient(ObjectMapper objectMapper, Duration timeout) {
        this.objectMapper = objectMapper;
        this.timeout = timeout;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(timeout) // connect timeout din Builder din HttpClient
                .build();
    }

    //metode generice, orice entitate are endpointul simplu de GET la base url
    public <T> ApiResult<T> get(String url, TypeReference<ApiResult<T>> type) {
        return executeRequest(HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .GET()
                .build(), type);
    }

    public <T> ApiResult<T> post(String url, Object body, TypeReference<ApiResult<T>> type) {
        return executeRequest(HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .POST(HttpRequest.BodyPublishers.ofString(serialize(body)))
                .header("Content-Type", "application/json")
                .build(), type);
    }

    public <T> ApiResult<T> put(String url, Object body, TypeReference<ApiResult<T>> type) {
        return executeRequest(HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .PUT(HttpRequest.BodyPublishers.ofString(serialize(body)))
                .header("Content-Type", "application/json")
                .build(), type);
    }

    public <T> ApiResult<T> delete(String url, TypeReference<ApiResult<T>> type) {
        return executeRequest(HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .DELETE()
                .build(), type);
    }

    private <T> ApiResult<T> executeRequest(HttpRequest request, TypeReference<ApiResult<T>> type) {
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return objectMapper.readValue(response.body(), type);
        } catch (Exception e) { // JsonProcessing sau JsonMapping
            return ApiResult.error("Could not process the request!", 500);
        }
    }

    private String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize request body!");//TO DO: better error handling
        }
    }
}
