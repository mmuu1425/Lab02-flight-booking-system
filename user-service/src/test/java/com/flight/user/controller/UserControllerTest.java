package com.flight.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.user.dto.UserDTO;
import com.flight.user.entity.User;
import com.flight.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUser() throws Exception {
        // 给定
        User user = new User("john", "John Doe", "john@example.com");
        UserDTO userDTO = new UserDTO(UUID.randomUUID(), "john", "John Doe", "john@example.com");

        when(userService.createUser(any(User.class))).thenReturn(userDTO);

        // 当 + 那么
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void shouldGetUserByUsername() throws Exception {
        // 给定
        UserDTO userDTO = new UserDTO(UUID.randomUUID(), "alice", "Alice Smith", "alice@example.com");
        when(userService.getUserByUsername("alice")).thenReturn(userDTO);

        // 当 + 那么
        mockMvc.perform(get("/api/users/username/alice"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.name").value("Alice Smith"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        // 给定
        UserDTO user1 = new UserDTO(UUID.randomUUID(), "user1", "User One", "user1@example.com");
        UserDTO user2 = new UserDTO(UUID.randomUUID(), "user2", "User Two", "user2@example.com");
        List<UserDTO> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        // 当 + 那么
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        // 给定
        UUID userUid = UUID.randomUUID();
        User userDetails = new User();
        userDetails.setName("Updated Name");
        userDetails.setEmail("updated@example.com");

        UserDTO updatedUser = new UserDTO(userUid, "john", "Updated Name", "updated@example.com");
        when(userService.updateUser(eq(userUid), any(User.class))).thenReturn(updatedUser);

        // 当 + 那么
        mockMvc.perform(put("/api/users/" + userUid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        // 给定
        UUID userUid = UUID.randomUUID();
        doNothing().when(userService).deleteUser(userUid);

        // 当 + 那么
        mockMvc.perform(delete("/api/users/" + userUid))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        verify(userService).deleteUser(userUid);
    }
}