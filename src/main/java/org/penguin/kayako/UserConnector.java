package org.penguin.kayako;

import java.util.List;

import org.penguin.kayako.domain.User;
import org.penguin.kayako.domain.UserCollection;
import org.penguin.kayako.exception.ApiRequestException;
import org.penguin.kayako.exception.ApiResponseException;

/**
 * Wrapper for any API calls specific to users.
 *
 * @author eugene
 */
public class UserConnector extends AbstractConnector {

    protected UserConnector(final KayakoClient client) {
        super(client);
    }

    /**
     * Retrieve a list of all the users in the help desk.
     *
     * @return An collection of users known in system
     * @throws ApiResponseException A wrapped exception of anything that went wrong when handling the response from kayako
     * @throws ApiRequestException  A wrapped exception of anything that went wrong sending the request to kayako
     */
    public List<User> list() throws ApiRequestException, ApiResponseException {
        return getApiRequest()
            .withPath("Filter")
            .get()
            .as(UserCollection.class)
            .getUsers();
    }

    /**
     * Retrieve the user identified by id.
     *
     * @param userId user unique numeric identifier (ID)
     * @return An collection of with requested users
     * @throws ApiResponseException A wrapped exception of anything that went wrong when handling the response from kayako
     * @throws ApiRequestException  A wrapped exception of anything that went wrong sending the request to kayako
     */
    public List<User> forId(final int userId) throws ApiRequestException, ApiResponseException {
        return getApiRequest()
            .withPathRaw(String.valueOf(userId))
            .get()
            .as(UserCollection.class)
            .getUsers();
    }

    /**
     * Retrieve a list of all the users starting from a marker (user id) till the item fetch limit is reached
     * (by default this is 1000).
     *
     * @param request fetching request
     * @return collection of {@link User}s
     * @throws ApiResponseException
     *             A wrapped exception of anything that went wrong when handling the response from kayako.
     * @throws ApiRequestException
     *             A wrapped exception of anything that went wrong sending the request to kayako.
     */
    public List<User> fetch(final UserFetchRequest request) throws ApiRequestException, ApiResponseException {
        request.validate();
        ApiRequest apiRequest = getApiRequest()
            .withPath("Filter")
            .withPathRaw(String.valueOf(request.getMarker()));
        if (null != request.getLimit()) {
            apiRequest = apiRequest.withPathRaw(String.valueOf(request.getLimit()));
        }
        return apiRequest
            .get()
            .as(UserCollection.class)
            .getUsers();
    }

    @Override
    protected ApiRequest getApiRequest() {
        final ApiRequest request = super.getApiRequest();
        return request
            .withPath("Base")
            .withPath("User");
    }

    /**
     * This request is to retrieve a list of all the users starting from a marker (user id) till the item fetch limit
     * is reached (by default this is 1000).
     */
    public static class UserFetchRequest extends AbstractRequest {
        private Integer marker;
        private Integer limit;

        private UserFetchRequest() {}

        private UserFetchRequest(final UserFetchRequest request) {
            this.marker = request.getMarker();
            this.limit = request.getLimit();
        }

        /**
         * Fetching users starting from a marker (user ID).
         *
         * @param marker the marker (user ID)
         * @return request instance
         */
        public UserFetchRequest marker(final int marker) {
            UserFetchRequest request = new UserFetchRequest(this);
            request.marker = marker;
            return request;
        }

        /**
         * Fetching user till the limit is reached (by default this is 1000).
         *
         * @param limit the fetch limit
         * @return request instance
         * @throws ApiRequestException in case argument is null
         */
        public UserFetchRequest limit(final int limit) throws ApiRequestException {
            UserFetchRequest request = new UserFetchRequest(this);
            request.limit = limit;
            return request;
        }

        public static UserFetchRequest where() {
            return new UserFetchRequest();
        }

        private Integer getMarker() {
            return marker;
        }

        private Integer getLimit() {
            return limit;
        }


        @Override
        protected void validate() throws ApiRequestException {
            if (marker == null) {
                throw new ApiRequestException(new IllegalStateException("Invalid request configuration. Marker is required"));
            }
        }
    }
}
