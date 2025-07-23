package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "posts")
public class Post implements Votable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment în DB
    private int id;

    @ManyToOne(optional = false) // fiecare post are un autor
    @JoinColumn(name = "author_id", nullable = false)
    private Users author;

    private String summary;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String filePath;

    private LocalDateTime createdAt;

    public Post() {
        // Constructorul gol e necesar pentru JPA
    }

    public Post(Users author, String summary, String content) {
        this.author = author;
        this.summary = summary;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Post(int id, Users author, String summary, String content, LocalDateTime createdAt) {
        this.id = id;
        this.author = author;
        this.summary = summary;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public Users getAuthor() {
        return author;
    }

    public String getSummary() {
        return summary;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setAuthor(Users author) {
        this.author = author;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", author=" + author.getUsername() +
                ", summary='" + summary + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    public String getAuthorUsername() {
        return author.getUsername();
    }
}
