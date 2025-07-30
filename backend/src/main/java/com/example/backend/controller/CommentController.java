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

    @Autowired
    private CommentService commentService;

    //    GET /comments/:id
    @GetMapping("/{id}")
    public ResponseEntity<Response<CommentResponseDto>> getCommentById(@PathVariable Long id) {
        try {
            CommentResponseDto commentResponse = commentService.getCommentById(id);
            return Response.ok(commentResponse);
        } catch (IllegalArgumentException e) {
            return Response.error("Failed to get comment: " + e.getMessage());
        }
    }

    //    PUT /comments/:id
    @PutMapping("/{id}")
    public ResponseEntity<Response<CommentResponseDto>> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequest) {
        try {
            CommentResponseDto commentResponse = commentService.updateComment(id, commentRequest);
            return Response.ok(commentResponse);
        } catch (IllegalArgumentException e) {
            return Response.error("Failed to update comment: " + e.getMessage());
        }
    }

    //    DELETE /comments/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteComment(@PathVariable Long id) {
        try {
            commentService.deleteComment(id);
            return Response.ok("Comment has been deleted successfully");
        } catch (IllegalArgumentException e) {
            return Response.error("Failed to delete comment: " + e.getMessage());
        }
    }

    //    PUT /comments/:id/vote
    @PutMapping("/{id}/vote")
    public ResponseEntity<Response<VoteResponseDto>> voteComment(@PathVariable Long id, @RequestBody VoteRequestDto voteRequest) {
        try {
            VoteResponseDto voteResponse = commentService.voteComment(id, voteRequest);
            return Response.ok(voteResponse);
        } catch (IllegalArgumentException e) {
            return Response.error("Failed to vote comment: " + e.getMessage());
        }
    }
}
