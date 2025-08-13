package service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import infra.http.ApiClient;
import infra.http.ApiResult;
import infra.http.endpoints.Endpoints;
import objects.domain.Comment;
import objects.domain.Vote;
import objects.dto.CommentRequestDto;
import objects.dto.VoteRequestDto;
import service.CommentService;

public class CommentServiceImpl implements CommentService {
    private final ApiClient apiClient;
    private final String baseUrl;

    public CommentServiceImpl(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.baseUrl = Endpoints.getCommentsEndpoint();
    }

    @Override
    public ApiResult<Comment> getById(Long id) {
        try {
            String url = baseUrl + "/" + id;
            return apiClient.get(url, new TypeReference<ApiResult<Comment>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to fetch comment: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<Comment> update(Long id, CommentRequestDto dto) {
        try {
            String url = baseUrl + "/" + id;
            return apiClient.put(url, dto, new TypeReference<ApiResult<Comment>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to update comment: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<Void> delete(Long id) {
        try {
            String url = baseUrl + "/" + id;
            return apiClient.delete(url, new TypeReference<ApiResult<Void>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to delete comment: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<Vote> voteComment(Long id, VoteRequestDto dto) {
        try {
            String url = baseUrl + "/" + id + "/vote";
            return apiClient.put(url, dto, new TypeReference<ApiResult<Vote>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to vote comment: " + e.getMessage(), 500);
        }
    }
}
