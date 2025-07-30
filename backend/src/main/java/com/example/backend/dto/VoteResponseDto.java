package com.example.backend.dto;

import com.example.backend.model.VoteType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponseDto {

    private Long upvotes;

    private Long downvotes;

    private Long score;

    private VoteType userVote;
}