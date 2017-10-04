package org.penguin.kayako.staff;

import java.io.StringReader;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.penguin.kayako.UnmarshallerFactory;
import org.penguin.kayako.exception.ApiResponseException;

import com.google.common.base.Strings;

/**
 * Created by ricky.sandhu on 10/3/2017.
 */
public class StaffApiResponse {

  private final String responseContent;
  private String sessionId;

  public StaffApiResponse(final String responseContent, final String sessionId) {
    this.responseContent = responseContent;
    this.sessionId = sessionId;
  }

  public String getResponseContent() {
      return responseContent;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(final String sessionId) {
    this.sessionId = sessionId;
  }

  @SuppressWarnings("unchecked")
  public <E> E as(final Class<E> returnType) throws ApiResponseException {
      try {
          final Unmarshaller unmarshaller = UnmarshallerFactory.getMapper(returnType);
          return (E) unmarshaller.unmarshal(new StringReader(Strings.nullToEmpty(responseContent)));
      } catch (final JAXBException e) {
          throw new ApiResponseException("An exception occurred unmarshalling return content", e);
      }
  }
}
