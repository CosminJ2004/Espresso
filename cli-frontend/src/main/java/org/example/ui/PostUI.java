package org.example.ui;

import org.example.models.Comment;
import org.example.models.Post;
import org.example.models.Subreddit;

import java.util.ArrayList;
import java.util.Scanner;

public class PostUI {
    private static PostUI instance;
    private CommentUI commentUI = CommentUI.getInstance();
    private final static String TITLE_SEPARATOR = "=";

    public static PostUI getInstance() {
        if (instance == null) {
            instance = new PostUI();
        }
        return instance;
    }

    static void renderPost(Post post) {
        System.out.println("Post ID: " + post.getId());
        System.out.println(post.getCreatedAt() + " " + post.getTitle());
        System.out.println(TITLE_SEPARATOR.repeat((post.getCreatedAt() + " " + post.getTitle()).length()));
        System.out.println(post.getContent());
        System.out.println("\n\n");
    }

    public void showFeed(Subreddit subreddit) {
        for (Post post : subreddit.getSubPosts().values()) {
            renderPost(post);
            /*
            int i = 2;
            for (Comment comment : post.getPostComments().values()) {
                commentUI.renderComment(comment, 1);
                i--;
                if (i == 0) break;
            }
             */
        }
    }

    public ArrayList<String> getPostDetailsFromUser() {
        ArrayList<String> postDetails = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter post title: ");
        postDetails.add(sc.nextLine());
        System.out.print("Enter post content: ");
        postDetails.add(sc.nextLine());
        System.out.print("Enter post author name: ");
        postDetails.add(sc.nextLine());
        System.out.print("Enter post subreddit name: ");
        postDetails.add(sc.nextLine());

        return postDetails;
    }

    public ArrayList<String> getUpdatedPostDetailsFromUser() {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> updatedDetails = new ArrayList<>();
        System.out.print("Enter post id: ");
        updatedDetails.add(sc.nextLine());
        System.out.print("Enter updated post title: ");
        updatedDetails.add(sc.nextLine());
        System.out.print("Enter updated post content: ");
        updatedDetails.add(sc.nextLine());

        return updatedDetails;
    }

    public long getPostIDFromUser() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter post id: ");
        return Long.parseLong(sc.nextLine());
    }


}
