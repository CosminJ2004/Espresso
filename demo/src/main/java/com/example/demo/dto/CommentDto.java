package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    
    private Long id;

    @NotNull(message = "Post ID is required")
    private Long postId;

    private Long parentId;

    @NotBlank(message = "Comment content is required")
    @Size(max = 1000, message = "Comment too long")
    private String content;
    
    @NotBlank(message = "Author is required")
    private String author;

    private int upvotes;

    private int downvotes;

    private int score;

    private int userVote;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<CommentDto> replies = new ArrayList<>();

}