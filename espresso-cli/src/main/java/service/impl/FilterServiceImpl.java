package service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import infra.http.ApiClient;
import infra.http.ApiResult;
import infra.http.endpoints.Endpoints;
import objects.domain.Filter;
import service.FilterService;

import java.util.List;

public class FilterServiceImpl implements FilterService {
    private final ApiClient apiClient;
    private final String baseUrl;

    public FilterServiceImpl(ApiClient apiClient){
        this.apiClient = apiClient;
        this.baseUrl = Endpoints.getFiltersEndpoint();
    }

    @Override
    public ApiResult<List<Filter>> getAll() {
        try {
            return apiClient.get(baseUrl, new TypeReference<ApiResult<List<Filter>>>() {});
        } catch (Exception e) {
            return ApiResult.error("Failed to fetch filters! Please try again later.", 500);
        }
    }

}
