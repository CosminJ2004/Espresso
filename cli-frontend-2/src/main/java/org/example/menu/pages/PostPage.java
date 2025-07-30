package org.example.menu.pages;

import org.example.menu.view.PostView;

import java.util.Scanner;

public class PostPage {
    PostView postView = PostView.getInstance();

    public void selectOption() {
        postView.printMenuOptions();
        Scanner sc = new Scanner(System.in);


    }
}
