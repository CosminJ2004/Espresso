package com.example.backend.repository;

import com.example.backend.model.Filter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {
    Optional<Filter> findByName(String code);
}