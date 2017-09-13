package org.penguin.kayako;

import java.util.List;

import org.penguin.kayako.domain.StaffUser;
import org.penguin.kayako.domain.StaffUserCollection;
import org.penguin.kayako.exception.ApiRequestException;
import org.penguin.kayako.exception.ApiResponseException;

/**
 * Created by ricky.sandhu on 9/12/2017.
 */
public class StaffConnector extends AbstractConnector{

  protected StaffConnector(final KayakoClient client) {
    super(client);
  }

  /**
   * Retrieve a list of all the staff in the help desk.
   *
   * @return A collection of staff known in system
   * @throws ApiResponseException A wrapped exception of anything that went wrong when handling the response from kayako
   * @throws ApiRequestException  A wrapped exception of anything that went wrong sending the request to kayako
   */
  public List<StaffUser> list() throws ApiRequestException, ApiResponseException {
      return getApiRequest()
          .get()
          .as(StaffUserCollection.class)
          .getUsers();
  }

  @Override
  protected ApiRequest getApiRequest() {
      final ApiRequest request = super.getApiRequest();
      return request
          .withPath("Base")
          .withPath("Staff");
  }

}
