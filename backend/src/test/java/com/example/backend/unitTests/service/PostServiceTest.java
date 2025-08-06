package com.example.backend.unitTests.service;

import com.example.backend.dto.PostRequestDto;
import com.example.backend.dto.PostResponseDto;
import com.example.backend.dto.VoteRequestDto;
import com.example.backend.dto.VoteResponseDto;
import com.example.backend.model.Post;
import com.example.backend.model.User;
import com.example.backend.model.Vote;
import com.example.backend.model.VoteType;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VoteRepository;
import com.example.backend.service.PostService;
import com.example.backend.service.VoteService;
import com.example.backend.unitTests.utils.TestDataUtility;
import com.example.backend.util.logger.ConsoleLogger;
import com.example.backend.util.logger.FileLogger;
import com.example.backend.util.logger.LoggerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.backend.util.logger.LogLevel;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

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

    //date de lucru
    private User mockUser;
    private Post mockPost;
    private Post anotherMockPost;
    private PostRequestDto mockPostRequest;
    private PostRequestDto anotherMockPostRequest;

    @BeforeEach
    void setUp() throws Exception {
        // Setup loggerii pentru test
        //in PostService, avem camp static final pt loggerManager, iar testele unitare nu incarca LoggerConfig,
        //deci loggerii console si file nu sunt inregsitrati
        LoggerManager manager = LoggerManager.getInstance();
        try {
            manager.addLogger("console", new ConsoleLogger(LogLevel.INFO));
        } catch (IllegalArgumentException e) {
            // logger exista deja
        }
        try {
            manager.addLogger("file", new FileLogger(LogLevel.INFO, "test.log"));
        } catch (IllegalArgumentException e) {
            // logger exista deja
        }
        
        //user
        mockUser = TestDataUtility.createHardcodedUser(); //are username-ul hardcodat in current_user, deoarece in service in metoda convertToResponseDto e hardcodat numele
        //post
        mockPost = TestDataUtility.createRandomPostWithId(mockUser, 1L);
        anotherMockPost = TestDataUtility.createRandomPostWithId(mockUser, 2L); // folosit pt o postare in plus sau pt update
        //post request
        mockPostRequest = new PostRequestDto(
                mockPost.getTitle(),
                mockPost.getContent(),
                mockPost.getAuthorUsername(),
                "subreddit_name"
        );
        //used for update
        anotherMockPostRequest = new PostRequestDto(
                anotherMockPost.getTitle(),
                anotherMockPost.getContent(),
                anotherMockPost.getAuthorUsername(),
                "subreddit_name"
        );
    }
    
    //test get post id
    @Test
    void shouldReturnPostResponseWhenPostExists() {
        when(postRepository.findById(mockPost.getId())).thenReturn(Optional.of(mockPost));
        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser)); // hard coded cu userul current_user momentan

        PostResponseDto result = postService.getPostById(mockPost.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(mockPost.getId());
        assertThat(result.getTitle()).isEqualTo(mockPost.getTitle());
        assertThat(result.getAuthor()).isEqualTo(mockUser.getUsername());
        //hard-coded in cod
        //assertThat(result.getSubreddit()).isEqualTo("echipa3_general");
    }

    @Test
    void shouldThrowExceptionWhenPostNotFound() {
        Long nonExistentId = 999L;
        when(postRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.getPostById(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Post not found with ID: " + nonExistentId);
    }
    //test get all posts
    @Test
    void shouldReturnAllPosts() {
        when(postRepository.findAll()).thenReturn(List.of(mockPost, anotherMockPost));
        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));

        List<PostResponseDto> result = postService.getAllPosts();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void shouldReturnEmptyListWhenNoPosts() {
        when(postRepository.findAll()).thenReturn(List.of());

        List<PostResponseDto> result = postService.getAllPosts();

        assertThat(result.size()).isEqualTo(0);
    }
    //create Post
    @Test
    void shouldCreatePostSuccessfully(){
        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));
        when(postRepository.save(any(Post.class))).thenReturn(mockPost);
        when(voteRepository.save(any(Vote.class))).thenReturn(new Vote());

        PostResponseDto result = postService.createPost(mockPostRequest);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(mockPost.getTitle());
        verify(postRepository).save(any(Post.class));
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundInCreatePost() {
        PostRequestDto requestDto = mockPostRequest;
        requestDto.setAuthor("nonexistent_user");
        when(userRepository.findByUsername(requestDto.getAuthor())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.createPost(requestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found: nonexistent_user");
    }
    //update Post
    @Test
    void shouldUpdatePostSuccessfully() {
        when(postRepository.findById(mockPost.getId())).thenReturn(Optional.of(mockPost));
        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));
        when(postRepository.save(any(Post.class))).thenReturn(anotherMockPost);

        PostResponseDto result = postService.updatePost(mockPost.getId(), anotherMockPostRequest);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(anotherMockPost.getTitle());
        assertThat(result.getContent()).isEqualTo(anotherMockPost.getContent());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void shouldThrowExceptionWhenPostNotFoundInUpdate() {
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.updatePost(999L, anotherMockPostRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Post not found with ID: 999");
    }
    //delete
    @Test
    void shouldDeletePostSuccessfully() {
        when(postRepository.existsById(mockPost.getId())).thenReturn(true);
        doNothing().when(postRepository).deleteById(mockPost.getId());

        postService.deletePost(mockPost.getId());

        verify(postRepository).deleteById(mockPost.getId());
    }

    @Test
    void shouldThrowExceptionWhenPostNotFoundInDelete() {
        when(postRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> postService.deletePost(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Post not found with ID: 999");
    }
    //vote
    @Test
    void shouldVotePostSuccessfully() {
        VoteRequestDto voteRequest = TestDataUtility.createVoteRequest(VoteType.up);
        Vote vote = TestDataUtility.createVote(mockUser, mockPost, VoteType.up);

        when(postRepository.findById(mockPost.getId())).thenReturn(Optional.of(mockPost));
        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));
        when(postRepository.findById(mockPost.getId())).thenReturn(Optional.of(mockPost));
        when(voteRepository.findByUserAndPost(mockUser, mockPost)).thenReturn(Optional.of(vote));

        VoteResponseDto result = postService.votePost(mockPost.getId(), voteRequest);

        assertThat(result).isNotNull();
        assertThat(result.getUserVote()).isEqualTo(VoteType.up);
        verify(voteService).vote(any(), any(), any(), eq(VoteType.up));
    }

    @Test
    void shouldThrowExceptionWhenPostNotFoundInVote() {
        var voteRequest = TestDataUtility.createVoteRequest(com.example.backend.model.VoteType.up);

        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.votePost(999L, voteRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Post not found");
    }
}
