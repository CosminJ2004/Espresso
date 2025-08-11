package org.example.apiservices;

import com.google.gson.*;
import org.example.models.User;
import org.example.utils.ApiResult;

import java.io.IOException;
import java.net.http.*;
import java.net.URI;
import java.util.Scanner;

public class ApiUserService {
    private static final String BASE_URL = "http://3.65.147.49/users";
    private final HttpClient client;
    private static ApiUserService instance;

    private ApiUserService(HttpClient client) {
        this.client = client;
    }

    public static ApiUserService getInstance(HttpClient client) {
        if (instance == null) {
            instance = new ApiUserService(client);
        }
        return instance;
    }

    public ApiResult<User> handleLogin(String json, Gson gson) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        final HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            return ApiResult.err("A aparut o eroare, reveniti mai tarziu!", null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ApiResult.err("Cererea a fost intrerupta, reveniti mai tarziu!", null);
        }

        int statusCode = response.statusCode();
        JsonObject responseBody;
        try {
            responseBody = JsonParser.parseString(response.body()).getAsJsonObject();
        } catch (Exception e) {
            return ApiResult.err("A aparut o eroare. Reveniti mai tarziu!", statusCode);
        }

        boolean success = responseBody.has("success") && responseBody.get("success").getAsBoolean();
        String message = responseBody.has("error") ? responseBody.get("error").getAsString() : null;

        if (!success) {
            return ApiResult.err(message != null ? message : "Nu te-ai putut conecta!", statusCode);
        }

        JsonElement data = responseBody.get("data");
        User user = gson.fromJson(data, User.class);

        return ApiResult.ok(user);
    }

    public ApiResult<User> handleRegister(String json, Gson gson) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        final HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            return ApiResult.err("A aparut o eroare, reveniti mai tarziu!", null);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return ApiResult.err("Cererea a fost intrerupta, reveniti mai tarziu!", null);
        }

        int statusCode = response.statusCode();
        JsonObject responseBody;
        try {
            responseBody = JsonParser.parseString(response.body()).getAsJsonObject();
        } catch (Exception e) {
            return ApiResult.err("A aparut o eroare. Reveniti mai tarziu!", statusCode);
        }

        boolean success = responseBody.has("success") && responseBody.get("success").getAsBoolean();
        String message = responseBody.has("error") ? responseBody.get("error").getAsString() : null;

        if (!success) {
            return ApiResult.err(message != null ? message : "Nu ti-ai putea crea un cont nou!", statusCode);
        }

        JsonElement data = responseBody.get("data");
        User user = gson.fromJson(data, User.class);

        return ApiResult.ok(user);
    }

    public JsonArray handleGet(HttpClient client) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();
        return responseObject.getAsJsonArray("data");
        //System.out.println("Utilizatori:\n" + response.body());
    }

    public JsonObject handlePost(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Parolă: ");
        String password = scanner.nextLine();

        String json = String.format("""
                {
                    "username": "%s",
                    "password": "%s"
                }
                """, username, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Utilizator creat:\n" + response.body());
        return null;
    }

    public JsonObject handlePut(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("ID utilizator: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.print("Username nou: ");
        String username = scanner.nextLine();
        System.out.print("Parolă nouă: ");
        String password = scanner.nextLine();

        String json = String.format("""
                {
                    "id": %d,
                    "username": "%s",
                    "password": "%s"
                }
                """, id, username, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Utilizator actualizat:\n" + response.body());
        return null;
    }

    public void handleDelete(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("ID utilizator de șters: ");
        Long id = Long.parseLong(scanner.nextLine());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Răspuns server:\n" + response.body());
    }

}
