package service;

import model.Post;
import model.User;
import model.Vote;
import model.VoteType;
import repository.PostRepository;
import repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class VoteServiceTest {

    private VoteService voteService;
    private VoteRepository mockRepo;

    @BeforeEach
    public void setUp() {
        mockRepo = mock(VoteRepository.class);
        VoteService.resetInstance(); // important ca testele să fie izolate
        voteService = VoteService.getInstance();
    }

    @Test
    public void testAddVote_CallsRepo() {

        User mockUser = new User(100, "UserTest", "test"); // sau mock(User.class)

        Post post = new Post(mockUser, "Titlu", "Continut");
        Vote vote = new Vote(mockUser, VoteType.UPVOTE); // userId, postId, upvote

        voteService.vote(post,mockUser,VoteType.DOWNVOTE);
        //inca nu
    }
}
