import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private Service service;

    public Menu(Service myService) {
        scanner = new Scanner(System.in);
        service = myService;
    }

    public void createLoginMenu() {
        while (!service.isUserLoggedIn()) {
            System.out.println("Choose your action:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    service.login();
                    break;
                case 2:
                    service.register();
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

    public void createPostMenu() {
        while (true) {
            service.expandPost();

            System.out.println("Choose your action for this post:");
            System.out.println("1. Delete the post");
            System.out.println("2. Add a comment");
            System.out.println("3. Add a comment to a comment");
            System.out.println("4. Upvote post");
            System.out.println("5. Downvote post");
            System.out.println("6. Vote a comment");
            System.out.println("7. Return");

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {

                case 1:
                    service.deletePost();
                    return;
                case 2:
                    service.addCommentToPost();
                    break;
                case 3:
                    service.addCommentToComment();
                    break;
                case 4:
                    service.upVoteToPost();
                    break;
                case 5:
                    service.downVoteToPost();
                case 6:
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        }
    }

    public void createMainMenu() {
        while (service.isUserLoggedIn()) {
            System.out.println("Choose your action:");
            System.out.println("1. Write a post");
            System.out.println("2. Show posts");
            System.out.println("3. Open post");
            System.out.println("4. Log out");

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    service.createPost();
                    break;
                case 2:
                    service.showPosts();
                    break;
                case 3:
                    service.openPost();
                    createPostMenu();
                    break;
                case 4:
                    service.userLogout();
                    break;
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        }
    }
}
