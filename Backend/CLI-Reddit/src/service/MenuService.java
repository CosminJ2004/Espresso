package service;

import model.Post;
import java.util.Scanner;

public class MenuService {
    private final Scanner scanner;
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final VoteService voteService;

    public MenuService(UserService userService, PostService postService, CommentService commentService, VoteService voteService) {
        this.scanner = new Scanner(System.in);
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

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    userService.login();
                    break;
                case 2:
                    userService.register();
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option, try again.");
                    break;
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

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    postService.createPost();
                    break;
                case 2:
                    for (Post post : postService.getAllPosts()) {
                        System.out.println(postService.display(post));
                    }
                    break;
                case 3: {
                    createPostMenu();
                }
                break;
                case 4:
                    userService.userLogout();
                    break;
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        }
    }

    public void createPostMenu() {
        while (true) {
            postService.expandPost();

            System.out.println("Choose your action for this post:");
            System.out.println("1. Delete the post");
            System.out.println("2. Add a comment");
            System.out.println("3. Add a comment to a comment");
            System.out.println("4. Upvote post");
            System.out.println("5. Downvote post");
            System.out.println("6. Upvote a comment");
            System.out.println("7. Downvote a comment");
            System.out.println("8. Return");

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    postService.deletePost();
                    return;
                case 2:
                    commentService.addCommentToPost();
                    break;
                case 3:
                    commentService.addCommentToComment();
                    break;
                case 4:
                    voteService.upvoteToPost();
                    break;
                case 5:
                    voteService.downvoteToPost();
                    break;
                case 6:
                    voteService.upVoteToComment();
                    break;
                case 7:
                    voteService.downVoteToComment();
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        }
    }

}
