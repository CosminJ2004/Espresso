package org.example.apiservices;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.*;
import java.net.URI;
import java.util.Scanner;

public class ApiCommentService implements IApiService {
    private static final String BASE_URL = "http://3.65.147.49/comments";

    public JsonArray handleGet(HttpClient client) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();
        return responseObject.getAsJsonArray("data");
        //System.out.println("Comentarii:\n" + response.body());
    }

    public void handlePost(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("Text comentariu: ");
        String content = scanner.nextLine();
        System.out.print("Username autor: ");
        String username = scanner.nextLine();
        System.out.print("ID postare: ");
        Long postId = Long.parseLong(scanner.nextLine());

        String json = String.format("""
        {
            "content": "%s",
            "author": "%s",
            "post_id": %d
        }
        """, content, username, postId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Comentariu adăugat:\n" + response.body());
    }

    public void handlePut(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("ID comentariu de editat: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Text nou: ");
        String content = scanner.nextLine();
        System.out.print("Username autor: ");
        String username = scanner.nextLine();
        System.out.print("ID postare: ");
        Long postId = Long.parseLong(scanner.nextLine());

        String json = String.format("""
    {
        "id": %d,
        "content": "%s",
        "author": "%s",
        "post_id": %d
    }
    """, id, content, username, postId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Comentariu actualizat:\n" + response.body());
    }

    public void handleDelete(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("ID comentariu de șters: ");
        Long id = Long.parseLong(scanner.nextLine());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Răspuns server:\n" + response.body());
    }


}
