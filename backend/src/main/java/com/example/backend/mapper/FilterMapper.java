package com.example.backend.mapper;

import com.example.backend.dto.FilterResponseDto;
import com.example.backend.model.Filter;

public final class FilterMapper {

    private FilterMapper() {
    }

    public static FilterResponseDto toDto(Filter filter) {
        if (filter == null) {
            return null;
        }
        return new FilterResponseDto(
                filter.getId(),
                filter.getName(),
                filter.getDisplayName()
        );
    }
}