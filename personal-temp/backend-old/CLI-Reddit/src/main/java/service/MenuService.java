package service;

import com.mysql.cj.exceptions.ClosedOnExpiredPasswordException;
import model.Post;
import util.Console;

import java.util.Scanner;

public class MenuService {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final VoteService voteService;
    private Post currentPost=null;

    public MenuService(UserService userService, PostService postService, CommentService commentService, VoteService voteService) {

        this.userService = userService;
        this.commentService = commentService;
        this.postService = postService;
        this.voteService = voteService;
    }

    public void createLoginMenu() {
        while (!UserService.isLoggedIn()) {
            System.out.println("Choose your action:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");

            try {
                int option= Console.readInt("");
                switch (option) {
                    case 1:
                        userService.loginUI();
                        break;
                    case 2:
                        userService.registerUI();
                        break;
                    case 3:
                        AnimationService.showGoodbyeAnimation();
                        System.exit(0);
                    default:
                        System.out.println("Invalid option, try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");

            }
        }
    }

    public void createMainMenu() {
        while (UserService.isLoggedIn()) {
            System.out.println("Choose your action:");
            System.out.println("1. Write a post");
            System.out.println("2. Show posts");
            System.out.println("3. Open post");
            System.out.println("4. Log out");

            try {
                int option = Console.readInt("");

                switch (option) {
                    case 1:
                        postService.createPostUI();
                        break;
                    case 2:
                        postService.showAllPosts();
                        break;
                    case 3: {
                        currentPost=postService.expandPostUI();
                        if (currentPost!=null) {
                            createPostMenu();
                        }
                    }
                    break;
                    case 4:
                        userService.userLogout();
                        break;
                    default:
                        System.out.println("Invalid option, try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");

            }
        }
    }

    public void createPostMenu() {
        while (true) {
            System.out.println("Choose your action for this post:");
            System.out.println("1. Delete post");
            System.out.println("2. Add a comment");
            System.out.println("3. Add a comment to a comment");
            System.out.println("4. Upvote post");
            System.out.println("5. Downvote post");
            System.out.println("6. Upvote a comment");
            System.out.println("7. Downvote a comment");
            System.out.println("8. Return");

            try {
                int option = Console.readInt("");

                switch (option) {
                    case 1:
                        postService.deletePostUI(currentPost);

                        return;
                    case 2:
                        commentService.addCommentToPost(currentPost);
                        postService.expand(currentPost);
                        break;
                    case 3:
                        commentService.addCommentToComment();
                        postService.expand(currentPost);
                        break;
                    case 4:
                        voteService.upvoteToPost(currentPost);
                        postService.expand(currentPost);
                        break;
                    case 5:
                        voteService.downvoteToPost(currentPost);
                        postService.expand(currentPost);
                        break;
                    case 6:
                        voteService.upVoteToComment();
                        postService.expand(currentPost);
                        break;
                    case 7:
                        voteService.downVoteToComment();
                        postService.expand(currentPost);
                        break;
                    case 8:
                        return;
                    default:
                        System.out.println("Invalid option, try again.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");

            }
        }
    }

}
