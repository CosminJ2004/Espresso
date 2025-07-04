import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String username;
    private String password;

    public User() {}
    
    private boolean isLoggedIn() {
        return this.username != null && !this.username.isEmpty();
    }
    
    private boolean isNotLoggedIn() {
        return !isLoggedIn();
    }
    
    public boolean deleteUser() {
        if (isNotLoggedIn()) {
            System.out.println("You must be logged in to delete your account.");
            return true;
        }
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Users WHERE username = ?")) {
            
            pstmt.setString(1, this.username);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                this.username = null;
                this.password = null;
                System.out.println("User deleted successfully!");
                return false;
            } else {
                System.out.println("User not found.");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("An error occurred: " + e.getMessage());
            return true;
        }
    }
    
    public String getUsername() {
        return username;
    }
    
    public boolean setUsername(String newUsername) {
        if (isNotLoggedIn()) {
            System.out.println("You must be logged in to change your username.");
            return true;
        }
        
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE Users SET username = ? WHERE username = ?")) {
            
            pstmt.setString(1, newUsername);
            pstmt.setString(2, this.username);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                this.username = newUsername;
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
        return true;
    }
    
    public String getPassword() {
        return password;
    }
    
    public boolean setPassword(String newPassword) {
        if (isNotLoggedIn()) {
            System.out.println("You must be logged in to change your password.");
            return true;
        }
        
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE Users SET password = ? WHERE username = ?")) {
            
            pstmt.setString(1, newPassword);
            pstmt.setString(2, this.username);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                this.password = newPassword;
                return false;
            } else {
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("An error occurred: " + e.getMessage());
            return true;
        }
    }

    public boolean register(String user, String pass) {

        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Users (username, password) VALUES (?, ?)")) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                this.username = user;
                this.password = pass;
                System.out.println("User registered successfully!");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("An error occurred: " + e.getMessage());
            return true;
        }
        System.out.println("Registration failed.");
        return true;
    }

    public boolean login(String user, String pass) {
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT username, password FROM Users WHERE username = ? AND password = ?")) {
            
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                this.username = rs.getString("username");
                this.password = rs.getString("password");
                System.out.println("Login successful! Welcome, " + this.username + "!");
                return false;
            } else {
                System.out.println("Invalid username or password.");
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("An error occurred: " + e.getMessage());
            return true;
        }
    }

    public boolean logout() {
        if (this.username != null) {
            System.out.println("Goodbye, " + this.username + "!");
            this.username = null;
            this.password = null;
            System.out.println("Logged out successfully.");
            return false;
        } else {
            System.out.println("No user is currently logged in.");
            return true;
        }
    }

}



