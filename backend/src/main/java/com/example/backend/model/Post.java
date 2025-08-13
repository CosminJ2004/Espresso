package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "title is required")
    @Size(max = 300, message = "title too long")
    private String title;

    @Column(columnDefinition = "TEXT")
    @Size(max = 10000, message = "content too long")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    private String imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filter_id")
    private Filter filter;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("createdAt ASC")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vote> votes = new ArrayList<>();

    public Post() {
    }

    public Post(User author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Post(User author, String title, String content, Filter filter) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.filter = filter;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (filter != null && imageId == null) {
            imageId = UUID.randomUUID().toString();
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
                .filter(vote -> vote.getType() == VoteType.UP)
                .count();
    }

    public long getDownvoteCount() {
        if (votes == null || votes.isEmpty()) {
            return 0;
        }
        return votes.stream()
                .filter(vote -> vote.getType() == VoteType.DOWN)
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

    public String getImageUrl() {
        if (imageId == null) {
            return null;
        }
        return generateImageUrl(imageId, filter != null ? filter.getName() : "none");
    }
    
    private String generateImageUrl(String imageId, String filter) {
        return String.format("http://13.61.12.137/images/%s/%s.png", filter, imageId);
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