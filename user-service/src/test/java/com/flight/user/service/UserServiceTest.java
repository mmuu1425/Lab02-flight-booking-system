package com.flight.user.service;

import com.flight.user.dto.UserDTO;
import com.flight.user.dto.LoginRequest;
import com.flight.user.entity.User;
import com.flight.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUser() {
        // 给定
        User user = new User("testuser", "Test User", "test@example.com");
        User savedUser = new User("testuser", "Test User", "test@example.com");
        savedUser.setUserUid(UUID.randomUUID());

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // 当
        UserDTO result = userService.createUser(user);

        // 那么
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getName()).isEqualTo("Test User");
        verify(userRepository).save(user);
    }

    @Test
    void shouldNotCreateUserWithDuplicateUsername() {
        // 给定
        User user = new User("existinguser", "Existing User", "existing@example.com");
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // 当 + 那么
        assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Username already exists: existinguser");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldGetUserByUsername() {
        // 给定
        UUID userUid = UUID.randomUUID();
        User user = new User("testuser", "Test User", "test@example.com");
        user.setUserUid(userUid);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // 当
        UserDTO result = userService.getUserByUsername("testuser");

        // 那么
        assertThat(result.getUserUid()).isEqualTo(userUid);
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByUsername() {
        // 给定
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // 当 + 那么
        assertThatThrownBy(() -> userService.getUserByUsername("nonexistent"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found: nonexistent");
    }
}