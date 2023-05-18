package com.sample.cardinal.controllers.exceptions;

/**
 * Created by pwilson on 11/21/17.
 */
public class InvalidStateException extends UnknownServiceException {
    public InvalidStateException(String message) {
        super(message);
    }

    public InvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
