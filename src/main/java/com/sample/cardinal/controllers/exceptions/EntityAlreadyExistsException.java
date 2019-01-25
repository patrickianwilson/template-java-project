package com.sample.cardinal.controllers.exceptions;

import lombok.Getter;

/**
 * Created by pwilson on 9/20/17.
 */
@Getter
public class EntityAlreadyExistsException extends RuntimeException {
    private Class modelClazz;
    private String id;

    public EntityAlreadyExistsException(Class modelClazz, String id) {
        super(String.format("Conflict: A %s with id '%s' already exists!", modelClazz.getSimpleName(), id));
        this.modelClazz = modelClazz;
        this.id = id;
    }
}
