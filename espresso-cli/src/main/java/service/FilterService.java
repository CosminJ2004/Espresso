package service;

import infra.http.ApiResult;
import objects.domain.Filter;

import java.util.List;

public interface FilterService {
    ApiResult<List<Filter>> getAll();
}
