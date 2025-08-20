package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import infra.http.ApiClient;
import service.CommentService;
import service.FilterService;
import service.PostService;
import service.UserService;
import service.impl.CommentServiceImpl;
import service.impl.FilterServiceImpl;
import service.impl.PostServiceImpl;
import service.impl.UserServiceImpl;

import java.time.Duration;

import static infra.util.AppConstants.DEFAULT_TIMEOUT_SECONDS;

public final class Wiring {
    //object mapper , apiCLient, services
    private final ObjectMapper objectMapper;
    //objectMapper.registerModule(new JavaTimeModule()); // serializeaza clase din java.time
    private final ApiClient apiClient;
    private final UserService userService;
    private final CommentService commentService;
    private final PostService postService;
    private final FilterService filterService;
    //constructor wiring
    public Wiring(){
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.apiClient = new ApiClient(objectMapper, Duration.ofSeconds(DEFAULT_TIMEOUT_SECONDS));
        this.userService = new UserServiceImpl(apiClient);
        this.commentService = new CommentServiceImpl(apiClient);
        this.postService = new PostServiceImpl(apiClient);
        this.filterService = new FilterServiceImpl(apiClient);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public UserService getUserService() {
        return userService;
    }

    public CommentService getCommentService() {
        return commentService;
    }

    public PostService getPostService() {
        return postService;
    }

    public FilterService getFilterService() {
        return filterService;
    }
}