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
import org.penguin.kayako.UriBuilder;
import org.penguin.kayako.domain.Post;
import org.penguin.kayako.exception.ApiRequestException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StaffTicketService {

  private static final String TICKET_LIST_REQUEST_PATH = "/Tickets/Retrieve";

  private static final String TICKET_REQUEST_PATH = TICKET_LIST_REQUEST_PATH + "/Data";

  private static final String TICKET_UPDATE_PATH = "/Tickets/Push/Index";

  private final UriBuilder baseURI;

  private final HttpRequestExecutor requestExecutor;

  private final String sessionId;

  public StaffTicketService(final UriBuilder baseURI, final HttpRequestExecutor requestExecutor, final String sessionId) {
    this.baseURI = baseURI;
    this.requestExecutor = requestExecutor;
    this.sessionId = sessionId;
  }

  public List<StaffTicket> getTickets(final EnumMap<TicketFetchParam, String> params) {
    return new StaffApiRequest(requestExecutor, baseURI, buildParams(params), sessionId)
        .withPathRaw(TICKET_LIST_REQUEST_PATH)
        .post()
        .as(StaffTicketCollection.class).getTickets();
  }

  private static List<NameValuePair> buildParams(final EnumMap<TicketFetchParam, String> params) {
    return params.entrySet().stream().map(entry ->
        new BasicNameValuePair(entry.getKey().value(), entry.getValue())).collect(Collectors.toList());
  }

  public String executeTicketRequest(final Command command, final EnumMap<TicketParams, String> params) {
    final List<NameValuePair> paramList = new ArrayList<>();
    paramList.add(new BasicNameValuePair("payload", buildPayload(command, params)));
    return new StaffApiRequest(requestExecutor, baseURI, paramList, sessionId)
        .withPathRaw(TICKET_UPDATE_PATH)
        .post()
        .getResponse();
  }

  private static String buildPayload(final Command command, final EnumMap<TicketParams, String> params) {
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
          commandElement.setAttribute(TicketParams.TICKET_ID.value(), params.get(TicketParams.TICKET_ID));
          break;
        default:
          throw new ApiRequestException("Failed to build payload for " + command.name() + " command." +
              "The command must be one of the following values: " + Arrays.toString(Command.values()));
      }

      rootElement.appendChild(commandElement);
      for (final Map.Entry<TicketParams, String> entry : params.entrySet()) {
        if (entry.getKey() != TicketParams.TICKET_ID) {
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

  public List<Post> getPosts(final String ticketId) {
    final List<NameValuePair> paramList = new ArrayList<>();
    paramList.add(new BasicNameValuePair("sessionid", sessionId));
    paramList.add(new BasicNameValuePair("ticketid", ticketId));
    return new StaffApiRequest(requestExecutor, baseURI, paramList, sessionId)
        .withPathRaw(TICKET_REQUEST_PATH)
        .post()
        .as(StaffTicketCollection.class).getTickets().get(0).getPosts();
  }
}
