package com.example.backend.exception;

public class FilterNotFoundException extends RuntimeException {
    public FilterNotFoundException(String filterId) {
        super("Filter not found with ID: " + filterId);
    }
}