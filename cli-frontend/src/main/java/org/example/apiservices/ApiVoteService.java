package org.example.apiservices;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.*;
import java.net.URI;
import java.util.Scanner;

public class ApiVoteService implements IApiService {
    private static final String BASE_URL = "http://3.65.147.49/votes";

    public JsonArray handleGet(HttpClient client) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject responseObject = JsonParser.parseString(response.body()).getAsJsonObject();
        return responseObject.getAsJsonArray("data");
        //System.out.println("Voturi:\n" + response.body());
    }

    public void handlePost(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("ID postare: ");
        Long postId = Long.parseLong(scanner.nextLine());
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Tip vot (UPVOTE/DOWNVOTE): ");
        String voteType = scanner.nextLine();

        String json = String.format("""
        {
            "type": "%s",
            "username": "%s",
            "post_id": %d
        }
        """, voteType, username, postId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Vot trimis:\n" + response.body());
    }

    public void handlePut(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("ID vot de modificat: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Tip vot nou (UPVOTE/DOWNVOTE): ");
        String voteType = scanner.nextLine();
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("ID postare: ");
        Long postId = Long.parseLong(scanner.nextLine());

        String json = String.format("""
    {
        "type": "%s",
        "username": "%s",
        "post_id": %d
    }
    """, voteType, username, postId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Vot actualizat:\n" + response.body());
    }

    public void handleDelete(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("ID vot de șters: ");
        Long id = Long.parseLong(scanner.nextLine());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Răspuns server:\n" + response.body());
    }


}
