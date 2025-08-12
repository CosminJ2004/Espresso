package service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import infra.http.ApiClient;
import infra.http.ApiResult;
import infra.http.endpoints.Endpoints;
import objects.domain.User;
import objects.dto.UserRequestDto;
import service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final ApiClient apiClient;
    private final String baseUrl;

    public UserServiceImpl(ApiClient apiClient) {
        this.apiClient = apiClient;
        this.baseUrl = Endpoints.getUsersEndpoint();
    }

    @Override
    public ApiResult<User> getById(Long id) {
        try {
            String url = baseUrl + "/" + id;
            return apiClient.get(url, new TypeReference<ApiResult<User>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to fetch user: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<List<User>> getAll() {
        try {
            return apiClient.get(baseUrl, new TypeReference<ApiResult<List<User>>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to fetch users: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<User> create(UserRequestDto dto) {
        try {
            return apiClient.post(baseUrl, dto, new TypeReference<ApiResult<User>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to create user: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<User> login(UserRequestDto dto) {
        try {
            String url = baseUrl + "/login";
            //System.out.println("Login URL: " + url);  // pentru debug sa vedem ce url construieste
            return apiClient.post(url, dto, new TypeReference<ApiResult<User>>() {});
        } catch (Exception e) {
            return ApiResult.error("Login failed: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<User> findByUsername(String username) {
        try {
            String url = baseUrl + "/search/" + username;
            return apiClient.get(url, new TypeReference<ApiResult<User>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to find user: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<Void> delete(String username) {
        try {
            String url = baseUrl + "/" + username;
            return apiClient.delete(url, new TypeReference<ApiResult<Void>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to delete user: " + e.getMessage(), 500);
        }
    }

    @Override
    public ApiResult<User> update(String username, UserRequestDto dto) {
        try {
            String url = baseUrl + "/" + username;
            return apiClient.put(url, dto, new TypeReference<ApiResult<User>>() {
            });
        } catch (Exception e) {
            return ApiResult.error("Failed to update user: " + e.getMessage(), 500);
        }
    }
}
