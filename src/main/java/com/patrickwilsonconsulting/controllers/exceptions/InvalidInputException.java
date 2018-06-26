package com.patrickwilsonconsulting.controllers.exceptions;

import lombok.Getter;

/**
 * Created by pwilson on 9/20/17.
 */
@Getter
public class InvalidInputException extends RuntimeException {
    private Class modelClazz;
    private String reason;
    private String field;

    public InvalidInputException(Class modelClazz, String field, String reason) {
        super(String.format("Invalid input for model type: %s, field '%s' is '%s'", modelClazz.getSimpleName(), field, reason));
        this.modelClazz = modelClazz;
        this.reason = reason;
        this.field = field;
    }

    public InvalidInputException(Class modelClazz, String field, String value, String reason) {
        super(String.format("Invalid input for model type: %s, field '%s' with value '%s' is '%s'", modelClazz.getSimpleName(), field, value, reason));
        this.modelClazz = modelClazz;
        this.reason = reason;
        this.field = field;
    }


}
