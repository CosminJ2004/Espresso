package org.example.services;

import org.example.mappers.PostMapper;
import org.example.menu.view.PostView;
import java.net.http.*;
import java.net.URI;
import java.util.Scanner;

public class PostService {
    private static final String BASE_URL = "http://3.65.147.49/posts";
    private static PostMapper postMapper = PostMapper.getInstance();
    private static PostView postView = PostView.getInstance();

    public void handleGet(HttpClient client) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        /*
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter()).create();
        String json = response.body();
        ApiPostResponse apiPostResponse = gson.fromJson(json, ApiPostResponse.class);
        Subreddit myDemoSub = new Subreddit();
        ArrayList<Post> posts = new ArrayList<Post>();
        for(PostDTO postDTO : apiPostResponse.getData()) {
            posts.add(postMapper.mapDTOToPost(postDTO));
        }
        myDemoSub.setPosts(posts);
        postView.printAllPosts(myDemoSub);
        */
        System.out.println("Postări:\n" + response.body());
    }

    public void handlePost(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("Titlu: ");
        String title = scanner.nextLine();
        System.out.print("Conținut: ");
        String content = scanner.nextLine();
        System.out.print("Username autor: ");
        String author = scanner.nextLine();

        String json = String.format("""
        {
            "author": "%s",
            "title": "%s",
            "content": "%s"
            
        }
        """, author,title, content);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Postare creată:\n" + response.body());
    }

    public void handlePut(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("ID postare: ");
        String id = scanner.nextLine();
        System.out.print("author: ");
        String author = scanner.nextLine();
        System.out.print("Summary nou: ");
        String summary = scanner.nextLine();
        System.out.print("Conținut nou: ");
        String content = scanner.nextLine();

        String json = String.format("""
        {
            "author": "%s",
            "title": "%s",
            "content": "%s"
        }
        """, author,summary, content);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Postare actualizată:\n" + response.body());
    }

    public void handleDelete(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("ID postare de șters: ");
        String id = scanner.nextLine();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Postare ștearsă:\n" + response.body());
    }
}
