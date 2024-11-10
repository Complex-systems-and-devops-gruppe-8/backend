package org.csdg8.auth;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.csdg8.user.User;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TokenService {

    @Inject
    JWTParser parser;

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    private Map<String, String> refreshTokens = new HashMap<>();

    public void storeRefreshToken(String username, String refreshToken) {
        if (username == null) {
            throw new NullPointerException("Username cannot be null");
        }

        if (username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be whitespace or empty");
        }

        if (refreshToken == null) {
            throw new NullPointerException("Refresh token cannot be null");
        }

        if (refreshToken.isBlank()) {
            throw new IllegalArgumentException("Refresh token cannot be whitespace or empty");
        }

        refreshTokens.put(username, refreshToken);
    }

    public boolean isValidRefreshToken(String username, String refreshToken) {
        return refreshTokens.containsKey(username) && refreshTokens.get(username).equals(refreshToken);
    }

    public void revokeRefreshToken(String username) {
        if (username == null) {
            throw new NullPointerException("Username cannot be null");
        }
        refreshTokens.remove(username);
    }

    public String generateAccessToken(User presentUser) {
        return Jwt.issuer(this.issuer)
                .upn(presentUser.getUsername())
                .subject(presentUser.id.toString())
                .groups(presentUser.getRole())
                .expiresIn(Duration.ofMinutes(5))
                .sign();
    }

    public Optional<String> getUsername(String accessToken) {
        try {
            String username = parser.parse(accessToken).getClaim("upn");
            return Optional.of(username);
        } catch (ParseException e) {
            return Optional.empty();
        }
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }
}
