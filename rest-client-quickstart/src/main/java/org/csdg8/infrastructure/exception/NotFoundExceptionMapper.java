package org.csdg8.infrastructure.exception;

import com.google.code.siren4j.converter.ReflectingConverter;
import com.google.code.siren4j.resource.ErrorMessageResource;

import io.quarkus.logging.Log;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.SneakyThrows;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @SneakyThrows
    @Override
    public Response toResponse(NotFoundException exception) {
        Log.warn(exception);
        final int statusCode = Response.Status.NOT_FOUND.getStatusCode();
        final ErrorMessageResource emr = new ErrorMessageResource(
                statusCode, statusCode,
                "The requested resource was not found.");

        return Response.status(statusCode)
                .entity(ReflectingConverter.newInstance().toEntity(emr).toString()).build();
    }
}
