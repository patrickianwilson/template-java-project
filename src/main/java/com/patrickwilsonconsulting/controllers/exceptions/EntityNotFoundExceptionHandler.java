package com.patrickwilsonconsulting.controllers.exceptions;

import com.patrickwilsonconsulting.service.exceptions.EntityNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by pwilson on 9/22/17.
 */
@Provider
public class EntityNotFoundExceptionHandler implements ExceptionMapper<EntityNotFoundException> {
    public Response.Status CODE = Response.Status.NOT_FOUND;
    @Override
    public Response toResponse(EntityNotFoundException exception) {
        ExceptionModel entity = ExceptionModel.builder()
                .code("404")
                .reason(exception.getMessage())
                .build();

        return Response.status(CODE).entity(entity).build();
    }
}
