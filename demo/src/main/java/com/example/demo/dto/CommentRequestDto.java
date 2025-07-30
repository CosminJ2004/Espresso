package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "Comment content is required")
    @Size(max = 1000, message = "Comment too long")
    private String content;

    @NotBlank(message = "Author is required")
    private String author;

    private Long parentId;
}