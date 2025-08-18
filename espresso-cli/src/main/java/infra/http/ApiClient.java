package infra.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import objects.dto.PostRequestWithImageDto;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.EntityBuilder;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Base64;
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

    public <T> ApiResult<T> postMultipart(String url, PostRequestWithImageDto dto, TypeReference<ApiResult<T>> type) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) { //momentan client nou sa nu pice cel actual
            HttpPost post = new HttpPost(url);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("title", dto.title(), ContentType.TEXT_PLAIN);
            builder.addTextBody("content", dto.content(), ContentType.TEXT_PLAIN);
            builder.addTextBody("author", dto.author(), ContentType.TEXT_PLAIN);
            builder.addTextBody("subreddit", dto.subreddit(), ContentType.TEXT_PLAIN);
            if (dto.filter() != null) {
                builder.addTextBody("filter", dto.filter().toString(), ContentType.TEXT_PLAIN);
            }

            if (dto.imagePath() != null) {
                File imageFile = dto.imagePath().toFile();
                builder.addBinaryBody("image", imageFile, ContentType.IMAGE_JPEG, imageFile.getName());
            }

            post.setEntity(builder.build());

            return httpClient.execute(post, response -> {
                try (InputStream is = response.getEntity().getContent()) {
                    return objectMapper.readValue(is, type);
                }
            });

        } catch (Exception e) {
            return ApiResult.error("Multipart request failed: " + e.getMessage(), 500);
        }
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
            //System.out.println("Response body: " + response.body());  // pentru debug, sa vad raspunsul in caz de ceva
            return objectMapper.readValue(response.body(), type);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            //e.printStackTrace();  // //debug
            return ApiResult.error("Could not process the request! Please try again later!", 500);
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
