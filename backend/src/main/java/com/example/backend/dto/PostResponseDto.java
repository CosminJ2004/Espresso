package com.example.backend.dto;

import com.example.backend.model.VoteType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private Long filter;
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
