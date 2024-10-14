package org.csdg8.auth;

import org.csdg8.auth.dto.RefreshAccessTokenRequest;
import org.csdg8.auth.dto.CreateTokenRequest;
import org.csdg8.model.exception.InvalidCredentialsException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import com.google.code.siren4j.component.Entity;
import com.google.code.siren4j.error.Siren4JException;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces({ MediaType.APPLICATION_JSON })
public class AuthResource {

    @Inject
    AuthController authController;

    @POST
    @Operation(summary = "Authenticate user and generate tokens", description = "Validates user credentials and, if successful, generates an access token and a refresh token. ")
    @APIResponse(responseCode = "200", description = "Successful login, returns a new access and refresh token")
    @APIResponse(responseCode = "401", description = "Invalid credentials")
    @Path("/token")
    @Consumes(MediaType.APPLICATION_JSON)
    public Entity createToken(CreateTokenRequest credentials) throws Siren4JException {
       return this.authController.createToken(credentials);
    }

    @POST
    @Operation(summary = "Refresh access token using a valid refresh token", description = "Validates the provided refresh token and, if valid, generates a new short-lived access token. The refresh token itself remains unchanged and can be used for future token refresh operations. If the refresh token is invalid, the request is rejected with an unauthorized response.")
    @APIResponse(responseCode = "200", description = "Successful token refresh, returns a new access token")
    @APIResponse(responseCode = "401", description = "Invalid refresh token")
    @RolesAllowed({ "user", "admin" })
    @Path("/token/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    public Entity refreshAccessToken(RefreshAccessTokenRequest request) throws Siren4JException {
        return this.authController.refreshAccessToken(request);
    }

    @ServerExceptionMapper(InvalidCredentialsException.class)
    public RestResponse<String> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return RestResponse.status(Response.Status.UNAUTHORIZED, "Invalid credentials");
    }
}
