package org.penguin.kayako.staff;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
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

public class KayakoStaffClient {

  private static final String TICKET_LIST_REQUEST_PATH = "/Tickets/Retrieve";

  private static final String TICKET_UPDATE_PATH = "/Tickets/Push/Index";

  private UriBuilder baseURI;
  private HttpRequestExecutor requestExecutor;
  private String sessionId;
  private final String username;
  private final String password;
  private long sessionTimeout;
  private long lastRequestTime;

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
    sessionTimeout = Long.parseLong(loginResponse.getSessionTimeout()) * 1000;
    return loginResponse;
  }

  public StaffApiResponse executeTicketRequest(final Command command, final EnumMap<TicketParam, String> params) {
    if (sessionId == null || sessionId.isEmpty() || System.currentTimeMillis() - lastRequestTime > sessionTimeout) {
      final LoginResponse loginResponse = login();
      if (loginResponse.getError() != null && !loginResponse.getError().isEmpty()) {
        throw new ApiRequestException("Request failed due to no active session. " + loginResponse.getError());
      }
    }
    final StaffApiResponse response;
    switch (command) {
      case RETRIEVE:
        response = getTickets(params);
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
        throw new ApiRequestException("Failed to execute ticket request for " + command.name() + " command." +
            "The command must be one of the following values: " + Arrays.toString(Command.values()));
    }
    lastRequestTime = System.currentTimeMillis();
    return response;
  }

  private StaffApiResponse getTickets(final EnumMap<TicketParam, String> params) {
    return new StaffApiRequest(requestExecutor, baseURI, buildParams(params), sessionId)
        .withPathRaw(TICKET_LIST_REQUEST_PATH)
        .post();
  }

  private static List<NameValuePair> buildParams(final EnumMap<TicketParam, String> params) {
    return params.entrySet().stream().map(entry ->
        new BasicNameValuePair(entry.getKey().value(), entry.getValue())).collect(Collectors.toList());
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
          commandElement.setAttribute(TicketParam.TICKET_ID.value(), params.get(TicketParam.TICKET_ID));
          break;
        default:
          throw new ApiRequestException("Failed to build payload for " + command.name() + " command." +
              "The command must be one of the following values: " + String.join(",",
              Command.CREATE.name(), Command.UPDATE.name()));
      }

      rootElement.appendChild(commandElement);
      for (final Map.Entry<TicketParam, String> entry : params.entrySet()) {
        if (entry.getKey() != TicketParam.TICKET_ID) {
          final Element param = document.createElement(entry.getKey().value());
          param.appendChild(document.createTextNode(entry.getValue()));
          commandElement.appendChild(param);
        }
      }
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
