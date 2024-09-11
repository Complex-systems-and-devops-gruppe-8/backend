package org.csdg8.user;

import java.util.Set;

import org.csdg8.model.exception.UserAlreadyExistsException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserResource {

    @Inject
    UserService userService;

    @POST
    @Operation(summary = "Register a new user", description = "Registers a new user with a provided username and password. Upon successful registration, the user is assigned a default role of 'user'.")
    @APIResponse(responseCode = "201", description = "User registered successfully")
    @APIResponse(responseCode = "400", description = "Invalid username or password format")
    @APIResponse(responseCode = "409", description = "Username already exists")
    @APIResponse(responseCode = "500", description = "Server error during registration")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(RegistrationRequest request) {

        if (!isValidUsername(request.username) || !isValidPassword(request.password)) {
            throw new BadRequestException("Invalid username or password format");
        }

        if (this.userService.findByUsername(request.username).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        userService.addUser(request.username, request.password, Set.of("user"));

        return Response.status(Response.Status.CREATED)
                .entity("User registered successfully")
                .build();
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

    @ServerExceptionMapper
    public RestResponse<String> mapException(UserAlreadyExistsException x) {
        return RestResponse.status(Response.Status.CONFLICT, "Username already exists");
    }
}
