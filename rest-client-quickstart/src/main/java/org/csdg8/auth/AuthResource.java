package org.csdg8.auth;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthResource {

    @Inject
    AuthService authService;

    @POST
    @Operation(summary = "Authenticate user and generate tokens", description = "Validates user credentials and, if successful, generates an access token and a refresh token. ")
    @APIResponse(responseCode = "200", description = "Successful login, returns a new access and refresh token")
    @APIResponse(responseCode = "401", description = "Invalid credentials")
    @Path("/token")
    @Consumes(MediaType.APPLICATION_JSON)
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

    @POST
    @Operation(summary = "Refresh access token using a valid refresh token", description = "Validates the provided refresh token and, if valid, generates a new short-lived access token. The refresh token itself remains unchanged and can be used for future token refresh operations. If the refresh token is invalid, the request is rejected with an unauthorized response.")
    @APIResponse(responseCode = "200", description = "Successful token refresh, returns a new access token")
    @APIResponse(responseCode = "401", description = "Invalid refresh token")
    @RolesAllowed({ "user", "admin" })
    @Path("/token/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
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
