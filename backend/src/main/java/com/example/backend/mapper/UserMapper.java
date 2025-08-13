package com.example.backend.mapper;

import com.example.backend.dto.UserResponseDto;
import com.example.backend.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponseDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getCreatedAt()
        );
    }
}
