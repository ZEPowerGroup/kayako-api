package org.penguin.kayako;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.net.URLEncoder.encode;

/**
 * An builder for URLs. Based on the javax.wx.rs UriBuilder.
 * 
 * @author raynerw
 * 
 */
public class UriBuilder {
    private final static String UTF8 = "UTF-8";
    
    private final String host;
    private final String path;
    private final String queryPath;
    private final String scheme = "http";
    private final ImmutableMap<String, String> queryParameters;
    
    public UriBuilder(String host) {
        this.host = host;
        this.path = "";
        this.queryPath = "";
        this.queryParameters = ImmutableMap.<String, String> builder().build();;
    }
    
    private UriBuilder(String host, String path, String queryPath, Map<String, String> queryParameters) {
        this.host = host;
        this.path = path;
        this.queryPath = queryPath;
        this.queryParameters = ImmutableMap.<String, String> copyOf(queryParameters);
    }
    
    /**
     * Add a segment to the path portion of the URI. Returns a new instance of the {@link UriBuilder}, leaving the old
     * instance untouched. This path will be URL encoded, so don't try to encode it yourself or attempt to append
     * multiple path segments in one call (eg. /path/to/thing). Leading and trailing /'s are not needed, as they will be
     * trimmed.
     * 
     * @param pathSegment
     *            The path segment you want to add onto the URI you are building.
     * @return A new instance of {@link UriBuilder} with the changed path.
     * @throws UriBuilderException
     *             A wrapper for any exceptions that occur building the URI.
     */
    public UriBuilder path(String pathSegment) throws UriBuilderException {
        try {
            String newPath = path + "/" + encode(trimSlashes(pathSegment), UTF8);
            return new UriBuilder(host, newPath, queryPath, queryParameters);
        } catch (UnsupportedEncodingException e) {
            throw new UriBuilderException(e);
        }
    }
    
    /**
     * Add a segment to the query parameter path. This is before the query key-value parameters, but after the path
     * portion of the URI.
     * 
     * @param pathSegment
     *            The query path segment you wish to add. Leading/trailing slashes (/) will be trimmed. Everything else
     *            will be URL encoded.
     * @return A new instance of {@link UriBuilder} with the changed query path.
     * @throws UriBuilderException
     *             A wrapper for any exceptions that occur building the URI.
     */
    public UriBuilder queryPath(String pathSegment) throws UriBuilderException {
        try {
            return queryPathUnescaped(encode(trimSlashes(pathSegment), UTF8));
        } catch (UnsupportedEncodingException e) {
            throw new UriBuilderException(e);
        }
    }
    
    /**
     * Add a segment to the query parameter path. This is before the query key-value parameters, but after the path
     * portion of the URI.
     * 
     * @param pathSegment
     *            The query path segment you wish to add. Leading/trailing slashes (/) will be trimmed. This will not be
     *            URL encoded.
     * @return A new instance of {@link UriBuilder} with the changed query path.
     * @throws UriBuilderException
     *             A wrapper for any exceptions that occur building the URI.
     */
    public UriBuilder queryPathUnescaped(String pathSegment) throws UriBuilderException {
        String newQueryPath = queryPath + "/" + trimSlashes(pathSegment);
        return new UriBuilder(host, path, newQueryPath, queryParameters);
    }
    
    /**
     * Add a query parameter to the URI.
     * 
     * @param key
     *            The key of the pair. This will be URL encoded.
     * @param value
     *            The value of the pair. This will be URL encoded.
     * @return A new instance of {@link UriBuilder} with the additional query parameters.
     */
    public UriBuilder queryParam(String key, String value) {
        return new UriBuilder(host, path, queryPath,
                ImmutableMap.<String, String> builder().putAll(queryParameters).put(key, value).build());
    }
    
    /**
     * Get the path segment of this URI. This actually includes everything after the host section. Query path and params
     * are incldued.
     * 
     * @return A string representing the path of the URI
     * @throws URISyntaxException
     *             If the URI built so far is invalid.
     */
    public String getPath() throws URISyntaxException {
        try {
            String basePath = path + (!isNullOrEmpty(queryPath) || queryParameters.size() > 0 ? "?" + queryPath : "");
            int i = 0;
            for (Entry<String, String> entry : queryParameters.entrySet()) {
                if (i != 0 || !isNullOrEmpty(queryPath)) {
                    basePath = basePath + "&";
                }
                basePath = basePath + encode(entry.getKey(), UTF8) + "=" + encode(entry.getValue(), UTF8);
                
                i++;
            }
            return basePath;
        } catch (UnsupportedEncodingException e) {
            throw new UriBuilderException(e);
        }
    }
    
    /**
     * Returns a new {@link URL} instance with the URI you are building.
     * 
     * @return A new {@link URL} instance with the scheme, host and path you've built.
     */
    public URL toURL() {
        try {
            return new URL(scheme, host, getPath());
        } catch (MalformedURLException e) {
            throw new UriBuilderException(e);
        } catch (URISyntaxException e) {
            throw new UriBuilderException(e);
        }
    }

    /**
     * Returns a new {@link URI} instance with the URI you are building.
     *
     * @return A new {@link URI} instance with the scheme, host and path you've built.
     */
    public URI toURI() {
        try {
        return toURL().toURI();
        } catch (URISyntaxException e) {
            throw new UriBuilderException(e);
        }
    }

    private static String trimSlashes(String path) {
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        while (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }
    
    public static class UriBuilderException extends RuntimeException {
        public UriBuilderException(Throwable e) {
            super("An error occurred building URI", e);
        }
    }
}
