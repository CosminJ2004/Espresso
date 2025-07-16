package service;

import model.User;
import repository.UserRepository;
import logger.*;
import util.Console;

import java.util.Optional;

public class UserService {
    private static final UserService instance = new UserService();
    private static User currentUser = null;
    private final UserRepository userRepository;

    private UserService() {
        this.userRepository = new UserRepository();
    }

    public static UserService getInstance() {
        return instance;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean register(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username cannot be empty.");
            return false;
        }

        if (password == null || password.length() < 6) {
            System.out.println("Password must be at least 6 characters.");
            return false;
        }

        if (userRepository.exists(username)) {
            System.out.println("Username already exists.");
            return false;
        }

        User newUser = new User(username, password);
        if (userRepository.save(newUser)) {
            setCurrentUser(newUser);
            System.out.println("Registered and logged in.");
            return true;
        }

        System.out.println("Registration failed.");
        return false;
    }

    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("Username cannot be empty.");
            return false;
        }

        if (password == null || password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return false;
        }

        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent()) {
            setCurrentUser(user.get());
            System.out.println("Logged in as " + username);
            return true;
        }

        System.out.println("Invalid username or password.");
        return false;
    }

    public boolean deleteCurrentUser() {
        if (!isLoggedIn()) {
            System.out.println("Not logged in.");
            return false;
        }

        String username = getCurrentUser().getUsername();

        if (userRepository.deleteByUsername(username)) {
            logout();
            System.out.println("Account deleted.");
            return true;
        }

        System.out.println("Failed to delete account.");
        return false;
    }

    public boolean changeUsername(String newUsername) {
        if (!isLoggedIn()) {
            System.out.println("Not logged in.");
            return false;
        }

        if (newUsername == null || newUsername.trim().isEmpty()) {
            System.out.println("New username cannot be empty.");
            return false;
        }

        if (userRepository.exists(newUsername)) {
            System.out.println("Username already exists.");
            return false;
        }

        String currentUsername = getCurrentUser().getUsername();

        if (userRepository.updateUsername(currentUsername, newUsername)) {
            getCurrentUser().setUsername(newUsername);
            System.out.println("Username changed.");
            return true;
        }

        System.out.println("Failed to change username.");
        return false;
    }

    public boolean changePassword(String newPassword) {
        if (!isLoggedIn()) {
            System.out.println("Not logged in.");
            return false;
        }

        if (newPassword == null || newPassword.length() < 6) {
            System.out.println("New password must be at least 6 characters.");
            return false;
        }

        String currentUsername = getCurrentUser().getUsername();

        if (userRepository.updatePassword(currentUsername, newPassword)) {
            getCurrentUser().setPassword(newPassword);
            System.out.println("Password changed.");
            return true;
        }

        System.out.println("Failed to change password.");
        return false;
    }

    public void loginUI() {
        Log.log(LogLevel.INFO, "Login attempt initiated");
        String username = Console.readCredential("Please enter your username: ");
        String password = Console.readCredential("Please enter your password: ");

        Log.log(LogLevel.DEBUG, "Attempting login for user: " + username);
        boolean loginSuccess = login(username, password);

        if (loginSuccess) {
            Log.log(LogLevel.INFO, "User logged in successfully: " + username);
        } else {
            Log.log(LogLevel.WARN, "Login failed for user: " + username);
        }
    }

    public void registerUI() {
        Log.log(LogLevel.INFO, "Registration attempt initiated");
        String username = Console.readCredential("Please enter the desired username: ");
        String password = Console.readCredential("Please enter the desired password: ");

        Log.log(LogLevel.DEBUG, "Attempting registration for user: " + username);
        boolean registrationSuccess = register(username, password);

        if (registrationSuccess) {
            Log.log(LogLevel.INFO, "User registered and logged in successfully: " + username);
        } else {
            Log.log(LogLevel.WARN, "Registration failed for user: " + username);
        }
    }

    public void userLogout() {
        if (getCurrentUser() != null) {
            Log.log(LogLevel.INFO, "User logout: " + getCurrentUser().getUsername());
        }
        logout();
    }
}
