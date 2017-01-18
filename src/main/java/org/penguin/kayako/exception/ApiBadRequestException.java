package org.penguin.kayako.exception;

/**
 *  Wrapper class for exception which happens http requests to kayako returns http response 400 Bad Request code.
 *
 *  @author eugene.ovsiannikov
 */
public final class ApiBadRequestException extends ApiRequestException {

  public ApiBadRequestException(final String message) {
    super(message);
  }

}
