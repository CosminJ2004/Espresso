package com.example.backend.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> {
    private boolean success;
    private T data;
    private String message;
    private String error;

    public static <T> ResponseEntity<Response<T>> ok(T data) {
        Response<T> response = new Response<>();
        response.success = true;
        response.data = data;
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<Response<T>> ok(String message) {
        Response<T> response = new Response<>();
        response.success = true;
        response.message = message;
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<Response<T>> error(int code, String message) {
        Response<T> response = new Response<>();
        response.success = false;
        response.error = message;
        return ResponseEntity.status(code).body(response);
    }
}
