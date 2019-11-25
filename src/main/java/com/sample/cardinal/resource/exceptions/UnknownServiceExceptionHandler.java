package com.sample.cardinal.resource.exceptions;

import com.inquestdevops.cardinal.base.model.ExceptionModel;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

@Provider
@Slf4j
public class UnknownServiceExceptionHandler implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        String errId = UUID.randomUUID().toString();
        ExceptionModel entity = ExceptionModel.builder().code("500")
                .reason(String.format("%s.  Please look for error ID %s in the logs.",
                        exception.getMessage(),
                        errId))
                .build();
        log.error("Error: " + errId, exception);
        return Response.status(500).header("X-Error-Id", errId).entity(entity).build();
    }
}
