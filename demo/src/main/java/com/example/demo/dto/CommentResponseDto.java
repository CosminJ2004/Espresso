package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class CommentResponseDto {
    private Long id;
    private String text;
    private String authorUsername;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> replies = new ArrayList<>();
}
