package presentation.io;

import java.util.Scanner;

public final class ConsoleIO implements AutoCloseable {
    private final Scanner scanner = new Scanner(System.in);

    public String readLine() {
        return scanner.nextLine();
    }

    @Override
    public void close() {
        scanner.close();
    }
}
