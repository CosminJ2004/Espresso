package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public boolean register(String username, String password) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Users (username, password) VALUES (?, ?)")) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                UserContext.setCurrentUser(new User(username, password));
                System.out.println("Registered and logged in.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Register failed: " + e.getMessage());
        }
        return false;
    }

    public boolean login(String username, String password) {
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Users WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User(username, password);
                UserContext.setCurrentUser(user);
                System.out.println("Logged in as " + username);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Login failed: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteCurrentUser() {
        if (!UserContext.isLoggedIn()) {
            System.out.println("Not logged in.");
            return false;
        }

        String username = UserContext.getCurrentUser().getUsername();

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM Users WHERE username = ?")) {

            stmt.setString(1, username);
            int affected = stmt.executeUpdate();

            if (affected > 0) {
                UserContext.logout();
                System.out.println("Account deleted.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Delete failed: " + e.getMessage());
        }

        return false;
    }

    public boolean changeUsername(String newUsername) {
        if (!UserContext.isLoggedIn()) {
            System.out.println("Not logged in.");
            return false;
        }

        String currentUsername = UserContext.getCurrentUser().getUsername();

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Users SET username = ? WHERE username = ?")) {

            stmt.setString(1, newUsername);
            stmt.setString(2, currentUsername);

            int affected = stmt.executeUpdate();

            if (affected > 0) {
                UserContext.getCurrentUser().setUsername(newUsername);
                System.out.println("Username changed.");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Change failed: " + e.getMessage());
        }

        return false;
    }

    public boolean changePassword(String newPassword) {
        if (!UserContext.isLoggedIn()) {
            System.out.println("Not logged in.");
            return false;
        }

        String currentUsername = UserContext.getCurrentUser().getUsername();

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE Users SET password = ? WHERE username = ?")) {

            stmt.setString(1, newPassword);
            stmt.setString(2, currentUsername);

            int affected = stmt.executeUpdate();

            if (affected > 0) {
                UserContext.getCurrentUser().setPassword(newPassword);
                System.out.println("Password changed.");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Change failed: " + e.getMessage());
        }

        return false;
    }
}
