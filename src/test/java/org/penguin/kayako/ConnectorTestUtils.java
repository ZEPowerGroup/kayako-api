package org.penguin.kayako;

import java.io.Reader;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.http.client.methods.HttpRequestBase;
import org.hamcrest.Description;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConnectorTestUtils {
    public static void setupUnmarshaller() {
        Unmarshaller mockUnmarshaller = Mockito.mock(Unmarshaller.class);
        
        try {
            when(mockUnmarshaller.unmarshal(Mockito.any(Reader.class)))
                    .thenReturn(null);
            
            PowerMockito.mockStatic(UnmarshallerFactory.class);
            when(UnmarshallerFactory.getMapper(Mockito.any(Class.class)))
                    .thenReturn(mockUnmarshaller);
        } catch (JAXBException e) {
            // Shouldn't happen when mocking, just swallow
        }
    }
    
    public static KayakoClient defaultMockClient() {
        KayakoClient client = mock(KayakoClient.class);
        when(client.getApiKey()).thenReturn("key");
        when(client.getApiSecret()).thenReturn("secret");
        when(client.getBaseURI()).thenReturn(new UriBuilder("localhost"));
        return client;
    }
    
    public static ArgumentMatcher<HttpRequestBase> queryStartsWith(final String path) {
        return new ArgumentMatcher<HttpRequestBase>() {
            
            @Override
            public boolean matches(Object argument) {
                HttpRequestBase request = (HttpRequestBase) argument;
                String uriQuery = request.getURI().getQuery();
                return uriQuery.startsWith(path);
            }
            
            @Override
            public void describeTo(Description description) {
                description.appendText("with query path ").appendValue(path);
            }
        };
    }
    
    public static ArgumentMatcher<HttpRequestBase> queryMatches(final String regex) {
        return new ArgumentMatcher<HttpRequestBase>() {

            @Override
            public boolean matches(Object argument) {
                HttpRequestBase request = (HttpRequestBase) argument;
                String uriQuery = request.getURI().getQuery();
                return uriQuery.matches(regex);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with query matching ").appendValue(regex);
            }
        };
    }

    public static Unmarshaller marshallerThatReturns(Object ob) {
        Unmarshaller mockUnmarshaller = Mockito.mock(Unmarshaller.class);
        try {
            when(mockUnmarshaller.unmarshal(Mockito.any(Reader.class))).thenReturn(ob);
        } catch (JAXBException e) {
            // Shouldn't happen
        }
        return mockUnmarshaller;
    }
}
