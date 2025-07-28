package com.example.demo.controller;

import com.example.demo.dto.PostDto;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;
import com.example.demo.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<Response<List<PostDto>>> getAllPosts() {
        List<PostDto> posts = postService.getAllPosts();
        return Response.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<PostDto>> getPostById(@PathVariable int id) {
        try {
            PostDto post = postService.getPostById(id);
            return Response.ok(post);
        } catch (IllegalArgumentException e) {
            return Response.error("Post not found");
        }
    }

    @PostMapping
    public ResponseEntity<Response<PostDto>> createPost(@RequestBody PostDto dto) {
        try {
            PostDto post = postService.addPost(dto);
            return Response.ok(post);
        } catch (Exception e) {
            return Response.error("Failed to create post");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<Post>> updatePost(@PathVariable int id, @RequestBody PostDto dto) {
        try {
            Post updatedPost = postService.updatePost(id, dto);
            return Response.ok(updatedPost);
        } catch (IllegalArgumentException e) {
            return Response.error("Post not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deletePost(@PathVariable int id) {
        boolean deleted = postService.deletePost(id);
        if (deleted) {
            return Response.ok("Post has been successfully deleted");
        } else {
            return Response.error("Post not found");
        }
    }

    @PutMapping("/{id}/vote")
    // TODO

    @PostMapping("/upload-test")
    public ResponseEntity<Response<String>> uploadTest(@RequestParam("file") MultipartFile file) {
        if (file == null) {
            return Response.error("File is null");
        }
        return Response.ok("File received: " + file.getOriginalFilename());
    }

//    @PostMapping("/create-with-image")
//    public ResponseEntity<Post> createPostWithImage(@ModelAttribute PostDto dto) {
//        MultipartFile file = dto.getFile();
//        if (file == null) {
//            System.out.println("file is null!");
//        } else {
//            System.out.println("file received: " + file.getOriginalFilename());
//        }
//
//        try {
//            String imagePath = postService.saveImage(dto.getFile());
//            Post post = postService.addPostWithImage(dto, imagePath);
//            return ResponseEntity.ok(post);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

}
