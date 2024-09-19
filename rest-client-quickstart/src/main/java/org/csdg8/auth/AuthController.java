package org.csdg8.auth;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class AuthController {

    @Inject
    AuthService authService;

    public Response createToken(Credentials credentials) {
        String accessToken = this.authService.createAccessToken(credentials.username, credentials.password);
        String refreshToken = this.authService.createRefreshToken(credentials.username, credentials.password);

        CreateTokenResponse response = new CreateTokenResponse(accessToken, refreshToken);
        return Response.ok(response).build();
    }

    public static class Credentials {
        public String username;
        public String password;

        public Credentials(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public static class CreateTokenResponse {
        public String accessToken;
        public String refreshToken;

        public CreateTokenResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    public Response refreshAccessToken(RefreshAccessTokenRequest request) {
        String newAccessToken = this.authService.refreshAccessToken(request.refreshToken, request.accessToken);

        RefreshAccessTokenResponse response = new RefreshAccessTokenResponse(newAccessToken);
        return Response.ok(response).build();
    }

    public static class RefreshAccessTokenRequest {
        public String refreshToken;
        public String accessToken;
    }

    public static class RefreshAccessTokenResponse {
        public String accessToken;

        public RefreshAccessTokenResponse(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
