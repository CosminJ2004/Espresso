import java.util.*;
public class Service {

    List<Post> posts = new ArrayList<>();

    public boolean login(Scanner scanner) {
        return true;
    }

    public boolean register(Scanner scanner) {
        return true;
    }
    public void createPost(Scanner scanner)
    {   System.out.println("Enter the author of the post: ");
        String author = scanner.nextLine();
        System.out.print("Enter the summary of the post: ");
        String summary = scanner.nextLine();
        System.out.print("Enter the content of the post: ");
        String content = scanner.nextLine();
        Post post = new Post(author, summary, content);
        posts.add(post);
    }
    public void showPost(Scanner scanner)
    {
        for(Post post:posts) {

            String msg = post.display();
            System.out.println(msg);
        }
    }
    public void deletePost(Scanner scanner)
    {
        System.out.print("Enter the ID of the post to delete: ");
        int idToDelete = scanner.nextInt();
        scanner.nextLine();

        boolean found = false;

        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() == idToDelete) {
                posts.remove(i);
                System.out.println("Post deleted.");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Post not found.");
        }

    }
    public void addCommentToPost(Scanner scanner)
    {

    }

    public void addCommentToComment(Scanner scanner) {

    }

    public void addVoteToPost(Scanner scanner) {

    }


    public void createLoginMenu(Scanner scanner) {
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            System.out.println("Choose your action:");
            System.out.println("1. Login.");
            System.out.println("2. Register.");
            System.out.println("3. Exit.");

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    isLoggedIn = login(scanner);
                    break;
                case 2:
                    isLoggedIn = register(scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        }
    }
    public void addVoteToComment(Scanner scanner)
    {}

    public void createMainMenu(Scanner scanner) {
        while (true) {
            System.out.println("Choose your action:");
            System.out.println("1. Write a post.");
            System.out.println("2. Show posts.");
            System.out.println("3. Exit");

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                      createPost(scanner);
                    break;
                case 2:
                    showPost(scanner);
                    System.out.println("Alege postarea pe care vrei sa o vizualizezi");
                    //expand
                    createPostMenu(scanner);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid option, try again.");
                    break;
            }
        }
    }

    public void createPostMenu(Scanner scanner) {
        while (true) {
            System.out.println("Choose your action:");
            System.out.println("1. Delete a post");
            System.out.println("2. Add a comment to post");
            System.out.println("3. Add a comment to a comment");
            System.out.println("4. Vote a post");
            System.out.println("5. Vote a comment");
            System.out.println("6. Return");

            int option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
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
}
