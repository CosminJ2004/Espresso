package com.example.backend.controller;

import com.example.backend.dto.CommentRequestDto;
import com.example.backend.dto.CommentResponseDto;
import com.example.backend.dto.VoteRequestDto;
import com.example.backend.dto.VoteResponseDto;
import com.example.backend.service.CommentService;
import com.example.backend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<CommentResponseDto>> getCommentById(@PathVariable String id) {
        return Response.ok(commentService.getCommentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<CommentResponseDto>> updateComment(@PathVariable String id, @RequestBody CommentRequestDto commentRequest) {
        return Response.ok(commentService.updateComment(id, commentRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
        return Response.ok("Comment deleted successfully");
    }

    @PutMapping("/{id}/vote")
    public ResponseEntity<Response<VoteResponseDto>> voteComment(@PathVariable String id, @RequestBody VoteRequestDto voteRequest) {
        return Response.ok(commentService.voteComment(id, voteRequest));
    }
}
