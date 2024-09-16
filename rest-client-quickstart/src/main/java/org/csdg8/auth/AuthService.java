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
            throw new InvalidCredentialsException();
        }

        User presentUser = user.get();
        return this.tokenService.generateAccessToken(presentUser);
    }

    // Refresh Token (long-lived)
    public String createRefreshToken(String username, String password) {
        Optional<User> user = this.userService.validateUser(username, password);
        if (user.isEmpty()) {
            throw new InvalidCredentialsException();
        }

        User presentUser = user.get();

        String refreshToken = this.tokenService.generateRefreshToken();
        this.tokenService.storeRefreshToken(presentUser.username, refreshToken);
        return refreshToken;
    }

    public String refreshAccessToken(String refreshToken, String prevAccessToken) {
        Optional<String> username = this.tokenService.getUsername(prevAccessToken);
        if (username.isEmpty()) {
            throw new InvalidCredentialsException();
        }
        if (!this.tokenService.isValidRefreshToken(username.get(), refreshToken)) {
            throw new InvalidCredentialsException();
        }

        Optional<User> user = this.userService.findByUsername(username.get());
        return this.tokenService.generateAccessToken(user.get());
    }
}
