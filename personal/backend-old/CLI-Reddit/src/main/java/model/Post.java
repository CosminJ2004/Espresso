package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Post implements Votable {
    private int id;
    private User author;
    private String summary;
    private String content;
    private LocalDateTime createdAt;

    public Post(User author, String summary, String content) {
        this.id = 0;
        this.author = author;
        this.summary = summary;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Post(int id, User author, String summary, String content, LocalDateTime createdAt) {
        this.id = id;
        this.author = author;
        this.summary = summary;
        this.content = content;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public User getAuthor() {
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
                ", author='" + author.getUsername() + '\'' +
                ", summary='" + summary + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
