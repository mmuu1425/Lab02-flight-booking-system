package com.flight.user.service;

import com.flight.user.dto.UserDTO;
import com.flight.user.dto.LoginRequest;
import com.flight.user.entity.User;
import com.flight.user.repository.UserRepository;
import com.flight.user.exception.UserNotFoundException;
import com.flight.user.exception.UserAlreadyExistsException;
import com.flight.user.exception.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO createUser(User user) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + user.getUsername());
        }
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    public UserDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
        return convertToDTO(user);
    }

    public UserDTO getUserById(UUID userUid) {
        User user = userRepository.findById(userUid)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userUid));
        return convertToDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO updateUser(UUID userUid, User userDetails) {
        User user = userRepository.findById(userUid)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userUid));

        if (userDetails.getName() != null) {
            user.setName(userDetails.getName());
        }
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }

        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    public void deleteUser(UUID userUid) {
        User user = userRepository.findById(userUid)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userUid));
        userRepository.delete(user);
    }

    public UserDTO loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));
        // 实际项目中应该验证密码
        return convertToDTO(user);
    }

    public boolean checkUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // 实体转DTO的辅助方法
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getUserUid(),
                user.getUsername(),
                user.getName(),
                user.getEmail()
        );
    }
}