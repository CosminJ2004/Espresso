package com.example.backend.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String commentId) {
        super("Comment not found with ID: " + commentId);
    }
}