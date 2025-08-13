package service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import infra.http.ApiClient;
import infra.http.ApiResult;
import infra.http.endpoints.Endpoints;
import objects.domain.Comment;
import objects.domain.Post;
import objects.domain.Vote;
import objects.dto.CommentRequestDto;
import objects.dto.PostRequestDto;
import objects.dto.PostRequestWithImageDto;
import objects.dto.VoteRequestDto;
import service.PostService;

import java.util.List;

public class PostServiceImpl implements PostService {
    private final ApiClient apiClient;
    private final String baseUrl;

    public PostServiceImpl(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.baseUrl = Endpoints.getPostsEndpoint();
    }

    @Override
    public ApiResult<List<Post>> getAll() {
        try {
            return apiClient.get(baseUrl, new TypeReference<ApiResult<List<Post>>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to fetch posts: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<Post> getById(Long id) {
        try {
            String url = baseUrl + "/" + id;
            return apiClient.get(url, new TypeReference<ApiResult<Post>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to fetch post: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<Post> create(PostRequestDto dto) {
        try {
            return apiClient.post(baseUrl, dto, new TypeReference<ApiResult<Post>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to create post: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<Post> createWithImage(PostRequestWithImageDto dto) {
        return null;
        //TO DO
    }

    @Override
    public ApiResult<Post> update(Long id, PostRequestDto dto) {
        try {
            String url = baseUrl + "/" + id;
            return apiClient.put(url, dto, new TypeReference<ApiResult<Post>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to update post: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<Void> delete(Long id) {
        try {
            String url = baseUrl + "/" + id;
            return apiClient.delete(url, new TypeReference<ApiResult<Void>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to delete post: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<Vote> votePost(Long id, VoteRequestDto dto) {
        try {
            String url = baseUrl + "/" + id + "/vote";
            return apiClient.put(url, dto, new TypeReference<ApiResult<Vote>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to vote post: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<List<Comment>> getCommentsByPostId(Long id) {
        try {
            String url = baseUrl + "/" + id + "/comments";
            return apiClient.get(url, new TypeReference<ApiResult<List<Comment>>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to fetch comments: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<Comment> addComment(Long postId, CommentRequestDto dto) {
        try {
            String url = baseUrl + "/" + postId + "/comments";
            return apiClient.post(url, dto, new TypeReference<ApiResult<Comment>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to add comment: " + e.getMessage(), 500);
        }
    }
}