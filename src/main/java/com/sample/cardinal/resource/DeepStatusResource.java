package com.sample.cardinal.resource;


import com.google.inject.Inject;
import com.sample.cardinal.repositories.UserRepository;
import com.sample.cardinal.repositories.model.UserEntity;

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

    private UserRepository userRepository;

    @Inject
    public DeepStatusResource(UserRepository userRepo) {
        this.userRepository = userRepo;
    }

    @GET
    public Response amIReady() {
        UserEntity userEntity = UserEntity.builder()
                .email("deep-status@sample.com")
                .id(userRepository.buildKey("deep-status-entity"))
                .name("deep status")
                .build();

        try {
            userRepository.save(userEntity);
        } finally {
            userRepository.delete(userEntity);
        }

        return Response.ok("This container is just super!").build();

    }
}