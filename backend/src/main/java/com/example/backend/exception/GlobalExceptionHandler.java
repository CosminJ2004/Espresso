package com.example.backend.exception;

import com.example.backend.util.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        return Response.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleGenericException(Exception e) {
        return Response.error("An unexpected error occurred: " + e.getMessage());
    }
}