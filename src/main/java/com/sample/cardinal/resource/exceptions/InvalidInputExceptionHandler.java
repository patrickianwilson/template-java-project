package com.sample.cardinal.resource.exceptions;

import com.sample.cardinal.controllers.exceptions.InvalidInputException;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by pwilson on 9/21/17.
 */
@Provider
public class InvalidInputExceptionHandler implements ExceptionMapper<InvalidInputException> {

    public static Response.Status CODE = Response.Status.BAD_REQUEST;

    @Produces("application/json")
    @Override
    public Response toResponse(InvalidInputException exception) {
        ExceptionModel entity = ExceptionModel.builder().code("400").reason(exception.getMessage()).build();
        return Response.status(CODE).entity(entity).build();
    }
}
