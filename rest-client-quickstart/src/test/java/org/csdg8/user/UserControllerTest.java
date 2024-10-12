package org.csdg8.user;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import org.csdg8.model.exception.InvalidCredentialsException;
import org.csdg8.model.exception.UserAlreadyExistsException;
import org.csdg8.model.exception.UserNotFoundException;
import org.csdg8.user.dto.RegistrationRequest;
import org.csdg8.user.dto.UserResponse;
import org.csdg8.user.dto.UserResponseList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@QuarkusTest
public class UserControllerTest {

    @Inject
    UserController userController;

    @BeforeEach
    @Transactional
    public void setup() {
        User.add("admin", "admin", Set.of("admin"));
        User.add("user", "user", Set.of("user"));
    }

    @AfterEach
    @Transactional
    public void teardown() {
        User.deleteAll();
    }

    @Test
    public void shouldRegisterUserAndReturnCreatedResponse() {
        RegistrationRequest request = new RegistrationRequest("john", "password123");
        Response response = userController.register(request);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals("User registered successfully", response.getEntity());
    }

    @Test
    public void shouldThrowUserAlreadyExistsExceptionWhenRegisteringExistingUser() {
        RegistrationRequest request = new RegistrationRequest("admin", "password123");
        assertThrowsExactly(UserAlreadyExistsException.class, () -> {
            userController.register(request);
        });
    }

    @Test
    public void shouldThrowInvalidCredentialsExceptionForInvalidCredentials() {
        RegistrationRequest request = new RegistrationRequest("u", "short");
        assertThrowsExactly(InvalidCredentialsException.class, () -> {
            userController.register(request);
        });
    }

    @Test
    public void shouldReturnAllUsers() {
        Response response = userController.all();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        UserResponseList userResponseList = (UserResponseList) response.getEntity();
        assertNotNull(userResponseList);
        List<UserResponse> users = userResponseList.users;
        assertEquals(2, users.size());
        assertEquals("admin", users.get(0).username);
        assertEquals("user", users.get(1).username);
    }

    @Test
    public void shouldReturnUserWhenGettingExistingUser() {
        Response response = userController.one("admin");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        UserResponse userResponse = (UserResponse) response.getEntity();
        assertNotNull(userResponse);
        assertEquals("admin", userResponse.username);
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenGettingNonExistentUser() {
        assertThrowsExactly(UserNotFoundException.class, () -> {
            this.userController.one("nonexistentuser");
        });
    }
}
