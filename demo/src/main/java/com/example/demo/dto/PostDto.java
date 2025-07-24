package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;

    @NotBlank(message = "Author username is required")
    private String authorUsername;

    @NotBlank(message = "Summary is required")
    @Size(max = 255, message = "Summary too long")
    private String summary;

    private String content;

    private String filePath;

    private LocalDateTime createdAt;

    private int commentCount;

    private int voteScore;


    private MultipartFile file;

    // Constructors
    public PostDto(Long id,String authorUsername, String summary, String content,String filePath) {
        this.id=id;
        this.filePath = filePath;
        this.authorUsername = authorUsername;
        this.summary = summary;
        this.content = content;
    }

    public PostDto(String authorUsername, String summary, String content, MultipartFile file) {
        this.authorUsername = authorUsername;
        this.summary = summary;
        this.content = content;
        this.file = file;
    }
    public PostDto(String authorUsername, String summary, String content) {
        this.authorUsername = authorUsername;
        this.summary = summary;
        this.content = content;
    }
    // Getters & Setters
    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
