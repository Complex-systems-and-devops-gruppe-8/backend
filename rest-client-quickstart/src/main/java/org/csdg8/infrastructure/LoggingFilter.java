package org.csdg8.infrastructure;

import java.io.IOException;

import io.quarkus.logging.Log;
import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Context
    UriInfo info;

    @Context
    HttpServerRequest request;

    @Override
    public void filter(ContainerRequestContext context) {

        final String method = context.getMethod();
        final String path = info.getPath();
        final String address = request.remoteAddress().toString();

        Log.infof("Request %s %s from IP %s", method, path, address);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        final String method = requestContext.getMethod();
        final String path = info.getPath();
        final Integer status = responseContext.getStatus();
        final Object entity = responseContext.getEntity();

        switch (status) {
            case Integer s when (s >= 100 && s < 400) ->
                log1xx2xx3xxResponse(method, path, status, entity);
            case Integer s when (s >= 400 && s < 500) ->
                log4xxResponse(method, path, status, entity);
            case Integer s when (s >= 500 && s < 600) ->
                log5xxResponse(method, path, status, entity);
            default ->
                logInvalidStatusResponse(method, path, status, entity);
        }
    }

    private void log1xx2xx3xxResponse(String method, String path, Integer status, Object entity) {
        Log.infof("Response %s %s returned status %d with body: %s", method, path, status, entity);
    }

    private void log4xxResponse(String method, String path, Integer status, Object entity) {
        Log.warnf("Response %s %s returned status %d with error: %s", method, path, status, entity);
    }

    private void log5xxResponse(String method, String path, Integer status, Object entity) {
        Log.errorf("Response %s %s returned status %d with error: %s", method, path, status, entity);
    }

    private void logInvalidStatusResponse(String method, String path, Integer status, Object entity) {
        Log.fatalf("Response %s %s returned invalid status %d with error: %s", method, path, status, entity);
    }
}