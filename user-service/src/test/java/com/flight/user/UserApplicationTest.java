package com.flight.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flight.user.entity.User;
import com.flight.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldReturnHealthCheck() throws Exception {
        mockMvc.perform(get("/manage/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        String userJson = """
            {
                "username": "john",
                "name": "John Doe",
                "email": "john@example.com"
            }
            """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.userUid").exists());
    }

    @Test
    void shouldNotCreateUserWithDuplicateUsername() throws Exception {
        // 先创建一个用户
        User existingUser = new User("alice", "Alice Smith", "alice@example.com");
        userRepository.save(existingUser);

        // 尝试创建相同用户名的用户
        String duplicateUserJson = """
        {
            "username": "alice",
            "name": "Alice Brown",
            "email": "alice2@example.com"
        }
        """;

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateUserJson))
                .andExpect(status().isConflict()); // 改为 409
    }

    @Test
    void shouldGetUserByUsername() throws Exception {
        // 先创建用户
        User user = new User("bob", "Bob Wilson", "bob@example.com");
        User savedUser = userRepository.save(user);

        mockMvc.perform(get("/api/users/username/bob"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("bob"))
                .andExpect(jsonPath("$.name").value("Bob Wilson"))
                .andExpect(jsonPath("$.email").value("bob@example.com"));
    }

    @Test
    void shouldReturnNotFoundForNonExistingUsername() throws Exception {
        mockMvc.perform(get("/api/users/username/nonexistent"))
                .andExpect(status().isNotFound()); // 改为 404
    }

    @Test
    void shouldGetUserById() throws Exception {
        // 先创建用户
        User user = new User("charlie", "Charlie Brown", "charlie@example.com");
        User savedUser = userRepository.save(user);

        mockMvc.perform(get("/api/users/" + savedUser.getUserUid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("charlie"))
                .andExpect(jsonPath("$.userUid").value(savedUser.getUserUid().toString()));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        // 创建多个用户
        userRepository.save(new User("user1", "User One", "user1@example.com"));
        userRepository.save(new User("user2", "User Two", "user2@example.com"));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username").exists())
                .andExpect(jsonPath("$[1].username").exists());
    }

    @Test
    void shouldUpdateUser() throws Exception {
        // 先创建用户
        User user = new User("david", "David Lee", "david@example.com");
        User savedUser = userRepository.save(user);

        String updateJson = """
            {
                "name": "David Kim",
                "email": "david.kim@example.com"
            }
            """;

        mockMvc.perform(put("/api/users/" + savedUser.getUserUid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("David Kim"))
                .andExpect(jsonPath("$.email").value("david.kim@example.com"))
                .andExpect(jsonPath("$.username").value("david"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        // 先创建用户
        User user = new User("eva", "Eva Green", "eva@example.com");
        User savedUser = userRepository.save(user);

        mockMvc.perform(delete("/api/users/" + savedUser.getUserUid()))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));

        // 验证用户确实被删除 - 期望返回 404 错误
        mockMvc.perform(get("/api/users/" + savedUser.getUserUid()))
                .andExpect(status().isNotFound()); // 改为 404
    }

    @Test
    void shouldLoginUser() throws Exception {
        // 先创建用户
        User user = new User("frank", "Frank Ocean", "frank@example.com");
        userRepository.save(user);

        String loginJson = """
            {
                "username": "frank",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("frank"))
                .andExpect(jsonPath("$.name").value("Frank Ocean"));
    }

    @Test
    void shouldCheckUsernameExists() throws Exception {
        // 先创建用户
        User user = new User("grace", "Grace Hopper", "grace@example.com");
        userRepository.save(user);

        mockMvc.perform(get("/api/users/check-username/grace"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void shouldCheckUsernameNotExists() throws Exception {
        mockMvc.perform(get("/api/users/check-username/unknown"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }
}