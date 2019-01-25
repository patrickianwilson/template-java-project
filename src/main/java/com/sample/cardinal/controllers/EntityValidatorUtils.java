package com.sample.cardinal.controllers;

import com.google.api.client.repackaged.com.google.common.base.Strings;
import com.sample.cardinal.resource.model.User;
import com.sample.cardinal.controllers.exceptions.InvalidInputException;

/**
 * Created by pwilson on 11/5/17.
 */
public class EntityValidatorUtils {


    public static void validateFieldNotSet(String str, Class<?> entityClazz, String field, String message, String... args) throws InvalidInputException {
        if (!Strings.isNullOrEmpty(str)) {
            throw new InvalidInputException(entityClazz, field, String.format(message, args));
        }
    }

    public static void validateFieldNotEmpty(String str, Class<?> entityClazz, String field, String message, String... args) throws InvalidInputException {
        if (Strings.isNullOrEmpty(str)) {
            throw new InvalidInputException(entityClazz, field, String.format(message, args));
        }
    }

    public static void validateEntity(User app) throws InvalidInputException {
        validateFieldNotEmpty(app.getUserId(), User.class, "userId", "cannot be null or empty");
    }

    /**
     * Add validators for all API entities here.
     */

}
