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
public class PostDto {
    
    private Long id;
    
    @NotBlank(message = "Author username is required")
    private String authorUsername;
    
    @NotBlank(message = "Summary is required")
    @Size(max = 255, message = "Summary too long")
    private String summary;
    
    private String content;
    
    private String filePath;
    
    private LocalDateTime createdAt;
    
    private int commentCount;
    
    private int voteScore;
    
    public PostDto(String authorUsername, String summary, String content) {
        this.authorUsername = authorUsername;
        this.summary = summary;
        this.content = content;
    }
}
