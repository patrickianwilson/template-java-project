package com.patrickwilsonconsulting.service;

import com.google.inject.Inject;
import com.patrickwilsonconsulting.builders.UserBuilder;
import com.patrickwilsonconsulting.controllers.model.User;
import com.patrickwilsonconsulting.repositories.UserRepository;
import com.patrickwilsonconsulting.repositories.model.UserEntity;
import com.patrickwilsonconsulting.service.exceptions.EntityAlreadyExistsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by pwilson on 11/5/17.
 */
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Inject))
public class ProductionUserService implements UserService {

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
