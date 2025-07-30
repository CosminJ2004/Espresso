package com.example.demo.dto;

import com.example.demo.model.VoteType;
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
public class CommentResponseDto {

    private Long id;

    @NotNull(message = "Post ID is required")
    private Long postId;

    private Long parentId;

    @NotBlank(message = "Comment content is required")
    @Size(max = 1000, message = "Comment too long")
    private String content;

    @NotBlank(message = "Author is required")
    private String author;

    private Long upvotes;

    private Long downvotes;

    private Long score;

    private VoteType userVote;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<CommentResponseDto> replies = new ArrayList<>();
}