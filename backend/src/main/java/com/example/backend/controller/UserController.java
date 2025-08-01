package com.example.backend.controller;

import com.example.backend.dto.UserRequestDto;
import com.example.backend.dto.UserResponseDto;
import com.example.backend.service.UserService;
import com.example.backend.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<UserResponseDto>> getUserById(@PathVariable Long id) {
        return Response.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<Response<List<UserResponseDto>>> getAllUsers() {
        return Response.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<Response<UserResponseDto>> createUser(@RequestBody UserRequestDto userRequest) {
        return Response.ok(userService.createUser(userRequest));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Response<Void>> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return Response.ok("User deleted successfully");
    }

    @PutMapping("/{username}")
    public ResponseEntity<Response<UserResponseDto>> updateUser(@PathVariable String username, @RequestBody UserRequestDto userRequest) {
        return Response.ok(userService.updateUser(username, userRequest));
    }
}
