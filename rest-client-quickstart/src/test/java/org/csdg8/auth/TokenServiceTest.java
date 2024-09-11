package org.csdg8.auth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.csdg8.user.User;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class TokenServiceTest {

    @Inject
    TokenService tokenService;

    @Test
    public void testStoreRefreshToken() {
        String username = "testUser";
        String refreshToken = "testRefreshToken";

        tokenService.storeRefreshToken(username, refreshToken);

        assertTrue(tokenService.isValidRefreshToken(username, refreshToken));
    }

    @Test
    public void testIsValidRefreshToken() {
        String username = "testUser";
        String refreshToken = "testRefreshToken";

        tokenService.storeRefreshToken(username, refreshToken);

        assertTrue(tokenService.isValidRefreshToken(username, refreshToken), "Refresh token should be valid");
        assertFalse(tokenService.isValidRefreshToken(username, "invalidToken"),
                "Invalid refresh token should not be valid");
        assertFalse(tokenService.isValidRefreshToken("invalidUser", refreshToken),
                "Refresh token for invalid user should not be valid");
    }

    @Test
    public void testRevokeRefreshToken() {
        String username = "testUser";
        String refreshToken = "testRefreshToken";

        tokenService.storeRefreshToken(username, refreshToken);
        assertTrue(tokenService.isValidRefreshToken(username, refreshToken),
                "Refresh token should be valid before revoking");

        tokenService.revokeRefreshToken(username);
        assertFalse(tokenService.isValidRefreshToken(username, refreshToken),
                "Refresh token should not be valid after revoking");
    }

    @Test
    public void testGenerateAccessToken() {
        User user = new User();
        user.username = "testUser";
        user.role = Set.of("user");

        String accessToken = tokenService.generateAccessToken(user);

        assertNotNull(accessToken);
        assertFalse(accessToken.isEmpty());
    }
}
