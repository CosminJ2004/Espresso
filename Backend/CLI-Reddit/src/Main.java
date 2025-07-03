//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        System.out.println("Espresso");

        try {
            Connection conn = DB.getConnection();
            System.out.println("Database connection established!");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }

    }
}