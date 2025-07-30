package org.example.models;

import java.time.LocalDateTime;

public class Post {
    private int id;
    private int idCount = 0;
    private String title;
    private String content;
    private String auuthor;
    private String subreddit;
    private int score;
    private int commentCount;
    private VoteType userVote;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Post(String title, String content, String author, String subreddit, int score, int commentCount, VoteType userVote, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = idCount++;
        this.title = title;
        this.content = content;
        this.auuthor = author;
        this.subreddit = subreddit;
        this.score = score;
        this.commentCount = commentCount;
        this.userVote = userVote;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    //Constructor pentru postare noua nu mapata de la dto
    public Post(String title, String content, String author, String subreddit, int score, int commentCount, VoteType userVote) {
        this.id = idCount++;
        this.title = title;
        this.content = content;
        this.auuthor = author;
        this.subreddit = subreddit;
        this.score = score;
        this.commentCount = commentCount;
        this.userVote = userVote;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return auuthor;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public int getScore() {
        return score;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public VoteType getUserVote() {
        return userVote;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

}
