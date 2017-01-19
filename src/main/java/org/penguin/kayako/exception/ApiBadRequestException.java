package org.penguin.kayako.exception;

/**
 *  Wrapper class for exception which should be thrown when http response with code 400 (Bad Request) is received
 *  from kayako server.
 *
 *  @author eugene.ovsiannikov
 */
public final class ApiBadRequestException extends ApiRequestException {

  public ApiBadRequestException(final String message) {
    super(message);
  }

}
