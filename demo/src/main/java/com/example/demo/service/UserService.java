package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static User currentUser;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getUsername(),user.getPassword());
    }

    public static User getCurrentUser() {
        return (currentUser);
    }


    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return convertToDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserDto createUser(UserDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        User newUser = new User(dto.getUsername(), dto.getPassword());



        User savedUser = userRepository.save(newUser);
        return convertToDto(savedUser);
    }
    @Transactional
    public void deleteUser(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        userRepository.deleteByUsername(username);
    }

    public User updateUser(String username, UserDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return userRepository.save(user);
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

        User newUser = new User(username, password);
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

        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);
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

        User user = currentUser;
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

        User user = currentUser;
        user.setPassword(newPassword);
//        userRepository.update(user);
        return true;
    }
}
