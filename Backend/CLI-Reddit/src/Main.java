import java.util.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Service service = new Service();

        while (true) {
            if (service.user.isNotLoggedIn()) {
                service.createLoginMenu(scanner);

                if (service.user.isNotLoggedIn()) {
                    break;
                }
            } else {
                service.createMainMenu(scanner);

                if (service.user.isNotLoggedIn()) {
                    continue;
                } else {
                    break;
                }
            }
        }
        
        scanner.close();
        System.out.println("Goodbye!");
    }
}