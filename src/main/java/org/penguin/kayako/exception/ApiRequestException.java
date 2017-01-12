package org.penguin.kayako.exception;

/**
 * Wrapper class for exception which happens during creating or sending requests to kayako.
 *
 * @author raynerw
 * @author fatroom
 */
public class ApiRequestException extends RuntimeException {

    public ApiRequestException(final Throwable e) {
        super("Kayako API request failed.", e);
    }

    public ApiRequestException(final String message, final Throwable e) {
        super(message, e);
    }
}
