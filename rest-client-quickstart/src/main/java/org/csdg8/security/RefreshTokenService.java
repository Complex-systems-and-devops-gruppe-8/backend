package org.csdg8.security;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class RefreshTokenService {

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
}
