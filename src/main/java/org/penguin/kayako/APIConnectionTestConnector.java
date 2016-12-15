package org.penguin.kayako;

import java.util.regex.Pattern;

import org.penguin.kayako.exception.ApiRequestException;
import org.penguin.kayako.exception.ApiResponseException;

/**
 * Wrapper for only the API connection test call.
 *
 * @author eugene
 */
public final class APIConnectionTestConnector extends AbstractConnector {

  private static final Pattern RESPONSE_PATTERN = Pattern.compile(".*apikey.*salt.*signature.*");

  protected APIConnectionTestConnector(final KayakoClient client) {
    super(client);
  }

  /**
   * Test the API connection.
   *
   * @throws ApiResponseException A wrapped exception of anything that went wrong when handling the response from kayako
   * @throws ApiRequestException  A wrapped exception of anything that went wrong sending the request to kayako
   */
  public void test() throws ApiRequestException, ApiResponseException {
    final String responseContent = getApiRequest().get().getResponseContent();
    if (!RESPONSE_PATTERN.matcher(responseContent.replaceAll("\\W", "")).matches()) {
      throw new ApiResponseException("Unexpected response: " + responseContent);
    }
  }

  @Override
  protected ApiRequest getApiRequest() {
    final ApiRequest request = super.getApiRequest();
    return request.withPath("Core").withPath("TestAPI");
  }
}
