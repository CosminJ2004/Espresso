package com.example.backend.service;

import com.example.backend.dto.FilterResponseDto;
import com.example.backend.repository.FilterRepository;
import com.example.backend.model.Filter;
import com.example.backend.util.logger.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilterService {

    private final FilterRepository filterRepository;
    private final Logger log;

    @Autowired
    public FilterService(FilterRepository filterRepository, Logger log) {
        this.filterRepository = filterRepository;
        this.log = log;
    }

    public List<FilterResponseDto> getAllFilters() {
        log.info("Fetching all filters");
        List<Filter> filters = filterRepository.findAll();
        log.info("Retrieved " + filters.size() + " filters");
        return filters.stream()
                .map(filter -> new FilterResponseDto(
                        filter.getId(),
                        filter.getName(),
                        filter.getDisplayName()
                ))
                .collect(Collectors.toList());
    }
}