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

    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //    GET /comments/:id
    @GetMapping("/{id}")
    public ResponseEntity<Response<CommentResponseDto>> getCommentById(@PathVariable Long id) {
        CommentResponseDto commentResponse = commentService.getCommentById(id);
        return Response.ok(commentResponse);
    }

    //    PUT /comments/:id
    @PutMapping("/{id}")
    public ResponseEntity<Response<CommentResponseDto>> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequest) {
        CommentResponseDto commentResponse = commentService.updateComment(id, commentRequest);
        return Response.ok(commentResponse);
    }

    //    DELETE /comments/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return Response.ok("Comment deleted successfully");
    }

    //    PUT /comments/:id/vote
    @PutMapping("/{id}/vote")
    public ResponseEntity<Response<VoteResponseDto>> voteComment(@PathVariable Long id, @RequestBody VoteRequestDto voteRequest) {
        VoteResponseDto voteResponse = commentService.voteComment(id, voteRequest);
        return Response.ok(voteResponse);
    }
}
