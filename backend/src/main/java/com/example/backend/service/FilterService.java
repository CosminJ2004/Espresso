package com.example.backend.service;

import com.example.backend.dto.FilterResponseDto;
import com.example.backend.repository.FilterRepository;
import com.example.backend.model.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterService {

    private final FilterRepository filterRepository;

    @Autowired
    public FilterService(FilterRepository filterRepository) {
        this.filterRepository = filterRepository;
    }

    public List<FilterResponseDto> getAllFilters() {
        List<Filter> filters = filterRepository.findAll();
        return filters.stream()
                .map(filter -> new FilterResponseDto(
                        filter.getId(),
                        filter.getName(),
                        filter.getDisplayName()
                ))
                .collect(Collectors.toList());
    }
}