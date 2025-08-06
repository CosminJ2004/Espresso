package com.example.backend.unitTests.service;

import com.example.backend.dto.PostResponseDto;
import com.example.backend.model.Post;
import com.example.backend.model.User;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VoteRepository;
import com.example.backend.service.PostService;
import com.example.backend.service.VoteService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private VoteService voteService;

    @InjectMocks
    private PostService postService;

    //data faker
    private Faker faker;
    private User mockUser;
    private Post mockPost;

    //TO DO: o metoda statica pt initialzari sa nu fie duuplicat codul unde e nevoie de user/post etc
    @BeforeEach
    void setUp() {
        //faker = new Faker();
        //user
        mockUser = new User();
        mockUser.setId(100L);
        mockUser.setUsername("current_user");
        mockUser.setPassword("password123");
        mockUser.setCreatedAt(LocalDateTime.now());
        mockUser.setUpdatedAt(LocalDateTime.now());
        //post
        mockPost = new Post(mockUser, "title", "content");
        mockPost.setId(1L);
    }

    @Test
    void shouldReturnPostResponseWhenPostExists() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));
        //hard-coded in cod momentan
        when(userRepository.findByUsername("current_user")).thenReturn(Optional.of(mockUser));
        PostResponseDto result = postService.getPostById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("title");
        assertThat(result.getAuthor()).isEqualTo(mockUser.getUsername());
        //hard-coded in cod
        //assertThat(result.getSubreddit()).isEqualTo("echipa3_general");
    }
}
