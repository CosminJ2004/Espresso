package com.example.backend.controller;

import com.example.backend.dto.UserDto;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import com.example.backend.util.Response;
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
    public ResponseEntity<Response<UserDto>> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return Response.ok(user);
    }

    @GetMapping
    public ResponseEntity<Response<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return Response.ok(users);
    }

    @PostMapping
    public ResponseEntity<Response<UserDto>> createUser(@RequestBody UserDto dto) {
        UserDto user = userService.createUser(dto);
        return Response.ok(user);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Response<Void>> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return Response.ok("User deleted successfully");
    }

    @PutMapping("/{username}")
    public ResponseEntity<Response<User>> updateUser(@PathVariable String username, @RequestBody UserDto dto) {
        User updatedUser = userService.updateUser(username, dto);
        return Response.ok(updatedUser);
    }
}
