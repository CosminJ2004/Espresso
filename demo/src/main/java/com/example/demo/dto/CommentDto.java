package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    
    private Long id;
    
    @NotBlank(message = "Comment text is required")
    @Size(max = 1000, message = "Comment too long")
    private String text;
    
    @NotBlank(message = "Author username is required")
    private String authorUsername;
    
    @NotNull(message = "Post ID is required")
    private Long postId;
    
    private Long parentId;
    
    private LocalDateTime createdAt;
    
    private int replyCount;
    
    private List<CommentDto> replies;
    
    public CommentDto(String text, String authorUsername, Long postId, Long parentId) {
        this.text = text;
        this.authorUsername = authorUsername;
        this.postId = postId;
        this.parentId = parentId;
    }
}