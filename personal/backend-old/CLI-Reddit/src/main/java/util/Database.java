package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL  = "jdbc:mysql://136.243.172.35:3307/default";
    private static final String USER = "mysql";
    private static final String PASS = "QIcgFPwlBt1JnI57QTv1gS36YZVvkKUVyZAolfbw20fZ76lAoTzuGdkrHsTHs0ON";

    public static void CheckDriver()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL Driver not found");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }


}
