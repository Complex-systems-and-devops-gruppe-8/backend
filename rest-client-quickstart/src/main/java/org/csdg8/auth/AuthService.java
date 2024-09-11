package org.csdg8.auth;

import java.util.Optional;
import java.util.UUID;

import org.csdg8.user.User;
import org.csdg8.user.UserService;

import io.quarkus.security.UnauthorizedException;
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
            throw new UnauthorizedException("Invalid credentials");
        }

        User presentUser = user.get();

        return this.tokenService.generateAccessToken(presentUser);
    }

    // Refresh Token (long-lived)
    public String createRefreshToken(String username, String password) {
        Optional<User> user = this.userService.validateUser(username, password);
        if (user.isEmpty()) {
            throw new UnauthorizedException("Invalid credentials");
        }

        User presentUser = user.get();

        String refreshToken = UUID.randomUUID().toString();
        this.tokenService.storeRefreshToken(presentUser.username, refreshToken);
        return refreshToken;
    }

    public String refreshAccessToken(String username, String refreshToken) {
        if (!this.tokenService.isValidRefreshToken(username, refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        Optional<User> user = this.userService.findByUsername(username);

        return this.tokenService.generateAccessToken(user.get());
    }
}
