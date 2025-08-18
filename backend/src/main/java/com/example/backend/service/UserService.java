package com.example.backend.service;

import com.example.backend.dto.UserRequestDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.exception.*;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.util.logger.Logger;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Logger log;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, Logger log) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.log = log;
    }

    public UserResponseDto getUserById(String id) {
        log.info("Fetching user by ID: " + id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error("User not found with ID: " + id);
            return new UserNotFoundException("User not found with id: " + id);
        });
        log.info("User found: " + user.getUsername());
        return UserMapper.toDto(user);
    }

    public List<UserResponseDto> getAllUsers() {
        log.info("Fetching all users");
        List<User> users = userRepository.findAll();
        log.info("Retrieved " + users.size() + " users");
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserResponseDto createUser(UserRequestDto userRequest) {
        log.info("Creating new user: " + userRequest.getUsername());
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            log.error("Username already taken: " + userRequest.getUsername());
            throw new UsernameAlreadyTakenException(userRequest.getUsername());
        }
        User user = new User(userRequest.getUsername(), userRequest.getPassword());

        // exemplu de criptare parolă (folosind bean-ul passwordEncoder)
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Password encoded for user: " + user.getUsername());

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: " + savedUser.getId());
        return UserMapper.toDto(savedUser);
    }

    //metoda folosita in frontend-cli pentru a loga un user ca sa faca postari/comentarii
    public UserResponseDto loginUser(UserRequestDto userRequest) {
        String username = userRequest.getUsername().trim();
        log.info("Login attempt for user: " + username);
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Login failed - user not found: " + username);
                    return new InvalidCredentialsException();
                });//nu arunc userNotFound ca sa nu expun ca exista un user cu acel username

        if (!passwordEncoder.matches(userRequest.getPassword(), user.getPassword())) {
            log.warn("Login failed - invalid password for user: " + username);
            throw new InvalidCredentialsException(); //Invalid username or password
        }
        log.info("Login successful for user: " + username);
        return UserMapper.toDto(user);
    }

    //metoda folosita in frontend-cli pentru a cauta useri dupa username
    public UserResponseDto getUserByUsername(String username) {
        log.info("Fetching user by username: " + username);
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: " + username);
                    return new UserNotFoundException("No user has been found with username: " + username);
                });
        return UserMapper.toDto(user);
    }

    @Transactional
    public void deleteUser(String username) {
        log.info("Deleting user: " + username);
        if (!userRepository.existsByUsername(username)) {
            log.error("User not found for deletion: " + username);
            throw new UserNotFoundException("User not found: " + username);
        }

        userRepository.deleteByUsername(username);
        log.info("User deleted successfully: " + username);
    }

    public UserResponseDto updateUser(String username, UserRequestDto dto) {
        log.info("Updating user: " + username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found for update: " + username);
                    return new UserNotFoundException("User not found: " + username);
                });

        String newUsername = dto.getUsername();
        if (newUsername != null) {
            newUsername = newUsername.trim();
            if (!newUsername.isEmpty() && !newUsername.equals(user.getUsername())) {
                if (userRepository.existsByUsername(newUsername)) {
                    log.error("Username already taken during update: " + newUsername);
                    throw new UsernameAlreadyTakenException(newUsername);
                }
                log.info("Updating username from '" + user.getUsername() + "' to '" + newUsername + "'");
                user.setUsername(newUsername);
            }
        }

        String newPassword = dto.getPassword();
        if (newPassword != null && !newPassword.isBlank()) {
            if (!passwordEncoder.matches(newPassword, user.getPassword())) {
                log.info("Updating password for user: " + user.getUsername());
                user.setPassword(passwordEncoder.encode(newPassword));
            }
        }
        User saved = userRepository.save(user);
        log.info("User updated successfully: " + saved.getUsername());
        return UserMapper.toDto(saved);
    }
}
