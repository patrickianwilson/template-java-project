package com.sample.cardinal.resource;

import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * Created by pwilson on 11/4/17.
 */
@Path("/status")
@Provider
@Api
public class StatusResource {

    @GET
    public Response amIUp() {
        return Response.ok("Excellante!").build();
    }
}
