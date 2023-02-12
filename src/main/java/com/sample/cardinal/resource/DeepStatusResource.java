package com.sample.cardinal.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 * This resource is only called by kubernetes to determine if the container should be put into service.
 * This should not called periodically once in service (the /status endpoint should be used for that).
 * Ensure the promotionConfig.properties file specifies this as the deep status endpoint.
 */
@Provider
@Path("/deep-status")
public class DeepStatusResource {

    @GET
    public Response amIReady() {


        return Response.ok("This container is just super!").build();

    }
}