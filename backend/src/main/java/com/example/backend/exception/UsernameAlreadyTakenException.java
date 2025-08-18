package com.example.backend.exception;

public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException(String username) {
        super("Username '" + username + "' is already taken");
    }
    
    public UsernameAlreadyTakenException() {
        super("Username already taken");
    }
}