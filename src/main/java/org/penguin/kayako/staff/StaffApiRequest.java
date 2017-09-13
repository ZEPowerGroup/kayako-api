package org.penguin.kayako.staff;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.penguin.kayako.ApiResponse;
import org.penguin.kayako.HttpRequestExecutor;
import org.penguin.kayako.UriBuilder;
import org.penguin.kayako.exception.ApiRequestException;

public class StaffApiRequest {
  private final HttpRequestExecutor requestExecutor;
  private final UriBuilder uri;
  private final List<NameValuePair> params;
  private final String sessionId;

  public StaffApiRequest(final HttpRequestExecutor requestExecutor, final UriBuilder uri,
                          final List<NameValuePair> params, final String sessionId) {
    this.requestExecutor = requestExecutor;
    this.uri = uri;
    this.params = params;
    this.sessionId = sessionId;
  }

  public StaffApiRequest withPath(final String path) {
    return new StaffApiRequest(requestExecutor, uri.queryPath(path), params, sessionId);
  }

  public StaffApiRequest withPathRaw(final String path) {
    return new StaffApiRequest(requestExecutor, uri.queryPathUnescaped(path), params, sessionId);
  }

  public StaffApiRequest withPostParam(final String name, final Object value) {
    final StaffApiRequest request = new StaffApiRequest(requestExecutor, uri, params, sessionId);
    request.params.add(new BasicNameValuePair(name, String.valueOf(value)));
    return request;
  }

  public ApiResponse post() throws ApiRequestException {
    try {
      final HttpPost post = new HttpPost(uri.toURI());
      post.setEntity(new UrlEncodedFormEntity(applySessionIdParam(params), Charset.forName("UTF-8")));
      return new ApiResponse(requestExecutor.execute(post));
    }
    catch (final IOException e) {
      throw new ApiRequestException(e);
    }
  }

  private List<NameValuePair> applySessionIdParam(final List<NameValuePair> original) {
    final List<NameValuePair> params = new ArrayList<>(original);
    params.add(new BasicNameValuePair("sessionid", sessionId));
    return params;
  }
}
