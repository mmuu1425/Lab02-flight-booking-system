package com.flight.user.repository;

import com.flight.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindUserByUsername() {
        // 给定
        User user = new User("testuser", "Test User", "test@example.com");
        entityManager.persistAndFlush(user);

        // 当
        Optional<User> found = userRepository.findByUsername("testuser");

        // 那么
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test User");
    }

    @Test
    void shouldCheckIfUsernameExists() {
        // 给定
        User user = new User("existinguser", "Existing User", "existing@example.com");
        entityManager.persistAndFlush(user);

        // 当
        boolean exists = userRepository.existsByUsername("existinguser");

        // 那么
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseForNonExistingUsername() {
        // 当
        boolean exists = userRepository.existsByUsername("nonexisting");

        // 那么
        assertThat(exists).isFalse();
    }
}