package service;

import objects.domain.User;
import objects.dto.UserRequestDto;
import infra.http.ApiResult;

import java.util.List;

public interface UserService {
    ApiResult<User> getById(Long id);
    ApiResult<List<User>> getAll();
    ApiResult<User> create(UserRequestDto dto);
    ApiResult<User> login(UserRequestDto dto);
    ApiResult<User> findByUsername(String username);
    ApiResult<Void> delete(String username);
    ApiResult<User> update(String username, UserRequestDto dto);
}
