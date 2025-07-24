package org.example.services;

import java.net.http.*;
import java.net.URI;
import java.util.Scanner;

public class CommentService {
    private static final String BASE_URL = "http://localhost:8080/comments";

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
            "postId": %d
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
}
