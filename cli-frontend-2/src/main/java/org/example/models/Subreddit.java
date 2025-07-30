package org.example.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Subreddit {
    private int id;
    private static int idCounter = 0;
    private String name;
    private String displayName;
    private String description;
    private int membercount;
    private int postCount;
    private String iconUrl;
    private LocalDateTime createdAt;
    private ArrayList<Post> posts;
    //private TreeSet<User> users;

    public Subreddit() {
        this.id = idCounter++;
        this.name = "Hardcoded Sub";
        this.displayName = "Hardcoded Sub";
        this.description = "Hardcoded Sub by Matei Boss";
        this.membercount = 1;
        this.postCount = 0;
        this.iconUrl = "nada";
        this.createdAt = LocalDateTime.now();
        this.posts = new ArrayList<>();
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public void setMembercount(int membercount) {
        this.membercount = membercount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getMembercount() {
        return membercount;
    }

    public int getPostCount() {
        return postCount;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }
}
