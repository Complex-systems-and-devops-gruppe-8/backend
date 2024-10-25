package org.csdg8.user;

import org.csdg8.model.exception.InvalidCredentialsException;
import org.csdg8.model.exception.UserAlreadyExistsException;
import org.csdg8.model.exception.UserNotFoundException;
import org.csdg8.user.dto.GetCollectionUserResponse;
import org.csdg8.user.dto.CreateUserRequest;
import org.csdg8.user.dto.GetUserResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import com.google.code.siren4j.Siren4J;
import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.error.Siren4JException;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces(Siren4J.JSON_MEDIATYPE)
public class UserResource {

    @Inject
    UserController userController;

    @POST
    @Operation(summary = "Register a new user", description = "Registers a new user with a provided username and password.")
    @APIResponse(responseCode = "201", description = "User registered successfully")
    @APIResponse(responseCode = "400", description = "Invalid username or password format")
    @APIResponse(responseCode = "409", description = "Username already exists")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(CreateUserRequest request) {
        return this.userController.createUser(request);
    }

    @GET
    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users.")
    @APIResponse(responseCode = "200", description = "Successfully retrieved all users", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GetCollectionUserResponse.class)),
    })
    public Entity getUsers() throws Siren4JException {
        return this.userController.getUsers();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get user by id", description = "Retrieves a specific user by their id.")
    @APIResponse(responseCode = "200", description = "Successfully retrieved the user", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = GetUserResponse.class)),
    })
    @APIResponse(responseCode = "404", description = "User not found")
    public Entity getUser(Long id) throws Siren4JException {
        return this.userController.getUser(id);
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
