package org.acme.hibernate.orm.panache.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExternalServiceExceptionHandler implements ExceptionMapper<ExternalServiceException> {
    @Override
    public Response toResponse(ExternalServiceException exception) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("{\"error\": \"" + exception.getMessage() + "\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
