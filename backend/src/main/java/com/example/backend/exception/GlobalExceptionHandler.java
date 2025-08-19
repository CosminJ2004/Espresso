package com.example.backend.exception;

import com.example.backend.util.Response;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    //eroare pt validarile DTO -> toate requesturile dto
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        //Response.error are String ca tip al mesajului, normal putem avea mai multe campuri cu erori, putem face hashmap, dar afisam doar mesajul principal in acest caz
        String firstErrorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Invalid input");

        return Response.error(400, firstErrorMessage);
    }

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

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Response<Object>> handlePostNotFoundException(PostNotFoundException e) {
        return Response.error(404, e.getMessage());
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<Response<Object>> handleCommentNotFoundException(CommentNotFoundException e) {
        return Response.error(404, e.getMessage());
    }

    @ExceptionHandler(FilterNotFoundException.class)
    public ResponseEntity<Response<Object>> handleFilterNotFoundException(FilterNotFoundException e) {
        return Response.error(404, e.getMessage());
    }

    @ExceptionHandler(ImageRequiredException.class)
    public ResponseEntity<Response<Object>> handleImageRequiredException(ImageRequiredException e) {
        return Response.error(400, e.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyTakenException.class)
    public ResponseEntity<Response<Object>> handleUsernameAlreadyTakenException(UsernameAlreadyTakenException e) {
        return Response.error(409, e.getMessage());
    }

    @ExceptionHandler(InvalidVoteException.class)
    public ResponseEntity<Response<Object>> handleInvalidVoteException(InvalidVoteException e) {
        return Response.error(400, e.getMessage());
    }

    @ExceptionHandler(ImageProcessingException.class)
    public ResponseEntity<Response<Object>> handleImageProcessingException(ImageProcessingException e) {
        return Response.error(500, e.getMessage());
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<Response<Object>> handleUnauthorizedOperationException(UnauthorizedOperationException e) {
        return Response.error(403, e.getMessage());
    }
}