package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 300, message = "Title too long")
    private String title;

    @Size(max = 10000, message = "Content too long")
    private String content;

    @NotBlank(message = "Author is required")
    private String author;

    private String subreddit;



}
