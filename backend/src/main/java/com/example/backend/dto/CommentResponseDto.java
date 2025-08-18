package com.example.backend.dto;

import com.example.backend.model.VoteType;
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

    private String id;
    private String postId;
    private String parentId;
    private String content;
    private String author;
    private Long upvotes;
    private Long downvotes;
    private Long score;
    private VoteType userVote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CommentResponseDto> replies = new ArrayList<>();
}