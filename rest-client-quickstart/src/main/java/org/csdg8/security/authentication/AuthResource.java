package org.csdg8.security.authentication;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.csdg8.security.jpa.user.User;
import org.csdg8.security.jpa.user.UserService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AuthResource {

    @Inject
    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @Inject
    UserService userService;

    @Inject
    TokenService tokenService;

    @Inject
    AuthService authService;

    @POST
    @Operation(summary = "Authenticate user and generate tokens", description = "Validates user credentials and, if successful, generates an access token and a refresh token. ")
    @APIResponse(responseCode = "200", description = "Successful login, returns a new access and refresh token")
    @APIResponse(responseCode = "401", description = "Invalid credentials")
    @Path("/token")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createToken(Credentials credentials) {
        Optional<User> user = this.userService.validateUser(credentials.username, credentials.password);
        if (user.isEmpty()) {
            throw new UnauthorizedException("Invalid credentials");
        }

        User presentUser = user.get();

        // Access Token (short-lived)
        String accessToken = Jwt.issuer(this.issuer)
                .upn(presentUser.username)
                .groups(presentUser.role)
                .expiresIn(Duration.ofMinutes(5))
                .sign();

        // Refresh Token (long-lived)
        String refreshToken = UUID.randomUUID().toString();
        this.tokenService.storeRefreshToken(presentUser.username, refreshToken);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
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

    @POST
    @Operation(summary = "Refresh access token using a valid refresh token", description = "Validates the provided refresh token and, if valid, generates a new short-lived access token. The refresh token itself remains unchanged and can be used for future token refresh operations. If the refresh token is invalid, the request is rejected with an unauthorized response.")
    @APIResponse(responseCode = "200", description = "Successful token refresh, returns a new access token")
    @APIResponse(responseCode = "401", description = "Invalid refresh token")
    @Path("/token/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refreshToken(RefreshRequest request) {
        String newAccessToken = this.authService.refreshAccessToken(request.username, request.refreshToken);

        RefreshTokenResponse response = new RefreshTokenResponse(newAccessToken);
        return Response.ok(response).build();
    }

    public static class RefreshRequest {
        public String username;
        public String refreshToken;
    }

    public static class RefreshTokenResponse {
        public String accessToken;

        public RefreshTokenResponse(String accessToken) {
            this.accessToken = accessToken;
        }
    }
}
