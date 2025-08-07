package org.example.models;

public class User {
    long id;
    String username;
    String password;
    String createdAt;
    int postCount;
    int commentCount;

    public User() {
        postCount = 0;
        commentCount = 0;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
