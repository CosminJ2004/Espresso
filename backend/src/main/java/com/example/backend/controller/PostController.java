package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.PostService;
import com.example.backend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @GetMapping
    public ResponseEntity<Response<List<PostResponseDto>>> getAllPosts() {
            return Response.ok(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<PostResponseDto>> getPostById(@PathVariable Long id) {
        return Response.ok(postService.getPostById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<PostResponseDto>> createPostWithoutImage(@RequestBody PostRequestDto postRequest) {
        return Response.ok(postService.createPostWithoutImage(postRequest));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<PostResponseDto>> createPostWithImage(@ModelAttribute PostRequestDto postRequest) {
        return Response.ok(postService.createPostWithImage(postRequest));
    }

    @PutMapping(value = "/{id}", consumes =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<PostResponseDto>> updatePostWithoutImage(@PathVariable Long id, @RequestBody PostRequestDto postRequest) {
        return Response.ok(postService.updatePostWithoutImage(id, postRequest));
    }

    @PutMapping(value = "/{id}", consumes =  MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<PostResponseDto>> updatePostWithImage(@PathVariable Long id, @ModelAttribute PostRequestDto postRequest) {
        return Response.ok(postService.updatePostWithImage(id, postRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return Response.ok("Post deleted successfully");
    }

    @PutMapping("/{id}/vote")
    public ResponseEntity<Response<VoteResponseDto>> votePost(@PathVariable Long id, @RequestBody VoteRequestDto voteRequest) {
        return Response.ok(postService.votePost(id, voteRequest));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Response<List<CommentResponseDto>>> getCommentsByPostId(@PathVariable Long id) {
        return Response.ok(postService.getCommentsByPostId(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Response<CommentResponseDto>> addComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequest) {
        return Response.ok(postService.addComment(id, commentRequest));
    }
}



