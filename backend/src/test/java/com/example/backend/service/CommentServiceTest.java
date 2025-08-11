package com.example.backend.service;

import com.example.backend.dto.CommentRequestDto;
import com.example.backend.dto.CommentResponseDto;
import com.example.backend.dto.VoteRequestDto;
import com.example.backend.dto.VoteResponseDto;
import com.example.backend.model.Comment;
import com.example.backend.model.Post;
import com.example.backend.model.User;
import com.example.backend.model.Vote;
import com.example.backend.model.VoteType;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private VoteService voteService;

    @InjectMocks
    private CommentService commentService;

    private Comment testComment;
    private User testUser;
    private Post testPost;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");

        testComment = new Comment();
        testComment.setId(1L);
        testComment.setText("Test comment content");
        testComment.setAuthor(testUser);
        testComment.setPost(testPost);
        testComment.setCreatedAt(LocalDateTime.now());
        testComment.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getCommentById() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(userRepository.findByUsername("current_user")).thenReturn(Optional.of(testUser));

        CommentResponseDto result = commentService.getCommentById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getContent()).isEqualTo("Test comment content");
        assertThat(result.getAuthor()).isEqualTo("testuser");
        assertThat(result.getPostId()).isEqualTo(1L);
        
        verify(commentRepository).findById(1L);
    }

    @Test
    void throwExceptionWhenCommentNotFound() {
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> commentService.getCommentById(999L)
        );
        assertThat(exception.getMessage()).contains("Comment not found with ID: 999");
    }

    @Test
    void updateComment() {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("Updated comment content");
        requestDto.setAuthor("testuser");

        Comment updatedComment = new Comment();
        updatedComment.setId(1L);
        updatedComment.setText("Updated comment content");
        updatedComment.setAuthor(testUser);
        updatedComment.setPost(testPost);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(updatedComment);
        when(userRepository.findByUsername("current_user")).thenReturn(Optional.of(testUser));

        CommentResponseDto result = commentService.updateComment(1L, requestDto);

        assertThat(result.getContent()).isEqualTo("Updated comment content");
        verify(commentRepository).save(testComment);
    }

    @Test
    void deleteComment() {
        when(commentRepository.existsById(1L)).thenReturn(true);

        commentService.deleteComment(1L);

        verify(commentRepository).deleteById(1L);
    }

    @Test
    void throwExceptionWhenDeletingNonExistentComment() {
        when(commentRepository.existsById(999L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> commentService.deleteComment(999L)
        );
        assertThat(exception.getMessage()).contains("Comment not found");
    }

    @Test
    void voteComment() {
        VoteRequestDto voteRequest = new VoteRequestDto();
        voteRequest.setVoteType(VoteType.UP);

        Comment mockRefreshedComment = mock(Comment.class);
        when(mockRefreshedComment.getUpvoteCount()).thenReturn(5L);
        when(mockRefreshedComment.getDownvoteCount()).thenReturn(2L);
        when(mockRefreshedComment.getScore()).thenReturn(3L);

        Vote userVote = new Vote();
        userVote.setType(VoteType.UP);

        when(commentRepository.findById(1L))
                .thenReturn(Optional.of(testComment))
                .thenReturn(Optional.of(mockRefreshedComment));
        when(userRepository.findByUsername("current_user")).thenReturn(Optional.of(testUser));
        when(voteRepository.findByUserAndComment(testUser, mockRefreshedComment)).thenReturn(Optional.of(userVote));

        VoteResponseDto result = commentService.voteComment(1L, voteRequest);

        verify(voteService).vote(testUser, null, testComment, VoteType.UP);
        assertThat(result.getUserVote()).isEqualTo(VoteType.UP);
    }

    @Test
    void throwExceptionWhenVotingNonExistentComment() {
        VoteRequestDto voteRequest = new VoteRequestDto();
        voteRequest.setVoteType(VoteType.UP);

        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> commentService.voteComment(999L, voteRequest)
        );
        assertThat(exception.getMessage()).contains("Comment not found");
    }

    @Test
    void throwExceptionWhenUserNotFoundForVoting() {
        VoteRequestDto voteRequest = new VoteRequestDto();
        voteRequest.setVoteType(VoteType.UP);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(userRepository.findByUsername("current_user")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> commentService.voteComment(1L, voteRequest)
        );
        assertThat(exception.getMessage()).contains("User not found");
    }
}