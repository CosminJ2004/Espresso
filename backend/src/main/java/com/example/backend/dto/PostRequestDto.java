package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title too long")
    private String title;

    private String content;

    @NotBlank(message = "Author is required")
    private String author;

    private String subreddit;
}
