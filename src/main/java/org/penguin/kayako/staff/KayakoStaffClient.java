package org.penguin.kayako.staff;

import java.util.ArrayList;

import org.penguin.kayako.HttpRequestExecutor;
import org.penguin.kayako.HttpRequestExecutorImpl;
import org.penguin.kayako.UriBuilder;

public class KayakoStaffClient {

  private UriBuilder baseURI;
  private HttpRequestExecutor requestExecutor;
  private String sessionId;
  private final String username;
  private final String password;

  public KayakoStaffClient(final String host, final String username, final String password) {
    this.baseURI = new UriBuilder(host).path("staffapi").path("index.php");
    this.requestExecutor = new HttpRequestExecutorImpl();
    this.username = username;
    this.password = password;
  }

  public LoginResponse login () {
    final String loginUri = "/Core/Default/Login";
    final LoginResponse loginResponse = new StaffApiRequest(requestExecutor, baseURI, new ArrayList<>(), sessionId)
        .withPathRaw(loginUri)
        .withPostParam("username", username)
        .withPostParam("password", password)
        .post()
        .as(LoginResponse.class);
    sessionId = loginResponse.getSessionId();
    return loginResponse;
  }

  public StaffTicketService getTicketService() {
    return new StaffTicketService(baseURI, requestExecutor, sessionId);
  }

  public UriBuilder getBaseURI() {
    return baseURI;
  }

  public void setBaseURI(final UriBuilder baseURI) {
    this.baseURI = baseURI;
  }

  public HttpRequestExecutor getRequestExecutor() {
    return requestExecutor;
  }

  public void setRequestExecutor(final HttpRequestExecutor requestExecutor) {
    this.requestExecutor = requestExecutor;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(final String sessionId) {
    this.sessionId = sessionId;
  }
}
