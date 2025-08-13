//package com.example.backend.unitTests.service;
//
//import com.example.backend.dto.*;
//import com.example.backend.model.*;
//import com.example.backend.repository.CommentRepository;
//import com.example.backend.repository.PostRepository;
//import com.example.backend.repository.UserRepository;
//import com.example.backend.repository.VoteRepository;
//import com.example.backend.service.CommentService;
//import com.example.backend.service.PostService;
//import com.example.backend.service.VoteService;
//import com.example.backend.unitTests.utils.TestDataUtility;
//import com.example.backend.util.logger.ConsoleLogger;
//import com.example.backend.util.logger.FileLogger;
//import com.example.backend.util.logger.LoggerManager;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.junit.jupiter.MockitoExtension;
//import com.example.backend.util.logger.LogLevel;
//import org.springframework.mock.web.MockMultipartFile;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class PostServiceTest {
//    @Mock
//    private PostRepository postRepository;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private CommentRepository commentRepository;
//    @Mock
//    private VoteRepository voteRepository;
//    @Mock
//    private VoteService voteService;
//    @Mock
//    private CommentService commentService;
//
//    @InjectMocks
//    private PostService postService;
//
//    //date de lucru
//    private User mockUser;
//    private Post mockPost;
//    private Post anotherMockPost;
//    private PostRequestDto mockPostRequest;
//    private PostRequestDto anotherMockPostRequest;
//    private Comment mockComment;
//    private Comment anotherMockComment;
//
//    @BeforeEach
//    void setUp() {//fara throws ca nu arunc nimic
//        // Setup loggerii pentru test
//        //in PostService, avem camp static final pt loggerManager, iar testele unitare nu incarca LoggerConfig,
//        //deci loggerii console si file nu sunt inregsitrati
//        LoggerManager manager = LoggerManager.getInstance();
//        try {
//            manager.addLogger("console", new ConsoleLogger(LogLevel.INFO));
//        } catch (IllegalArgumentException e) {
//            // logger exista deja
//        }
//        try {
//            manager.addLogger("file", new FileLogger(LogLevel.INFO, "test.log"));
//        } catch (IllegalArgumentException e) {
//            // logger exista deja
//        }
//
//        //user
//        mockUser = TestDataUtility.createHardcodedUser(); //are username-ul hardcodat in current_user, deoarece in service in metoda convertToResponseDto e hardcodat numele
//        //post
//        mockPost = TestDataUtility.createRandomPostWithId(mockUser, 1L);
//        anotherMockPost = TestDataUtility.createRandomPostWithId(mockUser, 2L); // folosit pt o postare in plus sau pt update
//        //post request
//        mockPostRequest = new PostRequestDto(
//                mockPost.getTitle(),
//                mockPost.getContent(),
//                mockPost.getAuthorUsername(),
//                "subreddit_name"
//        );
//        //used for update
//        anotherMockPostRequest = new PostRequestDto(
//                anotherMockPost.getTitle(),
//                anotherMockPost.getContent(),
//                anotherMockPost.getAuthorUsername(),
//                "subreddit_name"
//        );
//        //comment
//        mockComment = TestDataUtility.createRandomComment(mockUser, mockPost);
//        anotherMockComment = TestDataUtility.createRandomComment(mockUser, mockPost); //folosit pt un comment in plus
//    }
//
//    //test get post id
//    @Test
//    void shouldReturnPostResponseWhenPostExists() {
//        when(postRepository.findById(mockPost.getId())).thenReturn(Optional.of(mockPost));
//        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser)); // hard coded cu userul current_user momentan
//
//        PostResponseDto result = postService.getPostById(mockPost.getId());
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo(mockPost.getId());
//        assertThat(result.getTitle()).isEqualTo(mockPost.getTitle());
//        assertThat(result.getAuthor()).isEqualTo(mockUser.getUsername());
//        //hard-coded in cod
//        //assertThat(result.getSubreddit()).isEqualTo("echipa3_general");
//    }
//
//    @Test
//    void shouldThrowExceptionWhenPostNotFound() {
//        Long nonExistentId = 999L;
//        when(postRepository.findById(nonExistentId)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> postService.getPostById(nonExistentId))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Post not found with ID: " + nonExistentId);
//    }
//
//    //test get all posts
//    @Test
//    void shouldReturnAllPosts() {
//        when(postRepository.findAll()).thenReturn(List.of(mockPost, anotherMockPost));
//        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));
//
//        List<PostResponseDto> result = postService.getAllPosts();
//
//        assertThat(result.size()).isEqualTo(2);
//        assertThat(result.get(0).getId()).isEqualTo(1L);
//        assertThat(result.get(1).getId()).isEqualTo(2L);
//    }
//
//    @Test
//    void shouldReturnEmptyListWhenNoPosts() {
//        when(postRepository.findAll()).thenReturn(List.of());
//
//        List<PostResponseDto> result = postService.getAllPosts();
//
//        assertThat(result.size()).isEqualTo(0);
//    }
//
//    //create Post
//    @Test
//    void shouldCreatePostSuccessfully() {
//        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));
//        when(postRepository.save(any(Post.class))).thenReturn(mockPost);
//        when(voteRepository.save(any(Vote.class))).thenReturn(new Vote());
//
//        PostResponseDto result = postService.createPost(mockPostRequest);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getTitle()).isEqualTo(mockPost.getTitle());
//        verify(postRepository).save(any(Post.class));
//        verify(voteRepository).save(any(Vote.class));
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUserNotFoundInCreatePost() {
//        PostRequestDto requestDto = mockPostRequest;
//        requestDto.setAuthor("nonexistent_user");
//        when(userRepository.findByUsername(requestDto.getAuthor())).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> postService.createPost(requestDto))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("User not found: nonexistent_user");
//    }
//
//    //update Post
//    @Test
//    void shouldUpdatePostSuccessfully() {
//        when(postRepository.findById(mockPost.getId())).thenReturn(Optional.of(mockPost));
//        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));
//        when(postRepository.save(any(Post.class))).thenReturn(anotherMockPost);
//
//        PostResponseDto result = postService.updatePost(mockPost.getId(), anotherMockPostRequest);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getTitle()).isEqualTo(anotherMockPost.getTitle());
//        assertThat(result.getContent()).isEqualTo(anotherMockPost.getContent());
//        verify(postRepository).save(any(Post.class));
//    }
//
//    @Test
//    void shouldThrowExceptionWhenPostNotFoundInUpdate() {
//        when(postRepository.findById(999L)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> postService.updatePost(999L, anotherMockPostRequest))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Post not found with ID: 999");
//    }
//
//    //delete
//    @Test
//    void shouldDeletePostSuccessfully() {
//        when(postRepository.existsById(mockPost.getId())).thenReturn(true);
//        doNothing().when(postRepository).deleteById(mockPost.getId());
//
//        postService.deletePost(mockPost.getId());
//
//        verify(postRepository).deleteById(mockPost.getId());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenPostNotFoundInDelete() {
//        when(postRepository.existsById(999L)).thenReturn(false);
//
//        assertThatThrownBy(() -> postService.deletePost(999L))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Post not found with ID: 999");
//    }
//
//    //vote
//    @Test
//    void shouldVotePostSuccessfully() {
//        VoteRequestDto voteRequest = TestDataUtility.createVoteRequest(VoteType.UP);
//        Vote vote = TestDataUtility.createVote(mockUser, mockPost, VoteType.UP);
//
//        when(postRepository.findById(mockPost.getId())).thenReturn(Optional.of(mockPost));
//        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));
//        when(postRepository.findById(mockPost.getId())).thenReturn(Optional.of(mockPost));
//        when(voteRepository.findByUserAndPost(mockUser, mockPost)).thenReturn(Optional.of(vote));
//
//        VoteResponseDto result = postService.votePost(mockPost.getId(), voteRequest);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getUserVote()).isEqualTo(VoteType.UP);
//        verify(voteService).vote(any(), any(), any(), eq(VoteType.UP));
//    }
//
//    @Test
//    void shouldThrowExceptionWhenPostNotFoundInVote() {
//        VoteRequestDto voteRequest = TestDataUtility.createVoteRequest(VoteType.UP);
//
//        when(postRepository.findById(999L)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> postService.votePost(999L, voteRequest))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Post not found");
//    }
//
//    //comments
//    @Test
//    void shouldReturnCommentsForPost() {
//        List<Comment> comments = List.of(mockComment, anotherMockComment);
//        //raspunsurile dto returnate
//        CommentResponseDto response1 = new CommentResponseDto(
//                mockComment.getId(),
//                mockPost.getId(),
//                null,//parentId
//                mockComment.getText(),//text
//                mockComment.getAuthor().getUsername(), //author
//                0L,//upvote
//                0L,//downvotes
//                0L,//scor
//                null,//vot
//                mockComment.getCreatedAt(),
//                mockComment.getUpdatedAt(),
//                new ArrayList<>()//replies
//        );
//        CommentResponseDto response2 = new CommentResponseDto(
//                anotherMockComment.getId(),
//                mockPost.getId(),
//                null,
//                anotherMockComment.getText(),
//                anotherMockComment.getAuthor().getUsername(),
//                0L,
//                0L,
//                0L,
//                null,
//                anotherMockComment.getCreatedAt(),
//                anotherMockComment.getUpdatedAt(),
//                new ArrayList<>()
//        );
//
//        when(commentService.commentToCommentResponseDto(mockComment))
//                .thenReturn(response1);
//        when(commentService.commentToCommentResponseDto(anotherMockComment))
//                .thenReturn(response2);
//
//        when(postRepository.existsById(mockPost.getId())).thenReturn(true);
//        when(commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtAsc(mockPost.getId()))
//                .thenReturn(comments);
//
//        var result = postService.getCommentsByPostId(mockPost.getId());
//
//        assertThat(result).isNotNull();
//        assertThat(result.size()).isEqualTo(2);
//        verify(commentService, times(2)).commentToCommentResponseDto(any(Comment.class));
//    }
//
//    @Test
//    void shouldThrowExceptionWhenPostNotFoundInGetComments() {
//        when(postRepository.existsById(999L)).thenReturn(false);
//
//        assertThatThrownBy(() -> postService.getCommentsByPostId(999L))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Post not found with ID: 999");
//    }
//
//    @Test
//    void shouldAddCommentSuccessfully() {
//        CommentRequestDto requestDto = new CommentRequestDto(
//                mockComment.getText(),
//                mockUser.getUsername(),
//                null//doar la postare
//        );
//
//        CommentResponseDto expectedResponse = new CommentResponseDto(
//                mockComment.getId(),
//                mockPost.getId(),
//                null,
//                mockComment.getText(),
//                mockComment.getAuthor().getUsername(),
//                0L,
//                0L,
//                0L,
//                null,
//                mockComment.getCreatedAt(),
//                mockComment.getUpdatedAt(),
//                new ArrayList<>()
//        );
//
//        when(postRepository.findById(mockPost.getId())).thenReturn(Optional.of(mockPost));
//        when(userRepository.findByUsername(requestDto.getAuthor())).thenReturn(Optional.of(mockUser));
//        when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);
//        when(voteRepository.save(any(Vote.class))).thenReturn(new Vote());
//        when(commentService.commentToCommentResponseDto(mockComment)).thenReturn(expectedResponse);
//
//        CommentResponseDto result = postService.addComment(mockPost.getId(), requestDto);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo(mockComment.getId());
//        assertThat(result.getContent()).isEqualTo(requestDto.getContent());
//        verify(commentRepository).save(any(Comment.class));
//        verify(voteRepository).save(any(Vote.class));
//        verify(commentService).commentToCommentResponseDto(mockComment);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenParentCommentNotFound() {
//        Long parentId = 999L;
//        CommentRequestDto requestDto = new CommentRequestDto("Reply content", mockUser.getUsername(), parentId);
//
//        when(postRepository.findById(mockPost.getId())).thenReturn(Optional.of(mockPost));
//        when(userRepository.findByUsername(requestDto.getAuthor())).thenReturn(Optional.of(mockUser));
//        when(commentRepository.findById(parentId)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> postService.addComment(mockPost.getId(), requestDto))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining("Parent comment not found");
//    }
//
//    //metoda pt coverageu la getCommentsByPostId la verificarea cu getParent
//    @Test
//    void shouldNestChildCommentUnderParent() {
//        //parentComment va fi anotherMockComment, iar comentariu Copil e mockComment
//        anotherMockComment.setParent(null);
//        mockComment.setParent(anotherMockComment);
//
//        anotherMockComment.getReplies().clear();
//        anotherMockComment.getReplies().add(mockComment);
//
//        CommentResponseDto parentDto = getCommentResponseDto();
//
//        when(postRepository.existsById(mockPost.getId())).thenReturn(true);
//        when(commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtAsc(mockPost.getId()))
//                .thenReturn(List.of(anotherMockComment));
//        when(commentService.commentToCommentResponseDto(anotherMockComment)).thenReturn(parentDto);
//
//        List<CommentResponseDto> result = postService.getCommentsByPostId(mockPost.getId());
//
//        assertThat(result).isNotNull();
//        assertThat(result.size()).isEqualTo(1);
//        assertThat(result.getFirst().getId()).isEqualTo(anotherMockComment.getId());
//        assertThat(result.getFirst().getReplies().size()).isEqualTo(1);
//        assertThat(result.getFirst().getReplies().getFirst().getId()).isEqualTo(mockComment.getId());
//        verify(commentService, times(1)).commentToCommentResponseDto(any(Comment.class));
//    }
//
//    private CommentResponseDto getCommentResponseDto() {
//        CommentResponseDto childDto = new CommentResponseDto(
//                mockComment.getId(),
//                mockPost.getId(),
//                anotherMockComment.getId(),
//                mockComment.getText(),
//                mockComment.getAuthor().getUsername(),
//                0L,
//                0L,
//                0L,
//                null,
//                mockComment.getCreatedAt(),
//                mockComment.getUpdatedAt(),
//                new ArrayList<>()
//        );
//
//        return new CommentResponseDto(
//                anotherMockComment.getId(),
//                mockPost.getId(),
//                null,
//                anotherMockComment.getText(),
//                anotherMockComment.getAuthor().getUsername(),
//                0L,
//                0L,
//                0L,
//                null,
//                anotherMockComment.getCreatedAt(),
//                anotherMockComment.getUpdatedAt(),
//                List.of(childDto)
//        );
//    }
//
//    //image
////    @Test
////    void shouldThrowExceptionWhenSavingEmptyImage() {
////        MockMultipartFile emptyFile = new MockMultipartFile(
////                "file",
////                "empty.png",
////                "image/png",
////                new byte[0]
////        );
////
////        assertThatThrownBy(() -> postService.saveImage(emptyFile))
////                .isInstanceOf(IOException.class)
////                .hasMessage("Empty file.");
////    }
//
////    @Test
////    void shouldThrowExceptionWhenUserNotFoundInAddPostWithImage() {
////        String imagePath = "uploads/test.png";
////        when(userRepository.findByUsername(mockPostRequest.getAuthor())).thenReturn(Optional.empty());
////
////        assertThatThrownBy(() -> postService.addPostWithImage(mockPostRequest, imagePath))
////                .isInstanceOf(IllegalArgumentException.class)
////                .hasMessageContaining("User not found: " + mockPostRequest.getAuthor());
////    }
//
////    @Test
////    void shouldAddPostWithImageSuccessfully() {
////        String imagePath = "uploads/test.png";
////        when(userRepository.findByUsername(mockUser.getUsername())).thenReturn(Optional.of(mockUser));
////        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));
////
////        Post result = postService.addPostWithImage(mockPostRequest, imagePath);
////
////        assertThat(result).isNotNull();
////        assertThat(result.getFilePath()).isEqualTo(imagePath);
////        verify(userRepository).findByUsername(mockPostRequest.getAuthor());
////        verify(postRepository).save(any(Post.class));
////    }
//
////    @Test
////    void shouldSaveImageSuccessfully() throws IOException {
////        MockMultipartFile mockFile = new MockMultipartFile(
////                "file",
////                "test.png",
////                "image/png",
////                "dummy content".getBytes()
////        );
////
////        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
////            Path mockDir = Path.of("uploads");
////
////            filesMock.when(() -> Files.createDirectories(any(Path.class))).thenReturn(mockDir);
////            filesMock.when(() -> Files.copy(any(InputStream.class), any(Path.class), eq(StandardCopyOption.REPLACE_EXISTING)))
////                    .thenReturn(1L);
////
////            String result = postService.saveImage(mockFile);
////
////            assertThat(result).startsWith("/uploads/");
////            assertThat(result).endsWith("test.png");
////            filesMock.verify(() -> Files.createDirectories(any(Path.class)));
////            filesMock.verify(() -> Files.copy(any(InputStream.class), any(Path.class), eq(StandardCopyOption.REPLACE_EXISTING)));
////        }
////    }
//}
