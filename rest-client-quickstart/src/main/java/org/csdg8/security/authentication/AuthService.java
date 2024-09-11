package org.csdg8.security.authentication;

import java.time.Duration;

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
