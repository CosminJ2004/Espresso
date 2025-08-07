package org.example.models;

import java.util.HashMap;

public class Subreddit {
    private long id;
    private static long idCount = 0;
    private String name;
    private String displayName;
    private String description;
    private int memberCount;
    private int postCount;
    private String iconUrl;
    private String createdAt;
    private HashMap<String, Post> subPosts;

    public Subreddit(String name, String displayName, String description, int memberCount, String iconUrl, String createdAt) {
        this.id = idCount++;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.memberCount = memberCount;
        this.postCount = 0;
        this.iconUrl = iconUrl;
        this.createdAt = createdAt;
        this.subPosts = new HashMap<>();
    }

    public void addPost(Post post) {
        subPosts.put(Long.toString(post.getId()), post);
        postCount++;
    }

    public HashMap<String, Post> getSubPosts() {
        return subPosts;
    }

    public void setSubPosts(HashMap<String, Post> subPosts) {
        this.subPosts = subPosts;
    }
}
