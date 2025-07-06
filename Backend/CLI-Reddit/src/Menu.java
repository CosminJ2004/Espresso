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
                    service.login(scanner);
                    break;
                case 2:
                    service.register(scanner);
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

    public void createPostMenu(Scanner scanner) {
        while (true) {
            System.out.println("Choose your action for this post:");
            System.out.println("1. Delete the post");
            System.out.println("2. Add a comment");
            System.out.println("3. Add a comment to a comment");
            System.out.println("4. Vote the post");
            System.out.println("5. Vote a comment");
            System.out.println("6. Return");

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    service.deletePost(scanner);
                    return;
                /*case 2:
                    System.out.println("Choose the post id you want to comment on: ");
                    int idPost=scanner.nextInt();
                    scanner.nextLine();
                    for(Post post:posts) {
                        if(post.getId()==idPost)
                        {
                            System.out.println("Write your comment: ");
                            String textComment=scanner.nextLine();
                            //casting and adding them to the list of comments of posts
                            CommentPost commentPost = new CommentPost(user, textComment, post);
                            commentPosts.add(commentPost);//adding also in a list
                            post.addComment(commentPost);//adding comments to a post object
                        }

                    }
                    break;
                case 3:
                    System.out.println("Choose the comment id you want to comment on: ");
                    int idComment=scanner.nextInt();
                    scanner.nextLine();
                    for(CommentPost comment:commentPosts) {
                        if(comment.getId()==idComment)
                        {
                            System.out.println("Write your comment: ");
                            String textComment=scanner.nextLine();
                            //adding comments of comments after casting them
                            CommentCom commentCom = new CommentCom(user, textComment,comment);
                            comment.addReply(commentCom);//adding repluies to the comment object
                            commentComs.add(commentCom);//still adding to a list
                            //TO DO erase this logic
                        }


                    }
                    break;*/
                case 4:
                    service.addVoteToPost(scanner);
                    break;
                case 5:
                    break;
                case 6:
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
                    service.createPost(scanner);
                    break;
                case 2:
                    service.showPosts(scanner);
                    break;
                case 3:
                    service.openPost(scanner);
                    createPostMenu(scanner);
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
