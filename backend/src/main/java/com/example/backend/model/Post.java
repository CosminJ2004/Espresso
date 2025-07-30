package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "title is required")
    @Size(max = 255, message = "title too long")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    private String filePath;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vote> votes = new ArrayList<>();

    public Post() {}

    public Post(User author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Post(User author, String title, String content, String filePath) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.filePath = filePath;
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

    public long getCommentCount() {
        return comments != null ? comments.size() : 0;
    }

    public long getVoteCount() {
        if (votes == null || votes.isEmpty()) {
            return 0;
        }
        return votes.stream()
                .mapToInt(Vote::getVoteValue)
                .sum();
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

    public String getAuthorUsername() {
        return author != null ? author.getUsername() : "Unknown";
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + (title != null && title.length() > 50 ? 
                    title.substring(0, 50) + "..." : title) + '\'' +
                ", author=" + getAuthorUsername() +
                ", commentCount=" + getCommentCount() +
                ", voteScore=" + getVoteCount() +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post post)) return false;
        return getId() != null && getId().equals(post.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}