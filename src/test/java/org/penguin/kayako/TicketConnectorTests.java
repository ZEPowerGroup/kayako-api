package org.penguin.kayako;

import java.util.List;

import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.penguin.kayako.TicketConnector.DepartmentTicketRequest;
import org.penguin.kayako.domain.BasicTicket;
import org.penguin.kayako.domain.BasicTicketCollection;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.penguin.kayako.ConnectorTestUtils.queryStartsWith;
import static org.penguin.kayako.ConnectorTestUtils.queryMatches;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UnmarshallerFactory.class)
@PowerMockIgnore("javax.crypto.*")
public class TicketConnectorTests {
    @Before
    public void setup() {
        ConnectorTestUtils.setupUnmarshaller();
    }
    
    @Test
    public void testGetDepartmentTicketConstructsUri() throws Exception {
        // arrange
        KayakoClient client = ConnectorTestUtils.defaultMockClient();
        
        HttpRequestExecutor requestExecutor = mock(HttpRequestExecutor.class);
        when(client.getRequestExecutor()).thenReturn(requestExecutor);
        
        BasicTicketCollection ticketCollection = mock(BasicTicketCollection.class);
        Unmarshaller ticketCollectionUnmarshaller = ConnectorTestUtils.marshallerThatReturns(ticketCollection);
        when(UnmarshallerFactory.getMapper(BasicTicketCollection.class))
                .thenReturn(ticketCollectionUnmarshaller);
        
        TicketConnector connector = new TicketConnector(client);
        
        // act
        List<BasicTicket> tickets = connector.forDepartment(12);
        
        // assert
        verify(requestExecutor).execute(argThat(queryStartsWith("/Tickets/Ticket/ListAll/12/-1/-1/-1&")));
    }
    
    @Test
    public void testGetWithTicketFilterTicketStatusConstructsUri() throws Exception {
        // arrange
        KayakoClient client = ConnectorTestUtils.defaultMockClient();
        
        HttpRequestExecutor requestExecutor = mock(HttpRequestExecutor.class);
        when(client.getRequestExecutor()).thenReturn(requestExecutor);
        
        BasicTicketCollection ticketCollection = mock(BasicTicketCollection.class);
        Unmarshaller ticketCollectionUnmarshaller = ConnectorTestUtils.marshallerThatReturns(ticketCollection);
        when(UnmarshallerFactory.getMapper(BasicTicketCollection.class))
                .thenReturn(ticketCollectionUnmarshaller);
        
        TicketConnector connector = new TicketConnector(client);
        
        // act
        List<BasicTicket> tickets = connector.forDepartment(12,
                DepartmentTicketRequest.where().ticketStatusId(7));
        
        // assert
        verify(requestExecutor).execute(argThat(queryStartsWith("/Tickets/Ticket/ListAll/12/7/-1/-1&")));
    }
    
    @Test
    public void testGetWithTicketFilterTicketStatusesConstructsUri() throws Exception {
        // arrange
        KayakoClient client = ConnectorTestUtils.defaultMockClient();
        
        HttpRequestExecutor requestExecutor = mock(HttpRequestExecutor.class);
        when(client.getRequestExecutor()).thenReturn(requestExecutor);
        
        BasicTicketCollection ticketCollection = mock(BasicTicketCollection.class);
        Unmarshaller ticketCollectionUnmarshaller = ConnectorTestUtils.marshallerThatReturns(ticketCollection);
        when(UnmarshallerFactory.getMapper(BasicTicketCollection.class))
                .thenReturn(ticketCollectionUnmarshaller);
        
        TicketConnector connector = new TicketConnector(client);
        
        // act
        List<BasicTicket> tickets = connector.forDepartment(12,
                DepartmentTicketRequest.where().ticketStatusId(7, 2, 1));
        
        // assert
        verify(requestExecutor).execute(argThat(queryMatches("^/Tickets/Ticket/ListAll/12/((1|2|7),?){3}/-1/-1&.*")));
    }
    
