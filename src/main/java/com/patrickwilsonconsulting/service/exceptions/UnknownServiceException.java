package com.patrickwilsonconsulting.service.exceptions;

/**
 * Created by pwilson on 11/9/17.
 */
public class UnknownServiceException extends RuntimeException {
    public UnknownServiceException(String message) {
        super(message);
    }

    public UnknownServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
