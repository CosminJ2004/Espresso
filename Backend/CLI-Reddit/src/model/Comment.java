package model;

import service.VoteService;

import java.time.LocalDateTime;
import java.util.*;

public class Comment implements Votable {
    private static int counter = 0;
    private int id;
    private User author;
    private String text;
    private Post post;
    private Comment parent; // null dacă e top-level
    private List<Comment> replies = new ArrayList<>();
    private Map<String, String> userVotes = new HashMap<>();
    private int votes = 0;
    private LocalDateTime dateTime;

    public Comment(User author, String text, Post post, Comment parent) {
        this.id = ++counter;
        this.author = author;
        this.text = text;
        this.post = post;
        this.parent = parent;
        this.dateTime = LocalDateTime.now();
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

    public int getVotes() {
        return votes;
    }

    public Comment getParent() {
        return parent;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void addReply(Comment reply) {
        replies.add(reply);
    }

    public boolean upvote(String username) {
        String prev = userVotes.get(username);
        if ("upvote".equals(prev)) return false;
        if ("downvote".equals(prev)) votes += 2;
        else votes += 1;
        userVotes.put(username, "upvote");
        return true;
    }

    public boolean downvote(String username) {
        String prev = userVotes.get(username);
        if ("downvote".equals(prev)) return false;
        if ("upvote".equals(prev)) votes -= 2;
        else votes -= 1;
        userVotes.put(username, "downvote");
        return true;
    }

    public void display(int level, VoteService voteService) {
        int votes = voteService.getVoteCount(this);
        // indentare în funcție de level
        String indent = " ".repeat(level * 2);
        System.out.println(indent + "Comment ID: " + getId() + " Votes: " + votes);
        System.out.println(indent + getText());

        for (Comment reply : replies) {
            reply.display(level + 1, voteService);
        }
    }

    public Post getPost() {
        return post;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
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
                ", dateTime=" + dateTime +
                '}';
    }
}
