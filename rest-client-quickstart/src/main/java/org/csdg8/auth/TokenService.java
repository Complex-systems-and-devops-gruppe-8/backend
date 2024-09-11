package org.csdg8.auth;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.csdg8.security.jpa.user.User;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TokenService {

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    private Map<String, String> refreshTokens = new HashMap<>();

    public void storeRefreshToken(String username, String refreshToken) {
        refreshTokens.put(username, refreshToken);
    }

    public boolean isValidRefreshToken(String username, String refreshToken) {
        return refreshTokens.containsKey(username) && refreshTokens.get(username).equals(refreshToken);
    }

    public void revokeRefreshToken(String username) {
        refreshTokens.remove(username);
    }

    public String generateAccessToken(User presentUser) {
        return Jwt.issuer(this.issuer)
                .upn(presentUser.username)
                .groups(presentUser.role)
                .expiresIn(Duration.ofMinutes(5))
                .sign();
    }
}
