package com.example.demo.dto;

import com.example.demo.model.VoteType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequestDto {

    private VoteType voteType;

}