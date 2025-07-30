package com.example.demo.controller;

import com.example.demo.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    //    GET /comments/:id TODO
    @GetMapping("/{id}")

    //    PUT /comments/:id TODO
    @PutMapping("/{id}")

    //    DELETE /comments/:id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    //    PUT /comments/:id/vote TODO
}
