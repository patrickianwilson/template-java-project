package com.sample.cardinal.controllers;

import com.google.inject.Inject;
import com.sample.cardinal.builders.UserBuilder;
import com.sample.cardinal.resource.model.User;
import com.sample.cardinal.repositories.UserRepository;
import com.sample.cardinal.repositories.model.UserEntity;
import com.sample.cardinal.controllers.exceptions.EntityAlreadyExistsException;
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
