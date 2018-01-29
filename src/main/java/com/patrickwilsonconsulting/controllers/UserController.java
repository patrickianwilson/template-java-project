package com.patrickwilsonconsulting.controllers;

import com.google.inject.Inject;
import com.patrickwilsonconsulting.controllers.exceptions.ExceptionModel;
import com.patrickwilsonconsulting.controllers.model.User;
import com.patrickwilsonconsulting.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
public class UserController {

    private UserService appService;
    private String baseUrl = "http://localhost:8080";

    @Inject
    public UserController(UserService appService) {
        this.appService = appService;
    }

    @POST
    @ApiOperation("Create New User")
    @Consumes("application/json")
    @Produces("application/json")
    @ApiResponses(
           value = {
                @ApiResponse(code = 201, message = "User Created", response = User.class),
                @ApiResponse(code = 409, message = "User Already Exists", response = ExceptionModel.class),
                @ApiResponse(code = 400, message = "Invalid User Details", response = ExceptionModel.class)
           }
    )
    public Response createUser(User app) throws URISyntaxException {
        User result = appService.createUser(app);
        return Response.created(new URI(baseUrl+"/"+result.getUserId())).entity(result).build();
    }

}
