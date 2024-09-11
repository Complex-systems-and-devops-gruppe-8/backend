package org.csdg8.auth;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class AuthServiceTest {

    @Inject
    AuthService authService;

    @Test
    public void testCreateAccessTokenWithValidAdminCredentials() {
        String username = "admin";
        String password = "admin";

        String accessToken = authService.createAccessToken(username, password);
        assertNotNull(accessToken, "Access token should not be null for valid admin credentials");
    }

    @Test
    public void testCreateAccessTokenWithValidUserCredentials() {
        String username = "user";
        String password = "user";

        String accessToken = authService.createAccessToken(username, password);
        assertNotNull(accessToken, "Access token should not be null for valid user credentials");
    }

    @Test
    public void testCreateAccessTokenWithInvalidCredentials() {
        String username = "invalidUser";
        String password = "invalidPassword";

        assertThrows(UnauthorizedException.class, () -> {
            authService.createAccessToken(username, password);
        }, "Invalid credentials should throw UnauthorizedException");
    }

    @Test
    public void testCreateRefreshTokenWithValidAdminCredentials() {
        String username = "admin";
        String password = "admin";

        String refreshToken = authService.createRefreshToken(username, password);
        assertNotNull(refreshToken, "Refresh token should not be null for valid admin credentials");
    }

    @Test
    public void testCreateRefreshTokenWithValidUserCredentials() {
        String username = "user";
        String password = "user";

        String refreshToken = authService.createRefreshToken(username, password);
        assertNotNull(refreshToken, "Refresh token should not be null for valid user credentials");
    }

    @Test
    public void testCreateRefreshTokenWithInvalidCredentials() {
        String username = "invalidUser";
        String password = "invalidPassword";

        assertThrows(UnauthorizedException.class, () -> {
            authService.createRefreshToken(username, password);
        }, "Invalid credentials should throw UnauthorizedException");
    }

    @Test
    public void testRefreshAccessTokenWithValidAdminRefreshToken() {
        String username = "admin";
        String password = "admin";

        String refreshToken = authService.createRefreshToken(username, password);
        assertNotNull(refreshToken, "Refresh token should not be null for valid admin credentials");

        String accessToken = authService.refreshAccessToken(username, refreshToken);
        assertNotNull(accessToken, "Access token should not be null for valid refresh token");
    }

    @Test
    public void testRefreshAccessTokenWithValidUserRefreshToken() {
        String username = "user";
        String password = "user";

        String refreshToken = authService.createRefreshToken(username, password);
        assertNotNull(refreshToken, "Refresh token should not be null for valid user credentials");

        String accessToken = authService.refreshAccessToken(username, refreshToken);
        assertNotNull(accessToken, "Access token should not be null for valid refresh token");
    }

    @Test
    public void testRefreshAccessTokenWithInvalidRefreshToken() {
        String username = "admin";
        String invalidRefreshToken = "invalidRefreshToken";

        assertThrows(UnauthorizedException.class, () -> {
            authService.refreshAccessToken(username, invalidRefreshToken);
        }, "Invalid refresh token should throw UnauthorizedException");
    }
}
