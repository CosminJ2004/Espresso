package org.example.menu.pages;

import org.example.menu.view.PostView;

import java.util.Scanner;

public class PostPage {
    private PostView postView = PostView.getInstance();
    private static PostPage postPage;

    public static PostPage getInstance() {
        if (postPage == null) {
            postPage = new PostPage();
        }
        return postPage;
    }

    public void selectOption() {
        postView.printMenuOptions();
        Scanner sc = new Scanner(System.in);


    }
}
