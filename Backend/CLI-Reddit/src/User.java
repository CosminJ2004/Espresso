import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String username;
    private String password;

    public User() {}
    
    public boolean deleteUser() {
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Users WHERE username = ?")) {
            
            pstmt.setString(1, this.username);
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
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
        this.username = user;
        this.password = pass;

        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Users (username, password) VALUES (?, ?)")) {

            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                return false;
            }

        } catch (SQLException e) {
            System.err.println("An error occurred: " + e.getMessage());
            return true;
        }
        return false;
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

    public void logout() {
        if (this.username != null) {
            System.out.println("Goodbye, " + this.username + "!");
            this.username = null;
            this.password = null;
            System.out.println("Logged out successfully.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

}



