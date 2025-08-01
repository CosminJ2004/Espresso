package org.example.models;

import java.util.ArrayList;

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
    private ArrayList<Post> subPosts;

    public Subreddit(String name, String displayName, String description, int memberCount, int postCount, String iconUrl, String createdAt) {
        this.id = idCount++;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.memberCount = memberCount;
        this.postCount = postCount;
        this.iconUrl = iconUrl;
        this.createdAt = createdAt;
        this.subPosts = new ArrayList<>();
    }

    public void addPost(Post post) {
        subPosts.add(post);
    }

    public ArrayList<Post> getSubPosts() {
        return subPosts;
    }

    public void setSubPosts(ArrayList<Post> subPosts) {
        this.subPosts = subPosts;
    }
}
