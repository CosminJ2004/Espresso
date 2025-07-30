package com.example.backend.dto;

import com.example.backend.model.VoteType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequestDto {

    private VoteType voteType;

}