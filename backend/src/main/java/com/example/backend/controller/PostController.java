package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.service.PostService;
import com.example.backend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @PostMapping
    public ResponseEntity<Response<PostResponseDto>> createPost(@RequestBody PostRequestDto postRequest) {
        return Response.ok(postService.createPost(postRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<PostResponseDto>> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequest) {
        return Response.ok(postService.updatePost(id, postRequest));
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


    @PostMapping(value = "/with-image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Response<PostResponseDto>> createPostWithImage(PostRequestImageDto postRequestDto) throws IOException
    {
        return Response.ok(postService.createPostWithImage(postRequestDto));
    }

    @GetMapping(value = "/get-grayscale-filter")
    public ResponseEntity<Response<PostResponseDto>> getGrayscaleFilter(PostRequestImageDto postRequestDto)throws  IOException {
        return Response.ok(postService.getPostWithGrayscale(postRequestDto));
    }
}



