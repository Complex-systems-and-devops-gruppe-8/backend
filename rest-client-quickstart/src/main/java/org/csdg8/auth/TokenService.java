package org.csdg8.auth;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.csdg8.user.User;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.logging.Log;
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
        assert username != null;
        assert !username.isBlank();

        assert refreshToken != null;
        assert !refreshToken.isBlank();

        refreshTokens.put(username, refreshToken);

        assert refreshTokens.containsKey(username);
    }

    public boolean isValidRefreshToken(String username, String refreshToken) {
        assert username != null;
        assert refreshToken != null;

        return refreshTokens.containsKey(username) && refreshTokens.get(username).equals(refreshToken);
    }

    public void revokeRefreshToken(String username) {
        assert username != null;

        refreshTokens.remove(username);
    }

    public String generateAccessToken(User user) {
        assert user != null;
        assert user.getRole() != null;

        return Jwt.issuer(this.issuer)
                .upn(user.getUsername())
                .subject(user.id.toString())
                .groups(user.getRole())
                .expiresIn(Duration.ofMinutes(5))
                .sign();
    }

    public Optional<String> getUsername(String accessToken) {
        assert accessToken != null;

        try {
            String username = parser.parse(accessToken).getClaim("upn");
            return Optional.of(username);
        } catch (ParseException e) {
            Log.warn("Failed to parse 'upn' from supplied accessToken");
            return Optional.empty();
        }
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }
}
