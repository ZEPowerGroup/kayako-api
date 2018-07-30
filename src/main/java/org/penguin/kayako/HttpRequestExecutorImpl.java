package org.penguin.kayako;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.penguin.kayako.exception.ApiBadRequestException;
import org.penguin.kayako.exception.ApiRequestException;

import com.google.common.base.Preconditions;

public class HttpRequestExecutorImpl implements HttpRequestExecutor {

  /** Request object where parameters may be configured, e.g. connection timeout */
  private final RequestConfig requestConfig;

  /**
   * Class constructor, initializes request config
   *
   * @param requestTimeout Request timeout in milliseconds.  Zero denotes infinite timeout.
   */
  public HttpRequestExecutorImpl(final Integer requestTimeout) {
    Preconditions.checkArgument(null != requestTimeout, "Request timeout may not be null");
    requestConfig = RequestConfig.custom()
      .setConnectTimeout(requestTimeout)
      .setConnectionRequestTimeout(requestTimeout)
      .setSocketTimeout(requestTimeout)
      .build();
  }
    
  @Override
  public String execute(final HttpRequestBase request) throws ParseException, IOException {
    try(final CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
      final CloseableHttpResponse response = httpClient.execute(request)) {
      final StatusLine status = response.getStatusLine();
      final HttpEntity entity = response.getEntity();
      if (HttpStatus.SC_OK != status.getStatusCode()) {
          final String message = "Request failed with status code: " + status +
                                  (null == entity ? "" : " - \"" + EntityUtils.toString(entity) + "\"");
          if (HttpStatus.SC_BAD_REQUEST == status.getStatusCode()) {
              throw new ApiBadRequestException(message);
          }
          throw new ApiRequestException(message);
      }
      return EntityUtils.toString(entity);
    }
  }
}
