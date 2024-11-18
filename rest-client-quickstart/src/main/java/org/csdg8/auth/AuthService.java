package org.csdg8.auth;

import java.util.Optional;

import org.csdg8.model.exception.InvalidCredentialsException;
import org.csdg8.user.User;
import org.csdg8.user.UserService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {

    @Inject
    TokenService tokenService;

    @Inject
    UserService userService;

    // Access Token (short-lived)
    public String createAccessToken(String username, String password) {
        Optional<User> user = this.userService.validateUser(username, password);
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials, user not found when creating access token");
        }

        User presentUser = user.get();
        return this.tokenService.generateAccessToken(presentUser);
    }

    // Refresh Token (long-lived)
    public String createRefreshToken(String username, String password) {
        Optional<User> user = this.userService.validateUser(username, password);
        if (user.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials, user not found when creating refresh token");
        }

        User presentUser = user.get();

        String refreshToken = this.tokenService.generateRefreshToken();
        this.tokenService.storeRefreshToken(presentUser.getUsername(), refreshToken);
        return refreshToken;
    }

    public String refreshAccessToken(String refreshToken, String prevAccessToken) {
        Optional<String> username = this.tokenService.getUsername(prevAccessToken);
        if (username.isEmpty()) {
            throw new InvalidCredentialsException("Invalid credentials, username not found when refreshing access token");
        }
        if (!this.tokenService.isValidRefreshToken(username.get(), refreshToken)) {
            throw new InvalidCredentialsException("Invalid credentials, refresh token was invalid");
        }

        Optional<User> user = this.userService.findByUsername(username.get());
        return this.tokenService.generateAccessToken(user.get());
    }
}
