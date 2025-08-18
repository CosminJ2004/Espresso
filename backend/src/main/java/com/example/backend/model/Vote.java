package com.example.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "votes", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"user_id", "post_id"}),
           @UniqueConstraint(columnNames = {"user_id", "comment_id"})
       })
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public Vote() {}

    public Vote(User user, Post post, VoteType type) {
        this.user = user;
        this.post = post;
        this.comment = null;
        this.type = type;
    }

    public Vote(User user, Comment comment, VoteType type) {
        this.user = user;
        this.post = null;
        this.comment = comment;
        this.type = type;
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

    public int getVoteValue() {
        return type == VoteType.UP ? 1 : -1;
    }

    @Override
    public String toString() {
        String target = post != null ? "Post[" + post.getId() + "]" : 
                       comment != null ? "Comment[" + comment.getId() + "]" : "Unknown";
        
        return "Vote{" +
                "id=" + id +
                ", type=" + type +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", target=" + target +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vote vote)) return false;
        return getId() != null && getId().equals(vote.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}