package com.example.backend.dto;

import com.example.backend.model.VoteType;
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

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title too long")
    private String title;

    private String content;

    @NotBlank(message = "Author is required")
    private String author;

    private String subreddit;

    private Long upvotes;

    private Long downvotes;

    private Long score;

    private Long commentCount;

    private VoteType userVote;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
