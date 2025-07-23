package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    
    private Long id;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30, message = "Username must be 3-30 characters")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    
    private LocalDateTime createdAt;
    
    private int postCount;
    
    private int commentCount;
    
    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserDto(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public UserDto(Long id, String username, LocalDateTime createdAt, int postCount, int commentCount) {
        this.id = id;
        this.username = username;
        this.createdAt = createdAt;
        this.postCount = postCount;
        this.commentCount = commentCount;
    }
}