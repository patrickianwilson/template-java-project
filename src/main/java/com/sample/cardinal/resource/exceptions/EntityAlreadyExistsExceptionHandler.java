package com.sample.cardinal.resource.exceptions;


import com.sample.cardinal.controllers.exceptions.EntityAlreadyExistsException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by pwilson on 9/22/17.
 */
@Provider
public class EntityAlreadyExistsExceptionHandler implements ExceptionMapper<EntityAlreadyExistsException> {
    public static Response.Status CODE = Response.Status.CONFLICT;
    @Override
    public Response toResponse(EntityAlreadyExistsException exception) {
        ExceptionModel entity = ExceptionModel.builder()
                .reason(exception.getMessage())
                .code("409")
                .build();

        return Response.status(CODE).entity(entity).build();
    }
}
