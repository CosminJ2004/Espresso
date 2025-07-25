package com.example.demo.dto;

import com.example.demo.model.VoteType;
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

    private int upvotes;

    private int downvotes;

    private int score;

    private int commentCount;

    private VoteType userVote;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public PostDto(Long id, String author, String title, String content) {
        this.id=id;
        this.author = author;
        this.title = title;
        this.content = content;
    }


    public PostDto(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }
}
