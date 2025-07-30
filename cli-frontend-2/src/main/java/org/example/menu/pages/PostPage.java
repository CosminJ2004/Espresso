package org.example.menu.pages;

import org.example.menu.MenuPage;
import org.example.menu.view.PostView;
import org.example.models.Subreddit;
import org.example.services.PostService;

import java.util.Scanner;

public class PostPage {
    private static PostView postView = PostView.getInstance();
    private static PostPage postPage;
    private static PostService postService = PostService.getInstance();
    private static Scanner sc = new Scanner(System.in);

    public static PostPage getInstance() {
        if (postPage == null) {
            postPage = new PostPage();
        }
        return postPage;
    }

    public MenuPage back() {
        return MenuPage.MAIN_MENU;
    }

    public static void createPost() {
        postView.printInputRequest("title");
        String title = sc.nextLine();
        postView.printInputRequest("content");
        String content = sc.nextLine();
        postView.printInputRequest("author");
        String author = sc.nextLine();
        String json = String.format("""
        {
            "author": "%s",
            "title": "%s",
            "content": "%s"
            
        }
        """, author,title, content);
        postService.handlePost();

    }

    public void selectOption(Subreddit myHardcodedSubreddit) {
        postView.printMenuOptions();
        int input = sc.nextInt();
        switch (input) {
            case 1:
                postView.printAllPosts(myHardcodedSubreddit);
                break;
            case 2:

        }


    }
}
