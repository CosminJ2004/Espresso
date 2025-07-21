package com.example.demo.dto;

public class PostDto {
    private String authorUsername;
    private String summary;
    private String content;

    // Constructors
    public PostDto() {}

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
}
