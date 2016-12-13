package org.penguin.kayako.exception;

/**
 * Wrapper class for exception which happens during creating or sending requests to kayako.
 *
 * @author raynerw
 * @author fatroom
 */
public class ApiRequestException extends RuntimeException {

    public ApiRequestException(Throwable e) {
        super("An exception occurred attempting to create API request", e);
    }

    public ApiRequestException(String message, Throwable e) {
        super(message, e);
    }
}
