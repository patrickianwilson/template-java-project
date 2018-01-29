package com.patrickwilsonconsulting.builders;

import com.google.cloud.datastore.Key;
import com.google.inject.Inject;
import com.patrickwilson.lib.patterns.service.beans.BeanEntityBuilder;
import com.patrickwilsonconsulting.controllers.model.User;
import com.patrickwilsonconsulting.repositories.UserRepository;
import com.patrickwilsonconsulting.repositories.model.KeyValuePair;
import com.patrickwilsonconsulting.repositories.model.UserEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pwilson on 11/4/17.
 */
public class UserBuilder extends BeanEntityBuilder<User, UserEntity> {

    private UserRepository repository;

    @Inject
    public UserBuilder(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void defineProperties(PropertyCopier propertyCopier) {
        propertyCopier.copyProperty("name");
    }

    @Override
    protected UserEntity newEntity() {
        return new UserEntity();
    }

    @Override
    protected User newApiObject() {
        return new User();
    }

    @Override
    protected void extractApiProperties(User source, UserEntity target) {
        super.extractApiProperties(source, target);
        target.setId(repository.buildKey(source.getUserId()));

        if (source.getSampleMap() != null) {
            List<KeyValuePair> configList = new ArrayList<>(source.getSampleMap().size());
            for (Map.Entry<String, String> entry: source.getSampleMap().entrySet()) {
                configList.add(new KeyValuePair(entry.getKey(), entry.getValue()));
            }
            target.setSampleMap(configList);
        }


    }

    @Override
    protected void extractEntityProperties(UserEntity source, User target) {
        super.extractEntityProperties(source, target);
        target.setUserId(((Key)(source.getId().getKey())).getName());
        
        if (source.getSampleMap() != null) {
            Map<String, String> configMap = new HashMap<>();
            for (KeyValuePair kvp: source.getSampleMap()) {
                configMap.put(kvp.getKey(), kvp.getValue());
            }

            target.setSampleMap(configMap);
        }

    }
}
