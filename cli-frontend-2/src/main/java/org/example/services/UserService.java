package org.example.services;

import java.net.http.*;
import java.net.URI;
import java.util.Scanner;

public class UserService {
    private static final String BASE_URL = "http://localhost:8080/users";

    public void register(Scanner scanner, HttpClient client) throws Exception {
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
    }
}
