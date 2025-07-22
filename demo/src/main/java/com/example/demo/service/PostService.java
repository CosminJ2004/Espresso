package com.example.demo.service;

import com.example.demo.dto.PostDto;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PostService {
    private final List<Post> posts = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);


    // Simulăm o bază de date de useri
    private final List<User> users = List.of(
            new User("john", "123"),
            new User("anna", "abc")
    );

    public List<Post> getAllPosts() {
        User user=new User("john","asgasgas");
        Post postTest=new Post(user,"asnf","asdfasd");
        posts.add(postTest);
        return posts;
    }

    public Post createPost(PostDto dto) {
        // Caută userul în lista noastră mock
        User author = users.stream()
                .filter(u -> u.getUsername().equals(dto.getAuthorUsername()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = new Post(idCounter.getAndIncrement(), author, dto.getSummary(), dto.getContent(), java.time.LocalDateTime.now());
        posts.add(post);
        return post;
    }
}
