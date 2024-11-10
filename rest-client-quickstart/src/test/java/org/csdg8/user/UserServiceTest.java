package org.csdg8.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.csdg8.model.exception.InvalidCredentialsException;
import org.csdg8.model.exception.UserAlreadyExistsException;
import org.csdg8.model.exception.UserNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
public class UserServiceTest {

    @Inject
    UserService userService;

    @BeforeEach
    @Transactional
    public void setup() {
        User.add("admin", "admin1234", Set.of("admin"));
        User.add("user", "user1234", Set.of("user"));
    }

    @AfterEach
    @Transactional
    public void teardown() {
        User.deleteAll();
    }

    @Test
    public void shouldValidateUserWithCorrectCredentials() {
        Optional<User> user = userService.validateUser("admin", "admin1234");
        assertTrue(user.isPresent());
        assertEquals("admin", user.get().getUsername());
    }

    @Test
    public void shouldNotValidateUserWithIncorrectCredentials() {
        Optional<User> user = userService.validateUser("admin", "wrongpassword");
        assertFalse(user.isPresent());
    }

    @Test
    public void shouldGetUserRoleForExistingUser() {
        Optional<Set<String>> roles = userService.getUserRole("admin");
        assertTrue(roles.isPresent());
        assertTrue(roles.get().contains("admin"));
    }

    @Test
    public void shouldNotGetUserRoleForNonExistentUser() {
        Optional<Set<String>> roles = userService.getUserRole("nonexistentuser");
        assertFalse(roles.isPresent());
    }

    @Test
    public void shouldAddUserSuccessfully() {
        userService.addUser("newuser", "password123", Set.of("user"));
        Optional<User> user = userService.findByUsername("newuser");
        assertTrue(user.isPresent());
        assertEquals("newuser", user.get().getUsername());
    }

    @Test
    public void shouldThrowUserAlreadyExistsExceptionWhenAddingExistingUser() {
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.addUser("admin", "password123", Set.of("admin"));
        });
    }

    @Test
    public void shouldThrowInvalidCredentialsExceptionForInvalidUsername() {
        assertThrows(InvalidCredentialsException.class, () -> {
            userService.addUser("u", "password123", Set.of("user"));
        });
    }

    @Test
    public void shouldThrowInvalidCredentialsExceptionForInvalidPassword() {
        assertThrowsExactly(InvalidCredentialsException.class, () -> {
            userService.addUser("newuser", "short", Set.of("user"));
        });
    }

    @Test
    public void shouldGetUserByUsername() {
        Long id = userService.findByUsername("admin").orElseThrow().id;
        User user = userService.getUser(id);
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
    }

    @Test
    public void shouldThrowUserNotFoundExceptionForNonExistentUser() {
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUser(99999999L);
        });
    }

    @Test
    public void shouldGetAllUsers() {
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("admin", users.get(0).getUsername());
        assertEquals("user", users.get(1).getUsername());
    }
}
