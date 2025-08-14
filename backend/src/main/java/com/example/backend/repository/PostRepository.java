package com.example.backend.repository;

import com.example.backend.model.Post;
import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findAllByOrderByCreatedAtDesc();

    @Query(value = "SELECT 1", nativeQuery = true)
    String getLatency();
}
