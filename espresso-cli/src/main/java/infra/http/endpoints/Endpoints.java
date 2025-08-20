package infra.http.endpoints;

public final class Endpoints {
    private Endpoints() {
    }

    public static final String API_BASE_URL = "http://3.65.147.49";

    public static String getPostsEndpoint() {
        return API_BASE_URL + "/posts";
    }

    public static String getUsersEndpoint() {
        return API_BASE_URL + "/users";
    }

    public static String getCommentsEndpoint() {
        return API_BASE_URL + "/comments";
    }

    public static String getSubredditsEndpoint() {
        return API_BASE_URL + "/subreddits";
    }

    public static String getFiltersEndpoint() {
        return API_BASE_URL + "/filters";
    }
}
