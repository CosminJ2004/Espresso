package repository;

import logger.Log;
import logger.LogLevel;
import model.Post;
import model.User;
import util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostRepository {

    public boolean save(Post post) {
        String sql = "INSERT INTO Posts (author, summary, content, createdAt) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, post.getAuthor().getUsername());
            stmt.setString(2, post.getSummary());
            stmt.setString(3, post.getContent());
            stmt.setTimestamp(4, Timestamp.valueOf(post.getCreatedAt()));

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        post.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            Log.log(LogLevel.ERROR,"Error saving post: " + e.getMessage());
            return false;
        }
    }

    public Optional<Post> findById(int id) {
        String sql = "SELECT p.id, p.author, p.summary, p.content, p.createdAt, " +
                     "u.id, u.password FROM Posts p " +
                     "JOIN Users u ON p.author = u.username WHERE p.id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User author = new User(
                    rs.getInt("u.id"),
                    rs.getString("author"),
                    rs.getString("password")
                );


                
                return Optional.of(createPostFromResultSet(rs, author));
            }
        } catch (SQLException e) {
            Log.log(LogLevel.ERROR,"Error finding post by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Post> findAll() {
        String sql = "SELECT p.id, p.author, p.summary, p.content, p.createdAt, " +
                     "u.id, u.password FROM Posts p " +
                     "JOIN Users u ON p.author = u.username ORDER BY p.createdAt DESC";
        List<Post> posts = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User author = new User(
                    rs.getInt("u.id"),
                    rs.getString("author"),
                    rs.getString("password")
                );
                
                posts.add(createPostFromResultSet(rs, author));
            }
        } catch (SQLException e) {
            Log.log(LogLevel.ERROR,"Error finding all posts: " + e.getMessage());
        }
        return posts;
    }

    public List<Post> findByAuthor(User author) {
        String sql = "SELECT p.id, p.author, p.summary, p.content, p.createdAt, " +
                     "u.id, u.password FROM Posts p " +
                     "JOIN Users u ON p.author = u.username WHERE p.author = ? ORDER BY p.createdAt DESC";
        List<Post> posts = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, author.getUsername());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                posts.add(createPostFromResultSet(rs, author));
            }
        } catch (SQLException e) {
            Log.log(LogLevel.ERROR,"Error finding posts by author: " + e.getMessage());
        }
        return posts;
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM Posts WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.log(LogLevel.ERROR,"Error deleting post: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePost(int id, String summary, String content) {
        String sql = "UPDATE Posts SET summary = ?, content = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, summary);
            stmt.setString(2, content);
            stmt.setInt(3, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Log.log(LogLevel.ERROR,"Error updating post: " + e.getMessage());
            return false;
        }
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM Posts";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            Log.log(LogLevel.ERROR,"Error counting posts: " + e.getMessage());
        }
        return 0;
    }

    private Post createPostFromResultSet(ResultSet rs, User author) throws SQLException {
        return new Post(
            rs.getInt("id"),
            author,
            rs.getString("summary"),
            rs.getString("content"),
            rs.getTimestamp("createdAt").toLocalDateTime()
        );
    }
}
