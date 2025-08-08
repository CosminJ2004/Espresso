package com.example.backend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;

    private String username;

    private LocalDateTime createdAt;

    private int postCount;

    private int commentCount;

    public UserResponseDto(Long id, String username, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.createdAt = createdAt;
    }
}