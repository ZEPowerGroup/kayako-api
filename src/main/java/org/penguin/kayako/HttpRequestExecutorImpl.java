package org.penguin.kayako;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.penguin.kayako.exception.ApiRequestException;

public class HttpRequestExecutorImpl implements HttpRequestExecutor {
    
    @Override
    public String execute(HttpRequestBase request) throws ParseException, IOException {
        try(final CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            final CloseableHttpResponse response = httpClient.execute(request)) {
            final StatusLine status = response.getStatusLine();
            final HttpEntity entity = response.getEntity();
            if (HttpStatus.SC_OK != status.getStatusCode()) {
                throw new ApiRequestException(
                    new IOException(status + (null == entity ? "" : " - \"" + EntityUtils.toString(entity) + "\"")));
            }
            return EntityUtils.toString(entity);
        }
    }
    
}
