package org.acme.hibernate.orm.panache.controller;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.hibernate.orm.panache.exception.BadRequestException;
import org.acme.hibernate.orm.panache.exception.ExternalServiceException;
import org.acme.hibernate.orm.panache.exception.DatabaseException;
import org.acme.hibernate.orm.panache.service.JokeService;

@Path("/jokes")
public class JokeResource {

    @Inject
    JokeService jokeService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Uni<Response> addJokes(@QueryParam("count") @DefaultValue("10") int count) {
        // Validate input parameter 'count'
        if (count <= 0) {
            throw new BadRequestException("Count must be a positive number.");
        }

        // Fetch and store jokes
        return jokeService.fetchAndStoreJokes(count)
                .onItem().transform(jokes -> Response.ok(jokes).status(200).build())
                .onFailure().recoverWithItem(throwable -> {
                    String errorMessage = throwable.getMessage();
                    Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

                    // Handle specific exception cases
                    if (throwable instanceof BadRequestException) {
                        status = Response.Status.BAD_REQUEST;
                    } else if (throwable instanceof ExternalServiceException) {
                        status = Response.Status.SERVICE_UNAVAILABLE;
                        errorMessage = "Joke API is currently unavailable.";
                    } else if (throwable instanceof DatabaseException) {
                        errorMessage = "Database error occurred.";
                    }

                    // Return a structured error message
                    return Response.status(status)
                            .entity("{\"error\": \"" + errorMessage + "\"}")
                            .type(MediaType.APPLICATION_JSON)
                            .build();
                });
    }
}
