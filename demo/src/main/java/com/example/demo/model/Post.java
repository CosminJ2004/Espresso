package com.example.demo.model;

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
public class Post implements Votable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Summary is required")
    @Size(max = 255, message = "Summary too long")
    private String summary;

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

    public Post(User author, String summary, String content) {
        this.author = author;
        this.summary = summary;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Post(User author, String summary, String content, String filePath) {
        this.author = author;
        this.summary = summary;
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

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }

    public void addVote(Vote vote) {
        votes.add(vote);
        vote.setPost(this);
    }

    public void removeVote(Vote vote) {
        votes.remove(vote);
        vote.setPost(null);
    }

    public int getCommentCount() {
        return comments != null ? comments.size() : 0;
    }

    public int getVoteCount() {
        if (votes == null || votes.isEmpty()) {
            return 0;
        }
        return votes.stream()
                .mapToInt(Vote::getVoteValue)
                .sum();
    }

    public boolean hasComments() {
        return comments != null && !comments.isEmpty();
    }

    public boolean hasVotes() {
        return votes != null && !votes.isEmpty();
    }

    public boolean hasFile() {
        return filePath != null && !filePath.trim().isEmpty();
    }

    public boolean isPopular() {
        return getVoteCount() >= 10 || getCommentCount() >= 5;
    }

    public String getAuthorUsername() {
        return author != null ? author.getUsername() : "Unknown";
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", summary='" + (summary != null && summary.length() > 50 ? 
                    summary.substring(0, 50) + "..." : summary) + '\'' +
                ", author=" + getAuthorUsername() +
                ", commentCount=" + getCommentCount() +
                ", voteScore=" + getVoteCount() +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return getId() != null && getId().equals(post.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}