package com.example.backend.controller;

import com.example.backend.dto.FilterResponseDto;
import com.example.backend.service.FilterService;
import com.example.backend.util.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/filters")
public class FilterController {

    private final FilterService filterService;

    @Autowired
    public FilterController(FilterService filterService) {
        this.filterService = filterService;
    }

    @GetMapping
    public ResponseEntity<Response<List<FilterResponseDto>>> getFilters() {
        return Response.ok(filterService.getAllFilters());
    }
}
