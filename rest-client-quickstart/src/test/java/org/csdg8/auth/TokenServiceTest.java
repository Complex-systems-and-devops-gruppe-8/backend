package org.csdg8.auth;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        assertTrue(tokenService.isValidRefreshToken(username, refreshToken), "Refresh token should be valid");
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

        assertNotNull(accessToken, "Generated access token should not be null");
        assertFalse(accessToken.isBlank(), "Generated access token should not be blank or empty");
    }

    @Test
    public void testStoreRefreshTokenWithEmptyUsername() {
        String refreshToken = "testRefreshToken";
        assertThrows(IllegalArgumentException.class, () -> {
            tokenService.storeRefreshToken("", refreshToken);
        }, "Storing a blank username should throw NullPointerException");
    }

    @Test
    public void testStoreRefreshTokenWithNullUsername() {
        String refreshToken = "testRefreshToken";
        assertThrows(NullPointerException.class, () -> {
            tokenService.storeRefreshToken(null, refreshToken);
        }, "Storing null username should throw NullPointerException");
    }

    @Test
    public void testStoreRefreshTokenWithNullToken() {
        String username = "testUser";
        assertThrows(NullPointerException.class, () -> {
            tokenService.storeRefreshToken(username, null);
        }, "Storing a null refresh token should throw NullPointerException");
    }

    @Test
    public void testStoreRefreshTokenWithEmptyToken() {
        String username = "testUser";
        assertThrows(IllegalArgumentException.class, () -> {
            tokenService.storeRefreshToken(username, "");
        }, "Storing a blank refresh token should throw NullPointerException");
    }


    @Test
    public void testOverwriteRefreshToken() {
        String username = "testUser";
        String refreshToken1 = "testRefreshToken1";
        String refreshToken2 = "testRefreshToken2";

        tokenService.storeRefreshToken(username, refreshToken1);
        assertTrue(tokenService.isValidRefreshToken(username, refreshToken1),
                "First refresh token should be valid");

        tokenService.storeRefreshToken(username, refreshToken2);
        assertFalse(tokenService.isValidRefreshToken(username, refreshToken1),
                "First refresh token should no longer be valid after overwriting");
        assertTrue(tokenService.isValidRefreshToken(username, refreshToken2),
                "Second refresh token should be valid");
    }
}
