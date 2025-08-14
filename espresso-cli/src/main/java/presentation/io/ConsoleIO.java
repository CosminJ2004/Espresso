package presentation.io;

import java.util.Scanner;

public final class ConsoleIO implements AutoCloseable {
    private final Scanner scanner = new Scanner(System.in);

    public String readLine() {
        return scanner.nextLine();
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int readInt() { // numere ca input
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public int readInt(String prompt) {
        System.out.print(prompt);
        return readInt();
    }

    public String readPassword() {
        return readLine("Password: ");
    }

    public String readUsername() {
        return readLine("Username: ");
    }

    @Override
    public void close() {
        scanner.close();
    }
}
