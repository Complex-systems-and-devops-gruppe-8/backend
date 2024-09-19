package org.csdg8.user;

import org.csdg8.model.exception.InvalidCredentialsException;
import org.csdg8.model.exception.UserAlreadyExistsException;
import org.csdg8.model.exception.UserNotFoundException;
import org.csdg8.user.dto.RegistrationRequest;
import org.csdg8.user.dto.UserResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class UserResource {

    @Inject
    UserController userController;

    @POST
    @Operation(summary = "Register a new user", description = "Registers a new user with a provided username and password.")
    @APIResponse(responseCode = "201", description = "User registered successfully")
    @APIResponse(responseCode = "400", description = "Invalid username or password format")
    @APIResponse(responseCode = "409", description = "Username already exists")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(RegistrationRequest request) {
        return this.userController.register(request);
    }

    @GET
    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users.")
    @APIResponse(responseCode = "200", description = "Successfully retrieved all users", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserResponse[].class)),
            @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = UserResponse[].class))
    })
    public Response all() {
        return this.userController.all();
    }

    @GET
    @Path("/{username}")
    @Operation(summary = "Get user by username", description = "Retrieves a specific user by their username.")
    @APIResponse(responseCode = "200", description = "Successfully retrieved the user", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UserResponse.class)),
            @Content(mediaType = MediaType.APPLICATION_XML, schema = @Schema(implementation = UserResponse.class))
    })
    @APIResponse(responseCode = "404", description = "User not found")
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
