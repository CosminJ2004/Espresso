package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.PostService;
import com.example.backend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    //    GET /posts
    @GetMapping
    public ResponseEntity<Response<List<PostResponseDto>>> getAllPosts() {
            return Response.ok(postService.getAllPosts());
    }

    //    GET /posts/:id
    @GetMapping("/{id}")
    public ResponseEntity<Response<PostResponseDto>> getPostById(@PathVariable Long id) {
        return Response.ok(postService.getPostById(id));
    }

    //    POST /posts
    @PostMapping
    public ResponseEntity<Response<PostResponseDto>> createPost(@RequestBody PostRequestDto postRequest) {
        return Response.ok(postService.createPost(postRequest));
    }

    //    PUT /posts/:id
    @PutMapping("/{id}")
    public ResponseEntity<Response<PostResponseDto>> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequest) {
        return Response.ok(postService.updatePost(id, postRequest));
    }

    //    DELETE /posts/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return Response.ok("Post deleted successfully");
    }

    //    PUT /posts/:id/vote
    @PutMapping("/{id}/vote")
    public ResponseEntity<Response<VoteResponseDto>> votePost(@PathVariable Long id, @RequestBody VoteRequestDto voteRequest) {
        return Response.ok(postService.votePost(id, voteRequest));
    }

    //    GET /posts/:postId/comments
    @GetMapping("/{id}/comments")
    public ResponseEntity<Response<List<CommentResponseDto>>> getCommentsByPostId(@PathVariable Long id) {
        return Response.ok(postService.getCommentsByPostId(id));
    }

    //    POST /posts/:postId/comments
    @PostMapping("/{id}/comments")
    public ResponseEntity<Response<CommentResponseDto>> addComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequest) {
        return Response.ok(postService.addComment(id, commentRequest));
    }
}


//    @PostMapping("/upload-test")
//    public ResponseEntity<Response<String>> uploadTest(@RequestParam("file") MultipartFile file) {
//        if (file == null) {
//            return Response.error("File is null");
//        }
//        return Response.ok("File received: " + file.getOriginalFilename());
//    }

//    @PostMapping("/upload-test")
//    public ResponseEntity<Response<String>> uploadTest(@RequestParam("file") MultipartFile file) {
//        if (file == null) {
//            return Response.error("File is null");
//        }
//        return Response.ok("File received: " + file.getOriginalFilename());
//    }

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