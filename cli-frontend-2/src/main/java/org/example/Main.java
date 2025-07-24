package org.example;

import java.net.http.HttpClient;
import java.util.Scanner;

import org.example.services.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();

        PostService postService = new PostService();
        CommentService commentService = new CommentService();
        UserService userService = new UserService();
        VoteService voteService = new VoteService();

        System.out.println("CLI Java Client - Interfață interactivă");

        while (true) {
            System.out.println("""
                
                === Meniu Principal ===
                1. Postări
                2. Comentarii
                3. Utilizatori
                4. Voturi
                5. Exit
                Alege o opțiune (1-5):
                """);

            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    postMenu(scanner, client, postService);
                    break;
                case "2":
                    commentMenu(scanner, client, commentService);
                    break;
                case "3":
                    userMenu(scanner, client, userService);
                    break;
                case "4":
                    voteMenu(scanner, client, voteService);
                    break;
                case "5":
                    System.out.println("La revedere!");
                    return;
                default:
                    System.out.println("Opțiune invalidă. Încearcă din nou.");
            }
        }
    }

    private static void postMenu(Scanner scanner, HttpClient client, PostService postService) throws Exception {
        while (true) {
            System.out.println("""
                === Meniu Postări ===
                1. Afișează toate postările
                2. Creează postare
                3. Editează postare
                4. Șterge postare
                5. Înapoi
                """);

            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1":
                    postService.handleGet(client);
                    break;
                case "2":
                    postService.handlePost(scanner, client);
                    break;
                case "3":
                    postService.handlePut(scanner, client);
                    break;
                case "4":
                    postService.handleDelete(scanner, client);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Comandă necunoscută.");
            }
        }
    }

    private static void commentMenu(Scanner scanner, HttpClient client, CommentService commentService) throws Exception {
        while (true) {
            System.out.println("""
                === Meniu Comentarii ===
                1. Adaugă comentariu
                2. Înapoi
                """);

            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1":
                    commentService.handlePost(scanner, client);
                    break;
                case "2":
                    return;
                default:
                    System.out.println("Comandă necunoscută.");
            }
        }
    }

    private static void userMenu(Scanner scanner, HttpClient client, UserService userService) throws Exception {
        while (true) {
            System.out.println("""
                === Meniu Utilizatori ===
                1. Înregistrează utilizator
                2. Înapoi
                """);

            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1":
                    userService.register(scanner, client);
                    break;
                case "2":
                    return;
                default:
                    System.out.println("Comandă necunoscută.");
            }
        }
    }

    private static void voteMenu(Scanner scanner, HttpClient client, VoteService voteService) throws Exception {
        while (true) {
            System.out.println("""
                === Meniu Voturi ===
                1. Votează postare
                2. Înapoi
                """);

            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1":
                    voteService.votePost(scanner, client);
                    break;
                case "2":
                    return;
                default:
                    System.out.println("Comandă necunoscută.");
            }
        }
    }
}
