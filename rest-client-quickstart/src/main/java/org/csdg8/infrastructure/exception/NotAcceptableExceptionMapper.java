package org.csdg8.infrastructure.exception;

import com.google.code.siren4j.converter.ReflectingConverter;
import com.google.code.siren4j.resource.ErrorMessageResource;

import jakarta.ws.rs.NotAcceptableException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.SneakyThrows;

@Provider
public class NotAcceptableExceptionMapper implements ExceptionMapper<NotAcceptableException>{

    @SneakyThrows
    @Override
    public Response toResponse(NotAcceptableException exception) {
        final int statusCode = Response.Status.NOT_ACCEPTABLE.getStatusCode();
        final ErrorMessageResource emr = new ErrorMessageResource(
                statusCode, statusCode, 
                "Unsupported 'Accept' header. This API requires 'application/vnd.siren+json'.", null , "https://github.com/kevinswiber/siren");

        return Response.status(statusCode)
                .entity(ReflectingConverter.newInstance().toEntity(emr).toString()).build();
    }
    
}
