package org.example.ui;

import org.example.models.Post;

public class PostUI {
    private static PostUI instance;
    private static final String TITLE_SEPARATOR = "=";

    public static PostUI getInstance() {
        if (instance == null) {
            instance = new PostUI();
        }
        return instance;
    }

    public void renderPost(Post post) {
        System.out.println(post.getCreatedAt() + " " + post.getTitle());
        System.out.println(TITLE_SEPARATOR.repeat((post.getCreatedAt() + " " + post.getTitle()).length()));
        System.out.println(post.getContent());
        System.out.println("\n\n");
    }
}
