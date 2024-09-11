package org.csdg8.auth;

import org.csdg8.model.exception.InvalidCredentialsException;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AuthExceptionMapper {

    @ServerExceptionMapper(InvalidCredentialsException.class)
    public RestResponse<String> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return RestResponse.status(Response.Status.UNAUTHORIZED, "Invalid credentials");
    }
}