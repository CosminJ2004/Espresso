package com.example.backend.exception;

public class ImageRequiredException extends RuntimeException {
    public ImageRequiredException(String message) {
        super(message);
    }
    
    public ImageRequiredException() {
        super("Image is required for this operation");
    }
}