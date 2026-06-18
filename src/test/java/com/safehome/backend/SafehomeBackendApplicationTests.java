package com.safehome.backend;

import com.safehome.backend.domain.model.User;
import com.safehome.backend.domain.repository.UserRepository;
import com.safehome.backend.domain.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SafehomeBackendApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setFullName("Test User");
        user.setEmail("testuser123@safehome.com");
        user.setPasswordHash("password123");
        user.setPhone("+51999111222");
        user.setRole("ADMIN_USER");

        User saved = userService.register(user);

        assertNotNull(saved.getId());
        assertEquals("testuser123@safehome.com", saved.getEmail());
        assertEquals("ADMIN_USER", saved.getRole());
        assertNotEquals("password123", saved.getPasswordHash());
    }

    @Test
    void testFindUserByEmail() {
        User user = new User();
        user.setFullName("Find Test");
        user.setEmail("findtest@safehome.com");
        user.setPasswordHash("password123");
        user.setRole("ADMIN_USER");

        userService.register(user);

        assertTrue(userRepository.existsByEmail("findtest@safehome.com"));
    }

    @Test
    void testDuplicateEmailThrowsException() {
        User user1 = new User();
        user1.setFullName("User One");
        user1.setEmail("duplicate@safehome.com");
        user1.setPasswordHash("password123");
        user1.setRole("ADMIN_USER");

        userService.register(user1);

        User user2 = new User();
        user2.setFullName("User Two");
        user2.setEmail("duplicate@safehome.com");
        user2.setPasswordHash("password456");
        user2.setRole("ADMIN_USER");

        assertThrows(RuntimeException.class, () -> userService.register(user2));
    }
}