package com.sample.cardinal.resource;

import com.google.inject.Inject;
import com.sample.cardinal.resource.exceptions.ExceptionModel;
import com.sample.cardinal.resource.model.User;
import com.sample.cardinal.controllers.UserController;
import io.swagger.annotations.*;

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
    @ApiOperation(value = "CreateUser",
            authorizations = {@Authorization("basicAuth")})
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
