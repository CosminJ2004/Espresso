package com.example.demo.controller;

import com.example.demo.dto.CommentDto;
import com.example.demo.model.Comment;
import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    //comentariu ca sa vedem ca merge deplpoyul pe bune
    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto dto) {
        CommentDto savedComment = commentService.addComment(dto);
        return ResponseEntity.ok(savedComment);
    }

    @GetMapping("/post/{postId}/tree")
    public ResponseEntity<List<CommentDto>> getCommentTree(@PathVariable Long postId) {
        List<CommentDto> commentTree = commentService.getCommentTreeForPost(postId);
        return ResponseEntity.ok(commentTree);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}
