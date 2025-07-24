package com.example.demo.controller;

import com.example.demo.dto.CommentDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.model.Comment;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody CommentDto dto) {
        Comment savedComment = commentService.addComment(dto);
        return ResponseEntity.ok(savedComment);
    }
    @GetMapping("/post/{postId}/tree")
    public ResponseEntity<List<CommentResponseDto>> getCommentTree(@PathVariable Long postId) {
        List<CommentResponseDto> commentTree = commentService.getCommentTreeForPost(postId);
        return ResponseEntity.ok(commentTree);
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}
