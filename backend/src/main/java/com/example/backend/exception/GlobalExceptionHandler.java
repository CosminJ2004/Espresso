package com.example.backend.exception;

import com.example.backend.exception.login.InvalidCredentialsException;
import com.example.backend.exception.user.UserNotFoundException;
import com.example.backend.util.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Object>> handleIllegalArgumentException(IllegalArgumentException e) {
        return Response.error(400, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleGenericException(Exception e) {
        return Response.error(500, "An unexpected error occurred: " + e.getMessage());
    }
    //eroare de date de conectare invalide la login
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Response<Object>> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return Response.error(401, e.getMessage());
    }
    //eroare pentru user not found de exemplu daca cautam dupa username
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response<Object>> handleUserNotFoundException(UserNotFoundException e) {
        return Response.error(404, e.getMessage());
    }
}