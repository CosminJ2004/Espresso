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

    private Long upvotes;

    private Long downvotes;

    private Long score;

    private Long userVote;

}