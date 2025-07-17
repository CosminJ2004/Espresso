package repository;

import logger.*;
import model.User;
import util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository {

    public boolean save(User user) {
        String sql = "INSERT INTO Users (username, password) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.log(LogLevel.ERROR,"Error saving user: " + e.getMessage());
            return false;
        }
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new User(
                        rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            Log.log(LogLevel.ERROR,"Error finding user: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new User(
                        rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            Log.log(LogLevel.ERROR,"Error authenticating user: " + e.getMessage());
        }
        return Optional.empty();
    }

    public boolean updateUsername(String oldUsername, String newUsername) {
        String sql = "UPDATE Users SET username = ? WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newUsername);
            stmt.setString(2, oldUsername);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.log(LogLevel.ERROR,"Error updating username: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePassword(String username, String newPassword) {
        String sql = "UPDATE Users SET password = ? WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, username);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.log(LogLevel.ERROR,"Error updating password: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteByUsername(String username) {
        String sql = "DELETE FROM Users WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.log(LogLevel.ERROR,"Error deleting user: " + e.getMessage());
            return false;
        }
    }

    public boolean exists(String username) {
        return findByUsername(username).isPresent();
    }
}
