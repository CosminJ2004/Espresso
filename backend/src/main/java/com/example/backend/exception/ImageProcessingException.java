package com.example.backend.exception;

public class ImageProcessingException extends RuntimeException {
    public ImageProcessingException(String message) {
        super(message);
    }
    
    public ImageProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ImageProcessingException() {
        super("Failed to process image");
    }
}