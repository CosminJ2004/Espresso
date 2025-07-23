package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }


    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto dto) {
        try {
            return ResponseEntity.ok(userService.createUser(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        try {
            userService.deleteUser(username);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(
            @PathVariable String username,
            @RequestBody UserDto dto) {
        User updatedUser = userService.updateUser(username, dto);
        return ResponseEntity.ok(updatedUser);
    }


//
//
//
//    // 🔐 Register
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestParam String username, @RequestParam String password) {
//        try {
//            boolean success = userService.register(username, password);
//            return ResponseEntity.ok("User registered successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
//        }
//    }
//
//    // 🔐 Login
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
//        try {
//            boolean success = userService.login(username, password);
//            if (success) {
//                return ResponseEntity.ok("Login successful.");
//            } else {
//                return ResponseEntity.status(401).body("Invalid credentials.");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Login error: " + e.getMessage());
//        }
//    }
//
//    // 🚪 Logout
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout() {
//        if (userService.isLoggedIn()) {
//            String username = userService.getCurrentUser().getUsername();
//            userService.logout();
//            return ResponseEntity.ok("User " + username + " logged out.");
//        } else {
//            return ResponseEntity.badRequest().body("No user is currently logged in.");
//        }
//    }
//
//    // 👤 Get current user
//    @GetMapping("/me")
//    public Users currentUser() {
//        Users currentUser = userService.getCurrentUser();
//        return currentUser;
//
//    }
}
