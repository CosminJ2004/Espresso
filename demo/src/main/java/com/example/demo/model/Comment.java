package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.*;

public class Comment implements Votable {
    private int id;
    private static int counter = 0;
    private User author;
    private String text;
    private Post post;
    private int votes = 0;
    private Comment parent; // null dacă e top-level
    private List<Comment> replies = new ArrayList<>();
    private LocalDateTime createdAt;

    public Comment(User author, String text, Post post, Comment parent) {
        this.id = ++counter;
        this.author = author;
        this.text = text;
        this.post = post;
        this.parent = parent;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public Post getPost() {
        return post;
    }

    public int getVotes() {
        return votes;
    }

    public Comment getParent() {
        return parent;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", author='" + author.getUsername() + '\'' +
                ", text='" + text + '\'' +
                ", post=" + post.getId() +
                ", parent=" + (parent != null ? parent.getId() : "none") +
                ", dateTime=" + createdAt +
                '}';
    }
}
