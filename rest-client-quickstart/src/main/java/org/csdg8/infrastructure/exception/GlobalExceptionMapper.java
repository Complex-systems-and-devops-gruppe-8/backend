package org.csdg8.infrastructure.exception;

import com.google.code.siren4j.Siren4J;
import com.google.code.siren4j.converter.ReflectingConverter;
import com.google.code.siren4j.resource.ErrorMessageResource;

import io.quarkus.logging.Log;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.SneakyThrows;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @SneakyThrows
    @Override
    public Response toResponse(Exception exception) {
        Log.error(exception);
        final int statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        final ErrorMessageResource emr = new ErrorMessageResource(
                statusCode, statusCode, 
                "The server encountered an unexpected issue");

        return Response.status(statusCode)
                .entity(ReflectingConverter.newInstance().toEntity(emr).toString())
                .type(Siren4J.JSON_MEDIATYPE)
                .build();
    }
}
