package com.sample.cardinal.resource;

import com.google.inject.Inject;
import com.sample.cardinal.resource.exceptions.ExceptionModel;
import com.sample.cardinal.resource.model.User;
import com.sample.cardinal.controllers.UserController;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by pwilson on 11/5/17.
 */
@Path("/application")
@Provider
@Api
public class UserResource {

    private UserController appService;
    private String baseUrl = "http://localhost:8080";

    @Inject
    public UserResource(UserController appService) {
        this.appService = appService;
    }

    @POST
    @Operation(summary = "RegisterMajorVersion",
            security = {
                    @SecurityRequirement(name="rabbitOAuth"),
                    @SecurityRequirement(name="basicAuth")
            },
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = User.class)), description = "User Created"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ExceptionModel.class)), description = "Invalid properties on input"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", content = @Content(schema = @Schema(implementation = ExceptionModel.class)), description = "Conflicting Input: The user already exists for this module."),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ExceptionModel.class)), description = "Internal Server Error")
            }
    )
    @Consumes("application/json")
    @Produces("application/json")
    public Response createUser(User app) throws URISyntaxException {
        User result = appService.createUser(app);
        return Response.created(new URI(baseUrl+"/"+result.getUserId())).entity(result).build();
    }

}
