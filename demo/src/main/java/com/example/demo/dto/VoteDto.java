package com.example.demo.dto;

import com.example.demo.model.VoteType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {

    @NotNull(message = "Vote type is required")
    private VoteType type;

    private String username;

    private Long userId;

    private Long postId;

    private Long commentId;

    public VoteDto(VoteType type, String username, Long postId) {
        this.type = type;
        this.username = username;
        this.postId = postId;
        this.commentId = null;
    }

    public VoteDto(VoteType type, String username, Long commentId, boolean isComment) {
        this.type = type;
        this.username = username;
        this.postId = null;
        this.commentId = commentId;
    }
}