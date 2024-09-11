package org.csdg8.security;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.csdg8.security.jpa.User;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

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
    RefreshTokenService refreshTokenService;

    @POST
    @Operation(summary = "Authenticate user and generate tokens", description = "Validates user credentials and, if successful, generates an access token and a refresh token. ")
    @APIResponse(responseCode = "200", description = "Successful login, returns a new access and refresh token")
    @APIResponse(responseCode = "401", description = "Invalid credentials")
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(Credentials credentials) {

        Optional<User> user = this.userService.validateUser(credentials.username, credentials.password);
        if (user.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }

        User foundUser = user.get();

        // Access Token (short-lived)
        String accessToken = Jwt.issuer(this.issuer)
                .upn(foundUser.username)
                .groups(foundUser.role)
                .expiresIn(Duration.ofMinutes(5))
                .sign();

        // Refresh Token (long-lived)
        String refreshToken = UUID.randomUUID().toString();
        this.refreshTokenService.storeRefreshToken(foundUser.username, refreshToken);

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
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refreshToken(RefreshRequest request) {
        if (!this.refreshTokenService.isValidRefreshToken(request.username, request.refreshToken)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid refresh token").build();
        }

        String newAccessToken = Jwt.issuer(this.issuer)
                .upn(request.username)
                .groups(this.userService.getUserRole(request.username).get())
                .expiresIn(Duration.ofMinutes(5))
                .sign();

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        return Response.ok(response).build();
    }

    public static class RefreshRequest {
        public String username;
        public String refreshToken;
    }

    @POST
    @Operation(summary = "Register a new user", description = "Registers a new user with a provided username and password. Upon successful registration, the user is assigned a default role of 'user'.")
    @APIResponse(responseCode = "201", description = "User registered successfully")
    @APIResponse(responseCode = "400", description = "Invalid username or password format")
    @APIResponse(responseCode = "409", description = "Username already exists")
    @APIResponse(responseCode = "500", description = "Server error during registration")
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(RegistrationRequest request) {

        if (!isValidUsername(request.username) || !isValidPassword(request.password)) {
            // TODO throw error instead and handle error types with ServerExceptionMappers
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid username or password format")
                    .build();
        }

        if (this.userService.findByUsername(request.username).isPresent()) {
            // TODO throw error instead and handle error types with ServerExceptionMappers
            return Response.status(Response.Status.CONFLICT)
                    .entity("Username already exists")
                    .build();
        }

        try {
            userService.addUser(request.username, request.password, Set.of("user"));

            return Response.status(Response.Status.CREATED)
                    .entity("User registered successfully")
                    .build();
        } catch (Exception e) {
            // TODO throw error instead and handle error types with ServerExceptionMappers
            return Response.serverError()
                    .entity("Error occurred during registration")
                    .build();
        }
    }

    public static class RegistrationRequest {
        public String username;
        public String password;

        public RegistrationRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    // TODO username validation logic to more suitable class
    private boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    // TODO password validation logic to more suitable class
    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }
}
