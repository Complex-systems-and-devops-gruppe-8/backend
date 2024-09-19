package org.csdg8.user;

import org.csdg8.model.exception.InvalidCredentialsException;
import org.csdg8.model.exception.UserAlreadyExistsException;
import org.csdg8.model.exception.UserNotFoundException;
import org.csdg8.user.dto.RegistrationRequest;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class UserResource {

    @Inject
    UserController userController;

    @POST
    @Operation(summary = "Register a new user", description = "Registers a new user with a provided username and password. Upon successful registration, the user is assigned a default role of 'user'.")
    @APIResponse(responseCode = "201", description = "User registered successfully")
    @APIResponse(responseCode = "400", description = "Invalid username or password format")
    @APIResponse(responseCode = "409", description = "Username already exists")
    @APIResponse(responseCode = "500", description = "Server error during registration")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(RegistrationRequest request) {
        return this.userController.register(request);
    }

    @GET
    @Operation()
    @APIResponse()
    public Response all() {
        return this.userController.all();
    }

    @GET
    @Operation()
    @APIResponse()
    @Path("/{username}")
    public Response one(String username) {
        return this.userController.one(username);
    }

    @ServerExceptionMapper
    public RestResponse<String> mapExcepion(UserNotFoundException x) {
        return RestResponse.status(Response.Status.NOT_FOUND, "User not found");
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
