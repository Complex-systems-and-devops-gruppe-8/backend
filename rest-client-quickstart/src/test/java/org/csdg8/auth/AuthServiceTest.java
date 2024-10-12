package org.csdg8.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.csdg8.model.exception.InvalidCredentialsException;
import org.csdg8.user.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
public class AuthServiceTest {

    @Inject
    AuthService authService;

    @BeforeAll
    @Transactional
    public static void setup() {
        User.add("admin", "admin", Set.of("admin"));
        User.add("user", "user", Set.of("user"));
    }

    @AfterAll
    @Transactional
    public static void teardown() {
        User.deleteAll();
    }

    @Test
    public void shouldCreateAccessTokenWhenValidAdminCredentialsProvided() {
        String username = "admin";
        String password = "admin";

        String accessToken = authService.createAccessToken(username, password);
        assertNotNull(accessToken, "Access token should not be null for valid admin credentials");
    }

    @Test
    public void shouldCreateAccessTokenWhenValidUserCredentialsProvided() {
        String username = "user";
        String password = "user";

        String accessToken = authService.createAccessToken(username, password);
        assertNotNull(accessToken, "Access token should not be null for valid user credentials");
    }

    @Test
    public void shouldThrowExceptionWhenInvalidCredentialsProvidedForAccessToken() {
        String username = "invalidUser";
        String password = "invalidPassword";

        assertThrows(InvalidCredentialsException.class, () -> {
            authService.createAccessToken(username, password);
        }, "Invalid credentials should throw UnauthorizedException");
    }

    @Test
    public void shouldCreateRefreshTokenWhenValidAdminCredentialsProvided() {
        String username = "admin";
        String password = "admin";

        String refreshToken = authService.createRefreshToken(username, password);
        assertNotNull(refreshToken, "Refresh token should not be null for valid admin credentials");
    }

    @Test
    public void shouldCreateRefreshTokenWhenValidUserCredentialsProvided() {
        String username = "user";
        String password = "user";

        String refreshToken = authService.createRefreshToken(username, password);
        assertNotNull(refreshToken, "Refresh token should not be null for valid user credentials");
    }

    @Test
    public void shouldThrowExceptionWhenInvalidCredentialsProvidedForRefreshToken() {
        String username = "invalidUser";
        String password = "invalidPassword";

        assertThrows(InvalidCredentialsException.class, () -> {
            authService.createRefreshToken(username, password);
        }, "Invalid credentials should throw UnauthorizedException");
    }

    @Test
    public void shouldRefreshAccessTokenWhenValidAdminRefreshTokenProvided() {
        String username = "admin";
        String password = "admin";

        String refreshToken = authService.createRefreshToken(username, password);
        assertNotNull(refreshToken, "Refresh token should not be null for valid admin credentials");

        String accessToken = authService.createAccessToken(username, password);

        String newAccessToken = authService.refreshAccessToken(refreshToken, accessToken);
        assertNotNull(newAccessToken, "New access token should not be null for valid refresh token");
    }

    @Test
    public void shouldRefreshAccessTokenWhenValidUserRefreshTokenProvided() {
        String username = "user";
        String password = "user";

        String refreshToken = authService.createRefreshToken(username, password);
        assertNotNull(refreshToken, "Refresh token should not be null for valid user credentials");

        String accessToken = this.authService.createAccessToken(username, password);

        String newAccessToken = authService.refreshAccessToken(refreshToken, accessToken);
        assertNotNull(newAccessToken, "New access token should not be null for valid refresh token");
    }

    @Test
    public void shouldThrowExceptionWhenInvalidRefreshTokenProvidedForAccessTokenRefresh() {
        String username = "admin";
        String invalidRefreshToken = "invalidRefreshToken";

        assertThrows(InvalidCredentialsException.class, () -> {
            authService.refreshAccessToken(username, invalidRefreshToken);
        }, "Invalid refresh token should throw UnauthorizedException");
    }
}
