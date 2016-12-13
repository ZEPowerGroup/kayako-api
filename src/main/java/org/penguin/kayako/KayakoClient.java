package org.penguin.kayako;

/**
 * Kayako client interface. This is the starting point for any developers looking to use this kayako api wrapper.
 * 
 * @author raynerw
 * @author fatroom
 * 
 */
public class KayakoClient {
    private final String apiKey;
    private final String apiSecret;
    private final UriBuilder baseURI;
    private HttpRequestExecutor requestExecutor;
    
    /**
     * Initialize a new instance of the kayako client with your kayako address and security details.
     * 
     * @param apiKey
     *            The api key listed in your Kayako admin interface.
     * @param apiSecret
     *            The api secret listed in your Kayako admin interface.
     * @param baseURI
     *            The base address for your kayako installation. This should not include a scheme or a port. This
     *            wrapper assumes your api module is installed in the default top-level directory of your kayako
     *            installation (/api).
     * 
     *            Example address: my.kayakoinstallation.com
     */
    public KayakoClient(String apiKey, String apiSecret, String baseURI) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.baseURI = new UriBuilder(baseURI).path("api").path("index.php");
        this.requestExecutor = new HttpRequestExecutorImpl();
    }

    /**
     * Interact with kayako departments
     * 
     * @return An instance of {@link DepartmentConnector} that allows you to fetch departments.
     */
    public APIConnectionTestConnector testAPI() {
        return new APIConnectionTestConnector(this);
    }
    
    /**
     * Interact with kayako departments
     *
     * @return An instance of {@link DepartmentConnector} that allows you to fetch departments.
     */
    public DepartmentConnector departments() {
        return new DepartmentConnector(this);
    }

    /**
     * Interact with kayako tickets
     * 
     * @return An instance of {@link TicketConnector} that allows you to interact with Tickets.
     */
    public TicketConnector tickets() {
        return new TicketConnector(this);
    }
    
    /**
     * Interact with kayako ticket attachments.
     * 
     * @return An instance of {@link AttachmentConnector} that allows you to interact with ticket attachments.
     */
    public AttachmentConnector attachments() {
        return new AttachmentConnector(this);
    }
    
    /**
     * Interact with kayako ticket custom fields.
     * 
     * @return An instance of {@link TicketCustomFieldConnector} that allows you to fetch ticket custom fields.
     */
    public TicketCustomFieldConnector ticketCustomFields() {
        return new TicketCustomFieldConnector(this);
    }
    
    /**
     * Interact with kayako ticket notes.
     * 
     * @return An instance of {@link TicketNoteConnector} that allows you to interact with ticket notes.
     */
    public TicketNoteConnector notes() {
        return new TicketNoteConnector(this);
    }
    
    /**
     * Interact with kayako ticket statuses.
     * 
     * @return An instance of {@link TicketStatusConnector} that allows you to fetch ticket statuses
     */
    public TicketStatusConnector statuses() {
        return new TicketStatusConnector(this);
    }
    
    /**
     * Interact with kayako ticket types.
     * 
     * @return An instance of {@link TicketTypeConnector} that allows you to fetch ticket types
     */
    public TicketTypeConnector types() {
        return new TicketTypeConnector(this);
    }
    
    /**
     * Interact with kayako ticket priorities.
     * 
     * @return An instance of {@link TicketPriorityConnector} that allows you to fetch ticket types
     */
    public TicketPriorityConnector priorities() {
        return new TicketPriorityConnector(this);
    }
    
    /**
     * Perform ticket search in Kayako.
     * 
     * @return An instance of {@link TicketSearchConnector} that allows you to fetch ticket according to search query.
     */
    public TicketSearchConnector ticketSearch() {
        return new TicketSearchConnector(this);
    }
    
    /**
     * Interact with kayako ticket posts.
     * 
     * @return An instance of {@link TicketPostConnector} that allows you to interact with ticket posts.
     */
    public TicketPostConnector posts() {
        return new TicketPostConnector(this);
    }
    
    /**
     * Interact with kayako users.
     *
     * @return An instance of {@link UserConnector} that allows you to interact with users.
     */
    public UserConnector users() {
        return new UserConnector(this);
    }

    protected String getApiKey() {
        return apiKey;
    }
    
    protected String getApiSecret() {
        return apiSecret;
    }
    
    protected UriBuilder getBaseURI() {
        return baseURI;
    }
    
    protected HttpRequestExecutor getRequestExecutor() {
        return requestExecutor;
    }
    
    protected void setRequestExecutor(HttpRequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }
    
}
