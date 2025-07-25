package com.example.demo.controller;

import com.example.demo.dto.CommentDto;
import com.example.demo.dto.PostDto;
import com.example.demo.model.Post;
import com.example.demo.service.PostService;
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
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(postService.getPostById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public PostDto createPost(@RequestBody PostDto dto) {
        return postService.addPost(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable int id, @RequestBody PostDto dto) {
        try {
            Post updatedPost = postService.updatePost(id, dto);
            return ResponseEntity.ok(updatedPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable int id) {
        boolean deleted = postService.deletePost(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/vote")
    // TODO

    @PostMapping("/upload-test")
    public ResponseEntity<String> uploadTest(@RequestParam("file") MultipartFile file) {
        if (file == null) return ResponseEntity.badRequest().body("File is null");
        return ResponseEntity.ok("File received: " + file.getOriginalFilename());
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
