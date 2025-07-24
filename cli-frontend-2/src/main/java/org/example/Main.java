import java.net.http.*;
import java.net.URI;
import java.util.Scanner;

public class Main {

    private static final String BASE_URL = "http://localhost:8080/posts";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HttpClient client = HttpClient.newHttpClient();

        System.out.println("Java CLI Client");
        System.out.println("Comenzi disponibile: get, post, put, delete, exit");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine();

            try {
                switch (command) {
                    case "get":
                        handleGet(client);
                        break;

                    case "post":
                        handlePost(scanner, client);
                        break;

                    case "put":
                        handlePut(scanner, client);
                        break;

                    case "delete":
                        handleDelete(scanner, client);
                        break;

                    case "exit":
                        System.out.println("La revedere!");
                        return;

                    default:
                        System.out.println("Comandă necunoscută.");
                }
            } catch (Exception e) {
                System.out.println("A apărut o eroare: " + e.getMessage());
            }
        }
    }

    private static void handleGet(HttpClient client) throws Exception {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(getResponse.body());
    }

    private static void handlePost(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("Author username: ");
        String username = scanner.nextLine();
        System.out.print("Summary: ");
        String summary = scanner.nextLine();
        System.out.print("Content: ");
        String content = scanner.nextLine();


        String json = String.format("{\"authorUsername\": \"%s\", \"summary\": \"%s\", \"content\": \"%s\"}", username, summary,content);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(postResponse.body());
    }

    private static void handlePut(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("ID de actualizat: ");
        String id = scanner.nextLine();
        System.out.print("Noul username: ");
        String username = scanner.nextLine();

        System.out.print("Noul summary: ");
        String summary = scanner.nextLine();


        System.out.print("Noul content: ");
        String content = scanner.nextLine();

        String json = String.format("{\"authorUsername\": \"%s\", \"summary\": \"%s\", \"content\": \"%s\"}", username, summary,content);

        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> putResponse = client.send(putRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(putResponse.body());
    }

    private static void handleDelete(Scanner scanner, HttpClient client) throws Exception {
        System.out.print("ID de șters: ");
        String deleteId = scanner.nextLine();

        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + deleteId))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(deleteResponse.body());
    }
}
