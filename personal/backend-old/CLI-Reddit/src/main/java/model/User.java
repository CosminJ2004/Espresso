package model;

import java.util.Objects;

public class User {
    private int id;
    private String username;
    private String password;

    public User(String username, String password) {
        id = 0;
        this.username = username.trim();
        this.password = password;
    }

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username.trim();
        this.password = password;
    }

    public int getId() { return id; }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public void setUsername(String username) {
        this.username = username.trim();
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
