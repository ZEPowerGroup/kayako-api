package org.penguin.kayako;

import java.io.StringReader;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.penguin.kayako.exception.ApiResponseException;

import com.google.common.base.Strings;

public class ApiResponse {
    private final String responseContent;
    
    protected ApiResponse(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getResponseContent() {
        return responseContent;
    }

    @SuppressWarnings("unchecked")
    protected <E> E as(Class<E> returnType) throws ApiResponseException {
        
        try {
            Unmarshaller unmarshaller = UnmarshallerFactory.getMapper(returnType);
            return (E) unmarshaller.unmarshal(new StringReader(Strings.nullToEmpty(responseContent)));
        } catch (JAXBException e) {
            throw new ApiResponseException("An exception occurred unmarshalling return content", e);
        }
    }
    
}
