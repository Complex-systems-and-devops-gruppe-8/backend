package org.csdg8.security.authentication;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import org.csdg8.security.jpa.user.User;
import org.csdg8.security.jpa.user.UserService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {

    @Inject
    TokenService tokenService;

    @Inject
    UserService userService;

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    // Access Token (short-lived)
    public String createAccessToken(String username, String password) {
        Optional<User> user = this.userService.validateUser(username, password);
        if (user.isEmpty()) {
            throw new UnauthorizedException("Invalid credentials");
        }

        User presentUser = user.get();

        return Jwt.issuer(this.issuer)
                .upn(presentUser.username)
                .groups(presentUser.role)
                .expiresIn(Duration.ofMinutes(5))
                .sign();
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
        if (!tokenService.isValidRefreshToken(username, refreshToken)) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        return Jwt.issuer(issuer)
                .upn(username)
                .groups(userService.getUserRole(username).get())
                .expiresIn(Duration.ofMinutes(5))
                .sign();
    }
}
