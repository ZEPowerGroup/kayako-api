package org.penguin.kayako;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;
import org.penguin.kayako.exception.ApiRequestException;

import com.google.common.collect.Lists;

public class ApiRequest {
    private final HttpRequestExecutor requestExecutor;
    private final String apiSecret;
    private final String apiKey;
    private final String salt;
    private final String signature;
    
    private UriBuilder uri;
    private List<NameValuePair> params;
    
    protected ApiRequest(KayakoClient client) throws ApiRequestException {
        this(client.getRequestExecutor(), client.getApiKey(), client.getApiSecret(), client.getBaseURI());
    }
    
    private ApiRequest(HttpRequestExecutor requestExecutor, String apiKey, String apiSecret, UriBuilder baseURI) throws ApiRequestException {
        this.requestExecutor = requestExecutor;
        this.apiSecret = apiSecret;
        
        SecureRandom r = new SecureRandom();
        StringBuilder saltBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            saltBuilder.append((char) (48 + r.nextInt(74)));
        }
        this.apiKey = apiKey;
        this.salt = saltBuilder.toString();
        this.signature = generateSignature(apiSecret, salt);
        this.uri = baseURI;
        this.params = Lists.newArrayList();
    }
    
    private ApiRequest(HttpRequestExecutor requestExecutor, String apiKey, String apiSecret, String salt, String signature, UriBuilder uri, List<NameValuePair> params) {
        this.requestExecutor = requestExecutor;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.salt = salt;
        this.signature = signature;
        this.uri = uri;
        this.params = Lists.newArrayList(params);
    }
    
    public ApiRequest withPath(String path) {
        return new ApiRequest(requestExecutor, apiKey, apiSecret, salt, signature, uri.queryPath(path), params);
    }
    
    public ApiRequest withPathRaw(String path) {
        return new ApiRequest(requestExecutor, apiKey, apiSecret, salt, signature, uri.queryPathUnescaped(path), params);
    }
    
    public ApiRequest withPostParam(final String name, final Object value) {
        ApiRequest request = new ApiRequest(requestExecutor, apiKey, apiSecret, salt, signature, uri, params);
        request.params.add(new BasicNameValuePair(name, String.valueOf(value)));
        return request;
    }
    
    public ApiResponse get() throws ApiRequestException {
        try {
            UriBuilder uriBuilder = applySecurityParams(uri);
            String content = requestExecutor.execute(new HttpGet(uriBuilder.toURI()));
            return new ApiResponse(content);
        } catch (ClientProtocolException e) {
            throw new ApiRequestException(e);
        } catch (IOException e) {
            throw new ApiRequestException(e);
        }
    }
    
    public ApiResponse post() throws ApiRequestException {
        try {
            HttpPost post = new HttpPost(uri.toURI());
            List<NameValuePair> requestParams = applySecurityParams(params);
            post.setEntity(new UrlEncodedFormEntity(requestParams, Charset.forName("UTF-8")));
            String content = requestExecutor.execute(post);
            return new ApiResponse(content);
        } catch (IOException e) {
            throw new ApiRequestException(e);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e);
        }
    }
    
    public ApiResponse put() throws ApiRequestException {
        try {
            UriBuilder uriBuilder = applySecurityParams(uri);
            HttpPut put = new HttpPut(uriBuilder.toURI());
            List<NameValuePair> requestParams = applySecurityParams(params);
            put.setEntity(new UrlEncodedFormEntity(requestParams, Charset.forName("UTF-8")));
            String content = requestExecutor.execute(put);
            return new ApiResponse(content);
        } catch (ClientProtocolException e) {
            throw new ApiRequestException(e);
        } catch (IOException e) {
            throw new ApiRequestException(e);
        }
    }
    
    public ApiResponse delete() throws ApiRequestException {
        try {
            UriBuilder uriBuilder = applySecurityParams(uri);
            String content = requestExecutor.execute(new HttpDelete(uriBuilder.toURI()));
            return new ApiResponse(content);
        } catch (ClientProtocolException e) {
            throw new ApiRequestException(e);
        } catch (IOException e) {
            throw new ApiRequestException(e);
        }
    }
    
    public ApiRequest setSalt(String salt) throws ApiRequestException {
        return new ApiRequest(requestExecutor, apiKey, apiSecret, salt, generateSignature(apiSecret, salt), uri, params);
    }
    
    public String getSignature() {
        return signature;
    }
    
    private static String generateSignature(String apiSecret, String salt) throws ApiRequestException {
        Mac hmac;
        try {
            hmac = Mac.getInstance("HmacSHA256");
            hmac.init(new SecretKeySpec(apiSecret.getBytes(), "HmacSHA256"));
            
            return Base64.getEncoder().encodeToString(hmac.doFinal(salt.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new ApiRequestException(e);
        }
    }
    
    private UriBuilder applySecurityParams(final UriBuilder builder) {
        return builder.queryParam("apikey", apiKey)
                .queryParam("salt", salt)
                .queryParam("signature", signature);
    }
    
    private List<NameValuePair> applySecurityParams(final List<NameValuePair> originalParams) {
        List<NameValuePair> params = Lists.newArrayList(originalParams);
        params.add(new BasicNameValuePair("apikey", apiKey));
        params.add(new BasicNameValuePair("salt", salt));
        params.add(new BasicNameValuePair("signature", signature));
        return params;
    }
}
