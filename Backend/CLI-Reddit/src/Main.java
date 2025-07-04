import java.util.*;
/*import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDate;*/

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Service service = new Service();
        while (true) {
            boolean isLoggedIn = false;
            while (!isLoggedIn) {
                System.out.println("Choose your action:");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");

                int option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1:
                        isLoggedIn = service.login(scanner);
                        break;
                    case 2:
                        isLoggedIn = service.register(scanner);
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("Invalid option, try again.");
                        break;
                }
            }

            System.out.println("3. Create a post");
            System.out.println("4. Show a post");
            System.out.println("5. Delete a post");
            System.out.println("6. Add a comment to a post");
            System.out.println("7. Add a comment to a comment");
            System.out.println("8. Vote a post");
            System.out.println("9. Vote a comment");
            System.out.println("10. Exit");

            try {
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
                        service.createPost(scanner);
                        break;
                    case 4:
                        service.showPost(scanner);
                        break;
                    case 5:
                        service.deletePost(scanner);
                        break;
                    case 6:
                        service.addCommentToPost(scanner);
                        break;
                    case 7:
                        service.addCommentToComment(scanner);
                        break;
                    case 8:
                        service.addVoteToPost(scanner);
                        break;
                    case 9:
                        service.addVoteToComment(scanner);
                        break;
                    case 10:
                        System.out.println("Exiting.");
                        scanner.close();
                        break;
                    default:
                        System.out.println("invalid option. try again");
                        break;
                }
            } catch (NoSuchElementException e) {
                System.out.println("Error: Please enter a valid integer option.");
            }
        }
    }
}