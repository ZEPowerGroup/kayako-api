package org.penguin.kayako.staff;

import java.io.StringReader;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.penguin.kayako.UnmarshallerFactory;
import org.penguin.kayako.exception.ApiResponseException;

import com.google.common.base.Strings;

public class StaffApiResponse {
  private final String response;

  public StaffApiResponse(final String response) {
    this.response = response;
  }

  public String getResponse() {
    return response;
  }

  public <E> E as(final Class<E> returnType) throws ApiResponseException {
    try {
      final Unmarshaller unmarshaller = UnmarshallerFactory.getMapper(returnType);
      return (E) unmarshaller.unmarshal(new StringReader(Strings.nullToEmpty(response)));
    }
    catch (final JAXBException e) {
      throw new ApiResponseException("An exception occurred unmarshalling return content", e);
    }
  }
}
