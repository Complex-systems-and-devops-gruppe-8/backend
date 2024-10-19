package org.csdg8.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * <p>
 * Courtesy of gwenneg
 * {@link https://github.com/gwenneg/notifications-backend/blob/93deedc5345fc60c289706ab4ea4e418de2b2235/backend/src/main/java/com/redhat/cloud/notifications/routers/exception/ConstraintViolationExceptionMapper.java}
 * </p>
 * 
 * <p>
 * This code is adapted from an Apache 2.0 licensed project.
 * </p>
 * 
 * <p>
 * Original javadoc:
 * "Overrides the default exception mapper for the {@link ConstraintViolation}
 * exceptions. The reason is that if any method which isn't a top level handler
 * gets annotated with a {@link jakarta.validation.Valid} annotation, and the
 * incoming object is not valid, for some reason instead of capturing that
 * {@link ConstraintViolation} and returning a
 * {@link jakarta.ws.rs.BadRequestException},
 * an {@link jakarta.ws.rs.InternalServerErrorException} is returned with a
 * {@link HttpStatus#SC_INTERNAL_SERVER_ERROR} status code. This exception
 * mapper avoids that and returns the proper response for any
 * {@link ConstraintViolation}
 * that gets thrown in the code."
 * </p>
 * 
 * <p>
 * To use this functionality, annotate request class fields with
 * {@link jakarta.validation.constraints.NotNull}, etc.
 * and when these properties must hold (for example in a controller class)
 * annotate the parameter with {@link jakarta.validation.Valid}.
 * In the case that the constraint-violations do not hold a
 * {@link ConstraintViolationException} is thrown, caught here
 * and a response is returned outlining all the fields which violated the
 * requirements.
 * </p>
 * 
 * <pre>
 * //DTO request object
 * import jakarta.validation.constraints.NotBlank;
 * 
 * public class CreateTokenRequest {
 *
 *     {@code @NotBlank}
 *     public String username;
 *
 *     {@code @NotBlank}
 *     public String password;
 * }
 * 
 * //Controller class
 * import jakarta.validation.Valid;
 *
 * {@code @ApplicationScoped}
 * public class SomeController {
 *
 * public Response createToken({@code @Valid} CreateTokenRequest request) {
 *     ...
 * }
 * 
 * </pre>
 */
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(final ConstraintViolationException e) {
        List<MediaType> acceptableMediaTypes = headers.getAcceptableMediaTypes();
        MediaType responseType = determineResponseType(acceptableMediaTypes);

        ConstraintViolationResponse responseBody = new ConstraintViolationResponse();
        responseBody.title = "Constraint Violation";
        responseBody.description = "The submitted payload is incorrect";

        List<ConstraintViolationDetail> violations = e.getConstraintViolations().stream()
                .map(constraintViolation -> {
                    ConstraintViolationDetail violation = new ConstraintViolationDetail();
                    violation.field = constraintViolation.getPropertyPath().toString();
                    violation.message = constraintViolation.getMessage();
                    return violation;
                })
                .collect(Collectors.toList());

        responseBody.violations = violations;

        return Response.status(Response.Status.BAD_REQUEST)
                .type(responseType)
                .entity(responseBody)
                .build();
    }

    private MediaType determineResponseType(List<MediaType> acceptableMediaTypes) {
        return MediaType.APPLICATION_JSON_TYPE;
    }
}
