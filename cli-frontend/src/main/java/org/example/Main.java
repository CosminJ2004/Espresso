package org.example;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Scanner;

import org.example.apiservices.*;
import org.example.models.Subreddit;
import org.example.services.PostService;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();

        ApiPostService apiPostService = new ApiPostService();
        ApiCommentService apiCommentService = new ApiCommentService();
        ApiUserService apiUserService = new ApiUserService();
        ApiVoteService apiVoteService = new ApiVoteService();
        PostService postService = new PostService();

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
                    postMenu(scanner, client, apiPostService, postService);
                    break;
                case "2":
                    commentMenu(scanner, client, apiCommentService);
                    break;
                case "3":
                    userMenu(scanner, client, apiUserService);
                    break;
                case "4":
                    voteMenu(scanner, client, apiVoteService);
                    break;
                case "5":
                    System.out.println("La revedere!");
                    return;
                default:
                    System.out.println("Opțiune invalidă. Încearcă din nou.");
            }
        }
    }

    private static void postMenu(Scanner scanner, HttpClient client, ApiPostService apiPostService, PostService postService) throws Exception {
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
                    HashMap<String, Subreddit> subreddits = new HashMap<>();
                    subreddits = postService.populateSubreddits(subreddits, apiPostService.handleGet(client));
                    postService.showPosts(subreddits.get("echipa3_general"));
                    //apiPostService.handleGet(client);
                    break;
                case "2":
                    apiPostService.handlePost(scanner, client);
                    break;
                case "3":
                    apiPostService.handlePut(scanner, client);
                    break;
                case "4":
                    apiPostService.handleDelete(scanner, client);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Comandă necunoscută.");
            }
        }
    }

    private static void commentMenu(Scanner scanner, HttpClient client, ApiCommentService apiCommentService) throws Exception {
        while (true) {
            System.out.println("""
                    === Meniu Comentarii ===
                    1. Afișează comentarii
                    2. Adaugă comentariu
                    3. Modifică comentariu
                    4. Șterge comentariu
                    5. Înapoi
                    """);

            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1":
                    apiCommentService.handleGet(client);
                    break;
                case "2":
                    apiCommentService.handlePost(scanner, client);
                    break;
                case "3":
                    apiCommentService.handlePut(scanner, client);
                    break;
                case "4":
                    apiCommentService.handleDelete(scanner, client);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Comandă necunoscută.");
            }
        }
    }

    private static void userMenu(Scanner scanner, HttpClient client, ApiUserService apiUserService) throws Exception {
        while (true) {
            System.out.println("""
                    === Meniu Utilizatori ===
                    1. Afișează utilizatori
                    2. Înregistrează utilizator
                    3. Modifică utilizator
                    4. Șterge utilizator
                    5. Înapoi
                    """);

            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1":
                    apiUserService.handleGet(client);
                    break;
                case "2":
                    apiUserService.handlePost(scanner, client);
                    break;
                case "3":
                    apiUserService.handlePut(scanner, client);
                    break;
                case "4":
                    apiUserService.handleDelete(scanner, client);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Comandă necunoscută.");
            }
        }
    }

    private static void voteMenu(Scanner scanner, HttpClient client, ApiVoteService apiVoteService) throws Exception {
        while (true) {
            System.out.println("""
                    === Meniu Voturi ===
                    1. Afișează voturi
                    2. Votează postare
                    3. Modifică vot
                    4. Șterge vot
                    5. Înapoi
                    """);

            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1":
                    apiVoteService.handleGet(client);
                    break;
                case "2":
                    apiVoteService.handlePost(scanner, client);
                    break;
                case "3":
                    apiVoteService.handlePut(scanner, client);
                    break;
                case "4":
                    apiVoteService.handleDelete(scanner, client);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Comandă necunoscută.");
            }
        }
    }

}
