package com.sample.cardinal.resource.view;

import com.google.common.collect.ImmutableMap;
import com.inquestdevops.cardinal.ui.Template;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Map;

/**
 * This resource is only called by kubernetes to determine if the container should be put into service.
 * This should not called periodically once in service (the /status endpoint should be used for that).
 * Ensure the promotionConfig.properties file specifies this as the deep status endpoint.
 */
@Provider
@Path("/")
public class HomePageResource {

    @GET
    @Template("home")
    public Map<String,String> home() {
        return ImmutableMap.of("message", "Hello World!");

    }
}