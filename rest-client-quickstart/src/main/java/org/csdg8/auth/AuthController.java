package org.csdg8.auth;

import org.csdg8.auth.dto.CreateTokenRequest;
import org.csdg8.auth.dto.CreateTokenResponse;
import org.csdg8.auth.dto.RefreshAccessTokenRequest;
import org.csdg8.auth.dto.RefreshAccessTokenResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class AuthController {

    @Inject
    AuthService authService;

    public Response createToken(@Valid @NotNull CreateTokenRequest request) {
        String accessToken = this.authService.createAccessToken(request.username, request.password);
        String refreshToken = this.authService.createRefreshToken(request.username, request.password);

        CreateTokenResponse response = new CreateTokenResponse(accessToken, refreshToken);
        return Response.ok(response).build();
    }

    public Response refreshAccessToken(@Valid @NotNull RefreshAccessTokenRequest request) {
        String newAccessToken = this.authService.refreshAccessToken(request.refreshToken, request.accessToken);

        RefreshAccessTokenResponse response = new RefreshAccessTokenResponse(newAccessToken);
        return Response.ok(response).build();
    }
}