    @Test
    public void testGetWithTicketFilterTicketStatuses2ConstructsUri() throws Exception {
        // arrange
        KayakoClient client = ConnectorTestUtils.defaultMockClient();
        
        HttpRequestExecutor requestExecutor = mock(HttpRequestExecutor.class);
        when(client.getRequestExecutor()).thenReturn(requestExecutor);
        
        BasicTicketCollection ticketCollection = mock(BasicTicketCollection.class);
        Unmarshaller ticketCollectionUnmarshaller = ConnectorTestUtils.marshallerThatReturns(ticketCollection);
        when(UnmarshallerFactory.getMapper(BasicTicketCollection.class))
                .thenReturn(ticketCollectionUnmarshaller);
        
        TicketConnector connector = new TicketConnector(client);
        
        // act
        List<BasicTicket> tickets = connector.forDepartment(12,
                DepartmentTicketRequest.where().ticketStatusId(7).ticketStatusId(2, 1));
        
        // assert
        verify(requestExecutor).execute(argThat(queryMatches("^/Tickets/Ticket/ListAll/12/((7|1|2),?){3}/-1/-1&.*")));
    }
    
    @Test
    public void testGetWithTicketFilterTicketStatusesAndDuplicatesConstructsUri() throws Exception {
        // arrange
        KayakoClient client = ConnectorTestUtils.defaultMockClient();
        
        HttpRequestExecutor requestExecutor = mock(HttpRequestExecutor.class);
        when(client.getRequestExecutor()).thenReturn(requestExecutor);
        
        BasicTicketCollection ticketCollection = mock(BasicTicketCollection.class);
        Unmarshaller ticketCollectionUnmarshaller = ConnectorTestUtils.marshallerThatReturns(ticketCollection);
        when(UnmarshallerFactory.getMapper(BasicTicketCollection.class))
                .thenReturn(ticketCollectionUnmarshaller);
        
        TicketConnector connector = new TicketConnector(client);
        
        // act
        List<BasicTicket> tickets = connector.forDepartment(12,
                DepartmentTicketRequest.where().ticketStatusId(7, 7, 2, 1, 1));
        
        // assert
        verify(requestExecutor).execute(argThat(queryMatches("^/Tickets/Ticket/ListAll/12/((1|2|7),?){3}/-1/-1&.*")));
    }
    
    @Test
    public void testGetWithTicketFilterOwnerStaffConstructsUri() throws Exception {
        // arrange
        KayakoClient client = ConnectorTestUtils.defaultMockClient();
        
        HttpRequestExecutor requestExecutor = mock(HttpRequestExecutor.class);
        when(client.getRequestExecutor()).thenReturn(requestExecutor);
        
        BasicTicketCollection ticketCollection = mock(BasicTicketCollection.class);
        Unmarshaller ticketCollectionUnmarshaller = ConnectorTestUtils.marshallerThatReturns(ticketCollection);
        when(UnmarshallerFactory.getMapper(BasicTicketCollection.class))
                .thenReturn(ticketCollectionUnmarshaller);
        
        TicketConnector connector = new TicketConnector(client);
        
        // act
        List<BasicTicket> tickets = connector.forDepartment(12,
                DepartmentTicketRequest.where().ownerStaffId(9));
        
        // assert
        verify(requestExecutor).execute(argThat(queryStartsWith("/Tickets/Ticket/ListAll/12/-1/9/-1&")));
    }
    
    @Test
    public void testGetWithTicketFilterOwnerStaffIdsConstructsUri() throws Exception {
        // arrange
        KayakoClient client = ConnectorTestUtils.defaultMockClient();
        
        HttpRequestExecutor requestExecutor = mock(HttpRequestExecutor.class);
        when(client.getRequestExecutor()).thenReturn(requestExecutor);
        
        BasicTicketCollection ticketCollection = mock(BasicTicketCollection.class);
        Unmarshaller ticketCollectionUnmarshaller = ConnectorTestUtils.marshallerThatReturns(ticketCollection);
        when(UnmarshallerFactory.getMapper(BasicTicketCollection.class))
                .thenReturn(ticketCollectionUnmarshaller);
        
        TicketConnector connector = new TicketConnector(client);
        
        // act
        List<BasicTicket> tickets = connector.forDepartment(12,
                DepartmentTicketRequest.where().ownerStaffId(7, 211, 18));
        
        // assert
        verify(requestExecutor).execute(argThat(queryMatches("^/Tickets/Ticket/ListAll/12/-1/((18|7|211),?){3}/-1&.*")));
    }
    
