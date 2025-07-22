package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public User getAllUsers() {
        User user=new User("casni","aasfjnas");
        return user;
    }

    // 🔐 Register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username, @RequestParam String password) {
        try {
            boolean success = userService.register(username, password);
            return ResponseEntity.ok("User registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    // 🔐 Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        try {
            boolean success = userService.login(username, password);
            if (success) {
                return ResponseEntity.ok("Login successful.");
            } else {
                return ResponseEntity.status(401).body("Invalid credentials.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login error: " + e.getMessage());
        }
    }

    // 🚪 Logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        if (userService.isLoggedIn()) {
            String username = userService.getCurrentUser().get().getUsername();
            userService.logout();
            return ResponseEntity.ok("User " + username + " logged out.");
        } else {
            return ResponseEntity.badRequest().body("No user is currently logged in.");
        }
    }

    // 👤 Get current user
    @GetMapping("/me")
    public ResponseEntity<?> currentUser() {
        Optional<User> currentUser = userService.getCurrentUser();
        return currentUser
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).body("No user logged in."));
    }
}
