package org.penguin.kayako;

import java.io.IOException;

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
            final CloseableHttpResponse response = httpClient.execute(request)){
            StatusLine status = response.getStatusLine();
            if (HttpStatus.SC_OK != status.getStatusCode()) {
                throw new ApiRequestException(new IOException("Request failed with status code: " + status.getStatusCode()));
            }
            return EntityUtils.toString(response.getEntity());
        }
    }
    
}
