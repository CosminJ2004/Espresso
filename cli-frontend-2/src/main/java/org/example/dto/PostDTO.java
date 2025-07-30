package org.example.dto;

import org.example.models.VoteType;

import java.time.LocalDateTime;

public class PostDTO {
    private long id;
    private String title;
    private String content;
    private String author;
    private String subreddit;
    private int upvotes;
    private int downvotes;
    private int score;
    private VoteType userVote;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostDTO(String title, String content, String author) {
        this.id = 0;
        this.title = title;
        this.content = content;
        this.author = author;
        this.subreddit = "";
        this.upvotes = 0;
        this.downvotes = 0;
        this.score = 0;
        this.userVote = null;
        this.commentCount = 0;
        this.createdAt = null;
        this.updatedAt = null;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public int getScore() {
        return score;
    }

    public VoteType getUserVote() {
        return userVote;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}