package com.patrickwilsonconsulting.controllers;

import com.google.inject.Inject;
import com.patrickwilsonconsulting.builders.UserBuilder;
import com.patrickwilsonconsulting.resource.model.User;
import com.patrickwilsonconsulting.repositories.UserRepository;
import com.patrickwilsonconsulting.repositories.model.UserEntity;
import com.patrickwilsonconsulting.controllers.exceptions.EntityAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by pwilson on 11/5/17.
 */
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Inject))
public class ProductionUserController implements UserController {

    private UserRepository appRepo;
    private UserBuilder appBuilder;


    @Override
    public User createUser(User app) {
        EntityValidatorUtils.validateEntity(app);

        UserEntity entity = appBuilder.toEntity(app);

        if (appRepo.findOne(entity.getId()) != null) {
            throw new EntityAlreadyExistsException(User.class, app.getUserId());
        }

        UserEntity result = appRepo.save(entity);

        return appBuilder.toApiModel(result);
    }

}
