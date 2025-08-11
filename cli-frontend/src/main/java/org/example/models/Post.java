package org.example.models;

import java.util.HashMap;

public class Post {
    private long id;
    private String title;
    private String content;
    private String author;
    private String subreddit;
    private int upvotes;
    private int downvotes;
    private int score;
    private int commentCount;
    private VoteType userVote;
    private String createdAt;
    private String updateAt;
    private HashMap<String, Comment> postComments = new HashMap<>();

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

    public int getScore() {
        return score;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public VoteType getUserVote() {
        return userVote;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public HashMap<String, Comment> getPostComments() {
        return postComments;
    }

    public void addComment(Comment comment) {

        this.postComments.put(Long.toString(comment.getId()), comment);
    }
}
