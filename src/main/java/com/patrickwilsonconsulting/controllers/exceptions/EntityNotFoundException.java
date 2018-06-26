package com.patrickwilsonconsulting.controllers.exceptions;


import com.patrickwilsonconsulting.serializers.ApiObject;

/**
 * Created by pwilson on 9/22/17.
 */
public class EntityNotFoundException extends RuntimeException {
    private Class<? extends ApiObject> entityClazz;
    private String id;

    public EntityNotFoundException(Class<? extends ApiObject> entityClazz, String id) {
        super(String.format("Unable to locate entity with type: '%s' with id: '%s'", entityClazz.getSimpleName(), id));
        this.entityClazz = entityClazz;
        this.id = id;
    }
}
