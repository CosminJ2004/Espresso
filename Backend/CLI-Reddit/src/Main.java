import java.util.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Service service = new Service();
        Menu menu = new Menu(service);

        while (true) {
            menu.createLoginMenu();
            menu.createMainMenu();
        }
    }
}