    @Test
    public void testGetWithTicketFilterOwnerStaffIds2ConstructsUri() throws Exception {
        // arrange
        KayakoClient client = ConnectorTestUtils.defaultMockClient();
        
        HttpRequestExecutor requestExecutor = mock(HttpRequestExecutor.class);
        when(client.getRequestExecutor()).thenReturn(requestExecutor);
        
        BasicTicketCollection ticketCollection = mock(BasicTicketCollection.class);
        Unmarshaller ticketCollectionUnmarshaller = ConnectorTestUtils.marshallerThatReturns(ticketCollection);
        when(UnmarshallerFactory.getMapper(BasicTicketCollection.class))
                .thenReturn(ticketCollectionUnmarshaller);
        
        TicketConnector connector = new TicketConnector(client);
        
        // act
        List<BasicTicket> tickets = connector.forDepartment(12,
                DepartmentTicketRequest.where().ownerStaffId(7).ownerStaffId(211, 18));
        
        // assert
        verify(requestExecutor).execute(argThat(queryMatches("^/Tickets/Ticket/ListAll/12/-1/((7|18|211),?){3}/-1&.*")));
    }
    
    @Test
    public void testGetWithTicketFilterOwnerStaffIdsAndDuplicatesConstructsUri() throws Exception {
        // arrange
        KayakoClient client = ConnectorTestUtils.defaultMockClient();
        
        HttpRequestExecutor requestExecutor = mock(HttpRequestExecutor.class);
        when(client.getRequestExecutor()).thenReturn(requestExecutor);
        
        BasicTicketCollection ticketCollection = mock(BasicTicketCollection.class);
        Unmarshaller ticketCollectionUnmarshaller = ConnectorTestUtils.marshallerThatReturns(ticketCollection);
        when(UnmarshallerFactory.getMapper(BasicTicketCollection.class))
                .thenReturn(ticketCollectionUnmarshaller);
        
        TicketConnector connector = new TicketConnector(client);
        
        // act
        List<BasicTicket> tickets = connector.forDepartment(12,
                DepartmentTicketRequest.where().ownerStaffId(8, 8, 8, 17));
        
        // assert
        verify(requestExecutor).execute(argThat(queryMatches("^/Tickets/Ticket/ListAll/12/-1/((17|8),?){2}/-1&.*")));
    }
    
    @Test
    public void testGetWithTicketMultipleFiltersConstructsUri() throws Exception {
        // arrange
        KayakoClient client = ConnectorTestUtils.defaultMockClient();
        
        HttpRequestExecutor requestExecutor = mock(HttpRequestExecutor.class);
        when(client.getRequestExecutor()).thenReturn(requestExecutor);
        
        BasicTicketCollection ticketCollection = mock(BasicTicketCollection.class);
        Unmarshaller ticketCollectionUnmarshaller = ConnectorTestUtils.marshallerThatReturns(ticketCollection);
        when(UnmarshallerFactory.getMapper(BasicTicketCollection.class))
                .thenReturn(ticketCollectionUnmarshaller);
        
        TicketConnector connector = new TicketConnector(client);
        
        // act
        List<BasicTicket> tickets = connector.forDepartment(12,
                DepartmentTicketRequest.where()
                        .ownerStaffId(1)
                        .userId(9)
                        .ticketStatusId(2, 6)
                        .userId(0));
        
        // assert
        verify(requestExecutor).execute(argThat(queryStartsWith("/Tickets/Ticket/ListAll/12/2,6/1/9,0&")));
    }
    
}
