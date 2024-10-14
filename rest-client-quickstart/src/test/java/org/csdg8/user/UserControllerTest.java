package org.csdg8.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.csdg8.model.exception.UserAlreadyExistsException;
import org.csdg8.model.exception.UserNotFoundException;
import org.csdg8.user.dto.CreateUserRequest;
import org.csdg8.user.dto.GetUserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.converter.ReflectingConverter;
import com.google.code.siren4j.converter.ResourceConverter;
import com.google.code.siren4j.error.Siren4JException;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;

@QuarkusTest
public class UserControllerTest {

    @Inject
    UserController userController;

    private Long adminId;
    @SuppressWarnings("unused")
    private Long userId;

    @BeforeEach
    @Transactional
    public void setup() {
        this.adminId = User.add("admin", "admin1234", Set.of("admin"));
        this.userId = User.add("user", "user1234", Set.of("user"));
    }

    @AfterEach
    @Transactional
    public void teardown() {
        User.deleteAll();
    }

    @Test
    public void shouldRegisterUserAndReturnCreatedResponse() {
        CreateUserRequest request = new CreateUserRequest("john", "password123");
        Response response = userController.createUser(request);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getLocation());
    }

    @Test
    public void shouldThrowUserAlreadyExistsExceptionWhenRegisteringExistingUser() {
        CreateUserRequest request = new CreateUserRequest("admin", "password123");
        assertThrowsExactly(UserAlreadyExistsException.class, () -> {
            userController.createUser(request);
        });
    }

    @Test
    public void shouldThrowConstraintViolationException() {
        CreateUserRequest request = new CreateUserRequest("u", "short");
        assertThrowsExactly(ConstraintViolationException.class, () -> {
            userController.createUser(request);
        });
    }

    @Test
    public void shouldReturnAllUsers() throws Siren4JException {
        List<Entity> userEntities = this.userController.getUsers().getEntities();

        ResourceConverter resourceConverter = ReflectingConverter.newInstance();
        List<GetUserResponse> users = userEntities.stream()
                .map(entity -> (GetUserResponse) resourceConverter.toObject(entity, GetUserResponse.class))
                .collect(Collectors.toList());

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(user -> "admin".equals(user.getUsername())));
        assertTrue(users.stream().anyMatch(user -> "user".equals(user.getUsername())));
    }

    @Test
    public void shouldReturnUserWhenGettingExistingUser() throws Siren4JException {
        Entity userEntity = userController.getUser(adminId);

        ResourceConverter resourceConverter = ReflectingConverter.newInstance();
        GetUserResponse user = (GetUserResponse) resourceConverter.toObject(userEntity, GetUserResponse.class);

        assertEquals("admin", user.getUsername());
    }

    @Test
    public void shouldThrowUserNotFoundExceptionWhenGettingNonExistentUser() {
        assertThrowsExactly(UserNotFoundException.class, () -> {
            this.userController.getUser(9999999L);
        });
    }
}
