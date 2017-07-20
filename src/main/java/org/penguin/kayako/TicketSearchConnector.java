package org.penguin.kayako;

import java.util.List;

import org.penguin.kayako.domain.Ticket;
import org.penguin.kayako.domain.TicketCollection;
import org.penguin.kayako.exception.ApiRequestException;
import org.penguin.kayako.exception.ApiResponseException;

/**
 * Wrapper for any API calls specific to ticket search.
 *
 * @author fatroom
 */
public class TicketSearchConnector extends AbstractConnector {

    protected TicketSearchConnector(final KayakoClient client) {
        super(client);
    }

    /**
     * Run a search on tickets. You can combine the search factors to make the search span multiple areas.
     *
     * @param request ticket search request
     * @return collection of {@link Ticket} matching specified request
     * @throws ApiRequestException  A wrapped exception of anything that went wrong sending the request to kayako
     * @throws ApiResponseException A wrapped exception of anything that went wrong when handling the response from kayako
     */
    public List<Ticket> search(final TicketSearchRequest request) throws ApiRequestException, ApiResponseException {
        request.validate();
        ApiRequest apiRequest = getApiRequest().withPostParam("query", request.getQuery());
        if (request.isTicketId()) {
            apiRequest = apiRequest.withPostParam("ticketid", 1);
        }
        if (request.isContents()) {
            apiRequest = apiRequest.withPostParam("contents", 1);
        }
        if (request.isAuthor()) {
            apiRequest = apiRequest.withPostParam("author", 1);
        }
        if (request.isEmail()) {
            apiRequest = apiRequest.withPostParam("email", 1);
        }
        if (request.isCreatorEmail()) {
            apiRequest = apiRequest.withPostParam("creatoremail", 1);
        }
        if (request.isFullname()) {
            apiRequest = apiRequest.withPostParam("fullname", 1);
        }
        if (request.isNotes()) {
            apiRequest = apiRequest.withPostParam("notes", 1);
        }
        if (request.isUserGroup()) {
            apiRequest = apiRequest.withPostParam("usergroup", 1);
        }
        if (request.isUserOrganization()) {
            apiRequest = apiRequest.withPostParam("userorganization", 1);
        }
        if (request.isUser()) {
            apiRequest = apiRequest.withPostParam("user", 1);
        }
        if (request.isTags()) {
            apiRequest = apiRequest.withPostParam("tags", 1);
        }
        return apiRequest
                .post()
                .as(TicketCollection.class)
                .getTickets();
    }

    @Override
    protected ApiRequest getApiRequest() {
        ApiRequest request =  super.getApiRequest();
        return request
                .withPath("Tickets")
                .withPath("TicketSearch");
    }

    public static class TicketSearchRequest extends AbstractRequest {
        private String query;
        private boolean ticketId;
        private boolean contents;
        private boolean author;
        private boolean email;
        private boolean creatorEmail;
        private boolean fullname;
        private boolean notes;
        private boolean userGroup;
        private boolean userOrganization;
        private boolean user;
        private boolean tags;

        private TicketSearchRequest() {

        }
        
        private TicketSearchRequest(final TicketSearchRequest request) {
            query = request.getQuery();
            ticketId = request.isTicketId();
            contents = request.isContents();
            author = request.isAuthor();
            email = request.isEmail();
            creatorEmail = request.isCreatorEmail();
            fullname = request.isFullname();
            notes = request.isNotes();
            userGroup = request.isUserGroup();
            userOrganization = request.isUserOrganization();
            user = request.isUser();
            tags = request.isTags();
        }

        /**
         * The Search Query. Required parameter.
         *
         * @param query search query
         * @return request instance
         */
        public TicketSearchRequest query(final String query) {
            checkNotNull(query);
            TicketSearchRequest request = new TicketSearchRequest(this);
            request.query = query;
            return request;
        }

        /**
         * Search the Ticket ID &amp; Mask ID.
         *
         * @return request instance
         */
        public TicketSearchRequest ticketId() {
            TicketSearchRequest request = new TicketSearchRequest(this);
            request.ticketId = true;
            return request;
        }

        /**
         * Search the Ticket Post Contents.
         *
         * @return request instance
         */
        public TicketSearchRequest contents() {
            TicketSearchRequest request = new TicketSearchRequest(this);
            request.contents = true;
            return request;
        }

        /**
         * Search the Full Name &amp; Email of author.
         *
         * @return request instance
         */
        public TicketSearchRequest author() {
            TicketSearchRequest request = new TicketSearchRequest(this);
            request.author = true;
            return request;
        }

        /**
         * Search the Email Address (Ticket &amp; Posts).
         *
         * @return request instance
         */
        public TicketSearchRequest email() {
            TicketSearchRequest request = new TicketSearchRequest(this);
            request.email = true;
            return request;
        }

        /**
         * Search the Email Address (only Tickets).
         *
         * @return request instance
         */
        public TicketSearchRequest creatorEmail() {
            TicketSearchRequest request = new TicketSearchRequest(this);
            request.creatorEmail = true;
            return request;
        }

        /**
         * Search the Full Name.
         *
         * @return request instance
         */
        public TicketSearchRequest fullname() {
            TicketSearchRequest request = new TicketSearchRequest(this);
            request.fullname = true;
            return request;
        }

        /**
         * Search the Ticket Notes.
         *
         * @return request instance
         */
        public TicketSearchRequest notes() {
            TicketSearchRequest request = new TicketSearchRequest(this);
            request.notes = true;
            return request;
        }

        /**
         * Search the User Group.
         *
         * @return request instance
         */
        public TicketSearchRequest userGroup() {
            TicketSearchRequest request = new TicketSearchRequest(this);
            request.userGroup = true;
            return request;
        }

        /**
         * Search the User Organization.
         *
         * @return request instance
         */
        public TicketSearchRequest userOrganization() {
            TicketSearchRequest request = new TicketSearchRequest(this);
            request.userOrganization = true;
            return request;
        }

        /**
         * Search the User (Full Name, Email).
         *
         * @return request instance
         */
        public TicketSearchRequest user() {
            TicketSearchRequest request = new TicketSearchRequest(this);
            request.user = true;
            return request;
        }

        /**
         * Search the Ticket Tags.
         *
         * @return request instance
         */
        public TicketSearchRequest tags() {
            TicketSearchRequest request = new TicketSearchRequest(this);
            request.tags = true;
            return request;
        }

        public static TicketSearchRequest where() {
            return new TicketSearchRequest();
        }

        @Override
        protected void validate() throws ApiRequestException {
            if (query == null) {
                throw new ApiRequestException(new IllegalStateException("Invalid request configuration. Search query is required"));
            }
        }

        private String getQuery() {
            return query;
        }

        private boolean isTicketId() {
            return ticketId;
        }

        private boolean isContents() {
            return contents;
        }

        private boolean isAuthor() {
            return author;
        }

        private boolean isEmail() {
            return email;
        }

        private boolean isCreatorEmail() {
            return creatorEmail;
        }

        private boolean isFullname() {
            return fullname;
        }

        private boolean isNotes() {
            return notes;
        }

        private boolean isUserGroup() {
            return userGroup;
        }

        private boolean isUserOrganization() {
            return userOrganization;
        }

        private boolean isUser() {
            return user;
        }

        private boolean isTags() {
            return tags;
        }
    }
}
