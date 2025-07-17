package service;

import model.Post;
import model.User;
import repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PostServiceTest {

    private PostService postService;
    private PostRepository mockRepo;

    @BeforeEach
    public void setUp() {
        mockRepo = mock(PostRepository.class);
        PostService.resetInstance(); // important ca testele sa fie izolate
        postService = PostService.getInstance();
    }

    @Test
    public void testAddPost_ShouldSavePost() {
        User mockUser = new User(100, "UserTest", "test"); // sau mock(User.class)
        Post post = new Post(mockUser, "Titlu", "Continut");

        postService.addPost(post);

        verify(mockRepo, times(1)).save(post);
    }

    @Test
    public void testAddNullPost_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            postService.addPost(null);
        });
    }
}
