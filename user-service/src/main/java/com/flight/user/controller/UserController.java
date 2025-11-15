package com.flight.user.controller;

import com.flight.user.dto.UserDTO;
import com.flight.user.dto.LoginRequest;
import com.flight.user.entity.User;
import com.flight.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public UserDTO createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/username/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/{userUid}")
    public UserDTO getUserById(@PathVariable UUID userUid) {
        return userService.getUserById(userUid);
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{userUid}")
    public UserDTO updateUser(@PathVariable UUID userUid, @RequestBody User userDetails) {
        return userService.updateUser(userUid, userDetails);
    }

    @DeleteMapping("/{userUid}")
    public String deleteUser(@PathVariable UUID userUid) {
        userService.deleteUser(userUid);
        return "User deleted successfully";
    }

    @PostMapping("/login")
    public UserDTO loginUser(@RequestBody LoginRequest loginRequest) {
        return userService.loginUser(loginRequest);
    }

    @GetMapping("/check-username/{username}")
    public boolean checkUsernameExists(@PathVariable String username) {
        return userService.checkUsernameExists(username);
    }
}