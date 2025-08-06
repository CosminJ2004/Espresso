package com.example.backend.service;

import com.example.backend.dto.UserRequestDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private static User currentUser;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserResponseDto convertToDto(User user) {
        return new UserResponseDto(user.getId(), user.getUsername(), user.getPassword());
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return convertToDto(user);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto createUser(UserRequestDto userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        User user = new User(userRequest.getUsername(), userRequest.getPassword());

        // exemplu de criptare parolă (folosind bean-ul passwordEncoder)
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Transactional
    public void deleteUser(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        userRepository.deleteByUsername(username);
    }

    public UserResponseDto updateUser(String username, UserRequestDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return convertToDto(userRepository.save(user));
    }

    public void logout() {
        currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser == null;
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
        if (isLoggedIn()) {
            throw new IllegalStateException("User is not logged in.");
        }

        String username = currentUser.getUsername();
        userRepository.deleteByUsername(username);
        logout();
        return true;
    }

    public boolean changeUsername(String newUsername) {
        if (isLoggedIn()) {
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
        if (isLoggedIn()) {
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
