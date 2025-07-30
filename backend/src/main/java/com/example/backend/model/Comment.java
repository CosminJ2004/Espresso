package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "comments")
public class Comment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Comment text is required")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    private List<Comment> replies = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vote> votes = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Comment() {}

    public Comment(User author, String text, Post post) {
        this.author = author;
        this.text = text;
        this.post = post;
        this.parent = null;
        this.createdAt = LocalDateTime.now();
    }

    public Comment(User author, String text, Post post, Comment parent) {
        this.author = author;
        this.text = text;
        this.post = post;
        this.parent = parent;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public int getReplyCount() {
        return replies != null ? replies.size() : 0;
    }

    public long getUpvoteCount() {
        if (votes == null || votes.isEmpty()) {
            return 0;
        }
        return votes.stream()
                .filter(vote -> vote.getType() == VoteType.up)
                .count();
    }

    public long getDownvoteCount() {
        if (votes == null || votes.isEmpty()) {
            return 0;
        }
        return votes.stream()
                .filter(vote -> vote.getType() == VoteType.down)
                .count();
    }

    public long getScore() {
        return getUpvoteCount() - getDownvoteCount();
    }

    public VoteType getUserVote(User user) {
        if (votes == null || votes.isEmpty()) {
            return null;
        }
        return votes.stream()
                .filter(vote -> vote.getUser().equals(user))
                .map(Vote::getType)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + (text != null && text.length() > 50 ? 
                    text.substring(0, 50) + "..." : text) + '\'' +
                ", author=" + (author != null ? author.getUsername() : "null") +
                ", repliesCount=" + getReplyCount() +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment comment)) return false;
        return getId() != null && getId().equals(comment.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }



}