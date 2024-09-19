package org.csdg8.user;

import java.util.Set;

import org.csdg8.model.exception.InvalidCredentialsException;
import org.csdg8.model.exception.UserAlreadyExistsException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import jakarta.inject.Inject;
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

        this.userService.addUser(request.username, request.password, Set.of("user"));

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

    @ServerExceptionMapper
    public RestResponse<String> mapException(UserAlreadyExistsException x) {
        return RestResponse.status(Response.Status.CONFLICT, "Username already exists");
    }

    @ServerExceptionMapper
    public RestResponse<String> mapException(InvalidCredentialsException x) {
        return RestResponse.status(Response.Status.BAD_REQUEST, "Invalid username or password format");
    }
}
