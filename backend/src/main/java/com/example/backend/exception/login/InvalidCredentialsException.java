package com.example.backend.exception.login;

public class InvalidCredentialsException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Invalid username or password!";
    public InvalidCredentialsException() {
        super(DEFAULT_MESSAGE);
    }
}
