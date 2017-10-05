package org.penguin.kayako.staff;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.penguin.kayako.HttpRequestExecutor;
import org.penguin.kayako.HttpRequestExecutorImpl;
import org.penguin.kayako.UriBuilder;
import org.penguin.kayako.exception.ApiRequestException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This client wraps the kayako staff api. It can be used to:
 * 1. Update a ticket using http verb POST instead of PUT incase the PUT command is blocked from reaching the kayako
 * server by an intermediate web service such as ISS
 * 2. Provde a more flexbile search for tickets.
 * 3. Allow additional fields to be configured when creating/updating a ticket that are not configurable using the
 * REST API wrapper.
 *
 */
public class KayakoStaffClient {

  private static final String TICKET_LIST_REQUEST_PATH = "/Tickets/Retrieve";

  private static final String TICKET_UPDATE_PATH = "/Tickets/Push/Index";

  private final UriBuilder baseURI;
  private HttpRequestExecutor requestExecutor;
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
    final LoginResponse loginResponse = new StaffApiRequest(requestExecutor, baseURI, new ArrayList<>(), null)
        .withPathRaw(loginUri)
        .withPostParam("username", username)
        .withPostParam("password", password)
        .post()
        .as(LoginResponse.class);
    if (Integer.parseInt(loginResponse.getStatus()) != 1) {
      throw new ApiRequestException("Failed to login with specified credentials: " + loginResponse.getError());
    }
    return loginResponse;
  }

  /**
   * Depending on the command which is passed in, this method can take one of three actions:
   * 1. Create a ticket
   * 2. Update a ticket
   * 3. Retrieve a list of tickets.
   *
   * For information on which parameters are valid for each command, please refer to
   * https://kayako.atlassian.net/wiki/spaces/DEV/pages/4816966/Kayako+Staff+API
   *
   * Note that this method does not validate the parameters passed in to check if they are valid for the specified
   * command. If invalid parameters are passed, an unexpected result could occur so care should be taken to ensure valid
   * parameters are being used.
   *
   * @param command - The action to take.
   * @param params - A map of relevant parameters for the specified command.
   * @param sessionId - A pre-existing session id that can be used to execute the request. Pass in null if no previous
   *                  session exists. A new session id will be automatically generated and used.
   * @return A {@link StaffApiResponse} based on which command was executed. For detailed information about what each
   * commands response will contain please refer to the url above. The session id used to perform the request will also
   * be inside of the response.
   */
  public StaffApiResponse executeTicketRequest(final Command command,
                                               final EnumMap<TicketParam, String> params,
                                               String sessionId) {
    int numAttempts = 3;
    while (numAttempts-- > 0) {
      final StaffApiResponse response = executeCommand(command, params, sessionId == null || sessionId.isEmpty() ?
          login().getSessionId() : sessionId);
      try {
        if (Integer.parseInt(response.as(LoginResponse.class).getStatus()) == 1) {
          return response;
        }
        sessionId = login().getSessionId();
      }
      catch (final Exception e) {
        //ignore and retry
      }
    }
    //ran out of retries and failed to obtain a successful response
    throw new ApiRequestException("Failed to obtain a successful response from Kayako server after " + numAttempts +
        " attempts.");
  }

  private StaffApiResponse executeCommand(final Command command,
                                          final EnumMap<TicketParam, String> params,
                                          final String sessionId) {
    final StaffApiResponse response;
    switch (command) {
      case RETRIEVE:
        response = getTickets(params, sessionId);
        break;
      case UPDATE:
      case CREATE:
        final List<NameValuePair> paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("payload", buildPayload(command, params)));
        response = new StaffApiRequest(requestExecutor, baseURI, paramList, sessionId)
            .withPathRaw(TICKET_UPDATE_PATH)
            .post();
        break;
      default:
        throw new ApiRequestException("Failed to execute ticket request for the specified command. " +
            "The command must be one of the following values: " +
            String.join(", ", Command.RETRIEVE.name(), Command.CREATE.name(), Command.UPDATE.name()));
    }
    response.setSessionId(sessionId);
    return response;
  }

  private StaffApiResponse getTickets(final EnumMap<TicketParam, String> params, final String sessionId) {
    return new StaffApiRequest(requestExecutor, baseURI, buildParams(params), sessionId)
        .withPathRaw(TICKET_LIST_REQUEST_PATH)
        .post();
  }

  private static List<NameValuePair> buildParams(final EnumMap<TicketParam, String> params) {
    return params.entrySet().stream().map(entry ->
        new BasicNameValuePair(entry.getKey().getName(), entry.getValue())).collect(Collectors.toList());
  }

  private static String buildPayload(final Command command, final EnumMap<TicketParam, String> params) {
    try {
      final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
      final Element rootElement = document.createElement("kayako_staffapi");
      final Element commandElement;
      document.appendChild(rootElement);

      switch (command) {
        case CREATE:
          commandElement = document.createElement("create");
          commandElement.setAttribute("staffapiid", "1");
          break;
        case UPDATE:
          commandElement = document.createElement("modify");
          commandElement.setAttribute(TicketParam.TICKET_ID.getName(), params.get(TicketParam.TICKET_ID));
          break;
        default:
          throw new ApiRequestException("Failed to build payload for the specified command. " +
              "The command must be one of the following values: " + String.join(", ",
              Command.CREATE.name(), Command.UPDATE.name()));
      }

      rootElement.appendChild(commandElement);
      params.entrySet().stream().filter(entry -> entry.getKey() != TicketParam.TICKET_ID).forEach(entry -> {
        final Element param = document.createElement(entry.getKey().getName());
                  param.appendChild(document.createTextNode(entry.getValue()));
                  commandElement.appendChild(param);
      });
      return getStringFromDocument(document);
    }
    catch (final ParserConfigurationException e) {
      throw new ApiRequestException(e);
    }
  }

  private static String getStringFromDocument(final Document doc) {
    try {
      final DOMSource domSource = new DOMSource(doc);
      final StringWriter writer = new StringWriter();
      final StreamResult result = new StreamResult(writer);
      TransformerFactory.newInstance().newTransformer().transform(domSource, result);
      return writer.toString();
    }
    catch (final TransformerException e) {
      throw new ApiRequestException(e);
    }
  }

  public UriBuilder getBaseURI() {
    return baseURI;
  }

  public HttpRequestExecutor getRequestExecutor() {
    return requestExecutor;
  }

  public void setRequestExecutor(final HttpRequestExecutor requestExecutor) {
    this.requestExecutor = requestExecutor;
  }
}
