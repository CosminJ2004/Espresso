package org.example.apiservices;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.http.*;
import java.net.URI;

public class ApiPostService {
    private static final String BASE_URL = "http://3.65.147.49/posts";
    private final HttpClient client;
    private static ApiPostService instance;

    private ApiPostService(HttpClient client) {
        this.client = client;
    }

    public static ApiPostService getInstance(HttpClient client) {
        if (instance == null) {
            instance = new ApiPostService(client);
        }
        return instance;
    }

    public JsonArray handleGet() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();
        return responseObject.getAsJsonArray("data");
    }

    public JsonObject handlePost(String json) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();
        return responseObject.getAsJsonObject("data");
    }

    public JsonObject handlePut(String json, long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();
        return responseObject.getAsJsonObject("data");
    }

    public void handleDelete(long id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
