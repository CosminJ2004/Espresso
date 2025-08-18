package com.example.backend.exception;

public class UnauthorizedOperationException extends RuntimeException {
    public UnauthorizedOperationException(String message) {
        super(message);
    }
    
    public UnauthorizedOperationException() {
        super("You are not authorized to perform this operation");
    }
}