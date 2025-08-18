package com.example.backend.exception;

public class InvalidVoteException extends RuntimeException {
    public InvalidVoteException(String message) {
        super(message);
    }
    
    public InvalidVoteException() {
        super("Invalid vote operation");
    }
}