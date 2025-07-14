package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Comment {
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

    public void display(int indent) {
        String prefix = "  ".repeat(indent);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.println(prefix + "[" + id + "] " + text + " (by " + author + ") Votes: " + votes + " | Posted: " + dateTime.format(formatter));
        System.out.println(replies.size());
        for (Comment reply : replies) {
            reply.display(indent + 1);
        }
    }
}
