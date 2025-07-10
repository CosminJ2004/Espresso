package content;

import java.util.Scanner;

public class InputReader {
    private static Scanner scanner;
    private static final String INVALID_INPUT_ID = "Id format is invalid, please retry.";
    private static final String INVALID_INPUT_USER = "Username doesn't have the required minimum of characters.";
    private static final String INVALID_INPUT_PASS = "Password doesn't have the required minimum of characters.";
    private static final int MINIM_USER_CHARS= 5;
    private static final int MINIM_PASS_CHARS= 5;

    public InputReader() {
        scanner = new Scanner(System.in);
    }

    public static String readUsername(String prompt) {
        while (true) {
            System.out.print(prompt);
            String username = scanner.nextLine().trim();

            if (username.length() < MINIM_USER_CHARS) {
                System.out.println(INVALID_INPUT_USER);
            } else {
                return username;
            }
        }
    }

    public static String readPassword(String prompt) {
        while (true) {
            System.out.print(prompt);
            String username = scanner.nextLine().trim();

            if (username.length() < MINIM_PASS_CHARS) {
                System.out.println(INVALID_INPUT_PASS);
            } else {
                return username;
            }
        }
    }

    public static int readId(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INPUT_ID);
            }
        }
    }
}
