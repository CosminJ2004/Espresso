package org.example.models;

import java.util.HashMap;

public class Comment {
    private long id;
    private long postId;
    private long parentId;
    private String content;
    private String author;
    private int upvotes;
    private int downvotes;
    private int score;
    private VoteType userVote;
    private String createdAt;
    private String updatedAt;
    private HashMap<String, Comment> replies;

    public long getId() {
        return id;
    }

    public long getPostId() {
        return postId;
    }

    public long getParentId() {
        return parentId;
    } // null in cazul in care nu e reply

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public int getScore() {
        return score;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public HashMap<String, Comment> getReplies() {
        return this.replies;
    }

}
