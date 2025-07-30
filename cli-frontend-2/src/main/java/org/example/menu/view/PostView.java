package org.example.menu.view;

import org.example.models.Post;
import org.example.models.Subreddit;
import org.example.textformatters.FormatTextStyle;

public class PostView {
    private static PostView postView;
    private FormatTextStyle formatTextStyle = FormatTextStyle.getInstance();

    public static PostView getInstance() {
        if (postView == null) {
            postView = new PostView();
        }
        return postView;
    }

    public void printMenuOptions() {
        System.out.println("""
                === Meniu Postări ===
                1. Afișează toate postările
                2. Creează postare
                3. Editează postare
                4. Șterge postare
                5. Înapoi
                """);
    }

    public void printInputRequest(String input) {
        switch (input) {
            case "title":
                System.out.println("Please enter the title to your post: ");
                break;
            case "content":
                System.out.println("Please enter the content of your post: ");
                break;
            case "author":
                System.out.println("Please enter the author of your post: ");
        }
    }

    public void printAllPosts(Subreddit subreddit) {
        for (Post post : subreddit.getPosts()) {
            System.out.println(formatTextStyle.applyBold(post.getTitle()));
            System.out.println(formatTextStyle.applyBold(post.getAuthor()));
            System.out.println(formatTextStyle.applyBold("-------------"));
            System.out.println(formatTextStyle.applyBold(post.getContent()));
        }

    }
}
