//package service.impl;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import infra.http.ApiClient;
//import infra.http.ApiResult;
//import infra.http.endpoints.Endpoints;
//import objects.domain.User;
//import objects.dto.UserRequestDto;
//import service.UserService;
//
//public class UserServiceImpl implements UserService {
//    private final ApiClient apiClient;
//    private final String baseUrl;
//
//    public UserServiceImpl(ApiClient apiClient) {
//        this.apiClient = apiClient;
//        this.baseUrl = Endpoints.getUsersEndpoint();
//    }
//
//    @Override
//    public ApiResult<User> login(UserRequestDto dto) {
//        try {
//            String url = baseUrl + "/login";
//            return apiClient.post(url, dto, new TypeReference<ApiResult<User>>() {});
//        } catch (Exception e) {
//            return ApiResult.error("Login failed: " + e.getMessage(), 500);
//        }
//    }
//}
