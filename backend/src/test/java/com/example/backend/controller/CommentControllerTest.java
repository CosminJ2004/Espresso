package com.example.backend.controller;

import com.example.backend.dto.CommentRequestDto;
import com.example.backend.dto.CommentResponseDto;
import com.example.backend.dto.VoteRequestDto;
import com.example.backend.dto.VoteResponseDto;
import com.example.backend.model.VoteType;
import com.example.backend.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getCommentById() throws Exception {
        Long commentId = 1L;
        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setId(commentId);
        responseDto.setParentId(1L);
        responseDto.setContent("Test comment");
        responseDto.setAuthor("testuser");
        responseDto.setPostId(1L);
        responseDto.setUpvotes(5L);
        responseDto.setDownvotes(2L);
        responseDto.setScore(3L);
        responseDto.setUserVote(VoteType.up);
        responseDto.setCreatedAt(LocalDateTime.now());
        responseDto.setUpdatedAt(LocalDateTime.now());
        responseDto.setReplies(new ArrayList<>());

        when(commentService.getCommentById(commentId)).thenReturn(responseDto);

        mockMvc.perform(get("/comments/{id}", commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(commentId))
                .andExpect(jsonPath("$.data.parentId").value(1))
                .andExpect(jsonPath("$.data.content").value("Test comment"))
                .andExpect(jsonPath("$.data.author").value("testuser"))
                .andExpect(jsonPath("$.data.upvotes").value(5))
                .andExpect(jsonPath("$.data.downvotes").value(2))
                .andExpect(jsonPath("$.data.score").value(3))
                .andExpect(jsonPath("$.data.userVote").value("up"))
                .andExpect(jsonPath("$.data.replies").isArray())
                .andExpect(jsonPath("$.data.replies").isEmpty());
    }

    @Test
    void updateComment() throws Exception {
        Long commentId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("Updated comment content");

        CommentResponseDto responseDto = new CommentResponseDto();
        responseDto.setId(commentId);
        responseDto.setContent("Updated comment content");

        when(commentService.updateComment(commentId, requestDto)).thenReturn(responseDto);

        mockMvc.perform(put("/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(commentId))
                .andExpect(jsonPath("$.data.content").value("Updated comment content"));
    }

    @Test
    void deleteComment() throws Exception {
        Long commentId = 1L;

        doNothing().when(commentService).deleteComment(commentId);

        mockMvc.perform(delete("/comments/{id}", commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Comment deleted successfully"));
    }

    @Test
    void voteComment() throws Exception {
        Long commentId = 1L;
        VoteRequestDto voteRequest = new VoteRequestDto();
        voteRequest.setVoteType(VoteType.up);

        VoteResponseDto voteResponse = new VoteResponseDto();
        voteResponse.setUpvotes(6L);
        voteResponse.setDownvotes(0L);
        voteResponse.setScore(6L);
        voteResponse.setUserVote(VoteType.up);

        when(commentService.voteComment(commentId, voteRequest))
                .thenReturn(voteResponse);

        mockMvc.perform(put("/comments/{id}/vote", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.upvotes").value(6))
                .andExpect(jsonPath("$.data.downvotes").value(0))
                .andExpect(jsonPath("$.data.score").value(6))
                .andExpect(jsonPath("$.data.userVote").value("up"));
    }

    @Test
    void handleCommentNotFound() throws Exception {
        Long commentId = 999L;

        when(commentService.getCommentById(commentId))
                .thenThrow(new IllegalArgumentException("Comment not found with ID: " + commentId));

        mockMvc.perform(get("/comments/{id}", commentId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Comment not found with ID: " + commentId));
    }
}