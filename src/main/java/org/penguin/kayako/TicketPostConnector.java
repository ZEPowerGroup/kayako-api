package org.penguin.kayako;

import java.util.List;

import org.penguin.kayako.domain.Post;
import org.penguin.kayako.domain.PostCollection;
import org.penguin.kayako.exception.ApiRequestException;
import org.penguin.kayako.exception.ApiResponseException;

/**
 * Wrapper for any API calls specific to ticket posts.
 *
 * @author fatroom
 */
public class TicketPostConnector extends AbstractConnector {
    protected TicketPostConnector(final KayakoClient client) {
        super(client);
    }

    /**
     * Retrieve a list of all the ticket posts that belong to a ticket given ticket's id.
     *
     * @param ticketId The unique numeric identifier of the ticket
     * @return collection of {@link Post} for specified ticket
     * @throws ApiResponseException
     *             A wrapped exception of anything that went wrong when handling the response from kayako.
     * @throws ApiRequestException
     *             A wrapped exception of anything that went wrong sending the request to kayako.
     */
    public List<Post> forTicket(final int ticketId) throws ApiRequestException, ApiResponseException {
        return getApiRequest()
                .withPath("ListAll")
                .withPathRaw(String.valueOf(ticketId))
                .get()
                .as(PostCollection.class)
                .getPosts();
    }

    /**
     * Retrieve the post identified by id that belong to the specified ticket.
     *
     * @param ticketId The unique numeric identifier of the ticket
     * @param postId The unique numeric identifier of the ticket post
     * @return collection of {@link Post} for specified ticket and id
     * @throws ApiResponseException
     *             A wrapped exception of anything that went wrong when handling the response from kayako.
     * @throws ApiRequestException
     *             A wrapped exception of anything that went wrong sending the request to kayako.
     */
    public List<Post> forId(final int ticketId, final int postId) throws ApiRequestException, ApiResponseException {
        return getApiRequest()
                .withPathRaw(String.valueOf(ticketId))
                .withPathRaw(String.valueOf(postId))
                .get()
                .as(PostCollection.class)
                .getPosts();
    }

    /**
     * A new post to an existing ticket.
     *
     * @param request post creation request ({@link PostCreateRequest})
     * @return collection with created {@link Post}
     * @throws ApiResponseException
     *             A wrapped exception of anything that went wrong when handling the response from kayako.
     * @throws ApiRequestException
     *             A wrapped exception of anything that went wrong sending the request to kayako.
     */
    public List<Post> create(final PostCreateRequest request) throws ApiRequestException, ApiResponseException {
        request.validate();
        ApiRequest apiRequest = getApiRequest()
                .withPostParam("ticketid", request.getTicketId())
                .withPostParam("contents", request.getContents());
        if (request.getUserId() != null) {
            apiRequest = apiRequest.withPostParam("userid", request.getUserId());
        }
        if (request.getStaffId() != null) {
            apiRequest = apiRequest.withPostParam("staffid", request.getStaffId());
            if (request.getPostPrivate()) {
                apiRequest = apiRequest.withPostParam("isprivate", 1);
            } else {
                apiRequest = apiRequest.withPostParam("isprivate", 0);
            }
        }
        return apiRequest
                .post()
                .as(PostCollection.class)
                .getPosts();
    }

    /**
     * Delete a ticket post identified by id which belongs to a specified ticket.
     *
     * @param ticketId The unique numeric identifier of the ticket
     * @param postId The unique numeric identifier of the ticket post
     * @throws ApiResponseException
     *             A wrapped exception of anything that went wrong when handling the response from kayako.
     * @throws ApiRequestException
     *             A wrapped exception of anything that went wrong sending the request to kayako.
     */
    public void delete(final int ticketId, final int postId) throws ApiRequestException, ApiResponseException {
        getApiRequest()
                .withPathRaw(String.valueOf(ticketId))
                .withPathRaw(String.valueOf(postId))
                .delete();
    }

    @Override
    protected ApiRequest getApiRequest() {
        ApiRequest request = super.getApiRequest();
        return request
                .withPath("Tickets")
                .withPath("TicketPost");
    }

    public static class PostCreateRequest extends AbstractRequest {
        private Integer ticketId;
        private String contents;
        private Integer staffId;
        private Integer userId;
        private Boolean postPrivate;

        private PostCreateRequest() {
            
        }
        
        private PostCreateRequest(final PostCreateRequest request) {
            ticketId = request.getTicketId();
            contents = request.getContents();
            staffId = request.getStaffId();
            userId = request.getUserId();
            postPrivate = request.getPostPrivate();
        }
        
        public static PostCreateRequest where() {
            return new PostCreateRequest();
        }

        /**
         * The unique numeric identifier of the ticket.
         * Parameter is required.
         *
         * @param id identifier.
         * @return request instance
         */
        public PostCreateRequest ticketId(final int id) {
            PostCreateRequest request = new PostCreateRequest(this);
            request.ticketId = id;
            return request;
        }

        /**
         * The ticket post contents.
         * Parameter is required.
         *
         * @param contents post contents.
         * @return request instance
         * @throws ApiRequestException in case contents is null
         */
        public PostCreateRequest contents(final String contents) throws ApiRequestException {
            checkNotNull(contents);
            PostCreateRequest request = new PostCreateRequest(this);
            request.contents = contents;
            return request;
        }

        /**
         * The Staff ID, if the ticket post is to be created as a staff.
         *
         * @param id staff identifier
         * @return request instance
         * @throws ApiRequestException in case user id already set in this request
         */
        public PostCreateRequest staffId(final int id) throws ApiRequestException {
            if (userId != null) {
                throwInvalidConfigurationException();
            }
            PostCreateRequest request = new PostCreateRequest(this);
            request.staffId = id;
            return request;
        }

        /**
         * The User ID, if the ticket post is to be created as a user.
         *
         * @param id user identifier
         * @return request instance
         * @throws ApiRequestException in case staff id already set in this request
         */
        public PostCreateRequest userId(final int id) throws ApiRequestException {
            if (staffId != null) {
                throwInvalidConfigurationException();
            }
            PostCreateRequest request = new PostCreateRequest(this);
            request.userId = id;
            return request;
        }

        /**
         * Indicates whether the ticket post is private (hidden from the customer) or not.
         * Applies only to post created by staff user.
         * Parameter is required in case post created by staff user.
         *
         * @param postPrivate indicator flag (true when post should be private, false otherwise)
         * @return request instance
         */
        public PostCreateRequest postPrivate(final boolean postPrivate) {
            PostCreateRequest request = new PostCreateRequest(this);
            request.postPrivate = postPrivate;
            return request;
        }

        @Override
        protected void validate() throws ApiRequestException {
            if (ticketId == null) {
                throwInvalidConfigurationException("Ticket id is required");
            }
            if (contents == null) {
                throwInvalidConfigurationException("Post contents is required");
            }
            if ((staffId == null) && (userId == null)) {
                throwInvalidConfigurationException("Staff id or user id is required");
            }
            if ((staffId != null) && (postPrivate == null)) {
                throwInvalidConfigurationException("Staff id specified. You should also specify private post or not");
            }
        }

        private Integer getTicketId() {
            return ticketId;
        }

        private String getContents() {
            return contents;
        }

        private Integer getStaffId() {
            return staffId;
        }

        private Integer getUserId() {
            return userId;
        }

        private Boolean getPostPrivate() {
            return postPrivate;
        }
    }

}
