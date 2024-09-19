package org.csdg8.auth;

import org.csdg8.auth.dto.CreateTokenRequest;
import org.csdg8.auth.dto.CreateTokenResponse;
import org.csdg8.auth.dto.RefreshAccessTokenRequest;
import org.csdg8.auth.dto.RefreshAccessTokenResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class AuthController {

    @Inject
    AuthService authService;

    public Response createToken(CreateTokenRequest credentials) {
        String accessToken = this.authService.createAccessToken(credentials.username, credentials.password);
        String refreshToken = this.authService.createRefreshToken(credentials.username, credentials.password);

        CreateTokenResponse response = new CreateTokenResponse(accessToken, refreshToken);
        return Response.ok(response).build();
    }

    public Response refreshAccessToken(RefreshAccessTokenRequest request) {
        String newAccessToken = this.authService.refreshAccessToken(request.refreshToken, request.accessToken);

        RefreshAccessTokenResponse response = new RefreshAccessTokenResponse(newAccessToken);
        return Response.ok(response).build();
    }
}
