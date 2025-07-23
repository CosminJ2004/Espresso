package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static Users currentUser;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static Users getCurrentUser() {
        return (currentUser);
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean register(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("Username already exists.");
        }

        Users newUser = new Users(username, password);
        userRepository.save(newUser);
        currentUser = newUser;
        return true;
    }

    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        Optional<Users> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isPresent()) {
            currentUser = user.get();
            return true;
        }

        return false;
    }

    public boolean deleteCurrentUser() {
        if (!isLoggedIn()) {
            throw new IllegalStateException("User is not logged in.");
        }

        String username = currentUser.getUsername();
        userRepository.deleteByUsername(username);
        logout();
        return true;
    }

    public boolean changeUsername(String newUsername) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("User is not logged in.");
        }

        if (newUsername == null || newUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("New username cannot be empty.");
        }

        if (userRepository.existsByUsername(newUsername)) {
            throw new IllegalStateException("Username already exists.");
        }

        Users user = currentUser;
        user.setUsername(newUsername);
//        userRepository.update(user);
        currentUser = user;
        return true;
    }

    public boolean changePassword(String newPassword) {
        if (!isLoggedIn()) {
            throw new IllegalStateException("User is not logged in.");
        }

        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters.");
        }

        Users user = currentUser;
        user.setPassword(newPassword);
//        userRepository.update(user);
        return true;
    }
}
