package com.example.backend.unitTests.utils;

import com.example.backend.dto.VoteRequestDto;
import com.example.backend.model.*;
import net.datafaker.Faker;

public class TestDataUtility {
    private static final Faker faker = new Faker();

    //varianta hardcodata pentru "current_user" din convertToResponseDto unde se da findByUsername
    public static User createHardcodedUser() {
        String password = faker.internet().password(8, 16);
        Long id = Math.abs(faker.number().randomNumber());
        String username = "current_user";

        User user = new User(username, password);
        user.setId(id);

        return user;
    }

    //de folosit in cazul in care nu mai e hard-coded in PostService
    public static User createRandomUser() {
        String username = faker.regexify("[a-zA-Z0-9]{3,30}");
        String password = faker.internet().password(8, 16);
        Long id = Math.abs(faker.number().randomNumber());

        User user = new User(username, password);
        user.setId(id);

        return user;
    }

    public static Post createRandomPostWithId(User author, Long id) {
        String title = faker.lorem().characters(1, 255);
        String content = faker.lorem().paragraph();

        Post post = new Post(author, title, content);
        post.setId(id);

        return post;
    }

    public static VoteRequestDto createVoteRequest(VoteType type) {
        VoteRequestDto request = new VoteRequestDto();
        request.setVoteType(type);
        return request;
    }

    public static Vote createVote(User user, Post post, VoteType type) {
        Vote vote = new Vote(user, post, type);
        vote.setId(Math.abs(faker.number().randomNumber()));
        return vote;
    }

    public static Comment createRandomComment(User author, Post post) {
        String content = faker.lorem().sentence();
        Long id = Math.abs(faker.number().randomNumber());

        Comment comment = new Comment(author, content, post);
        comment.setId(id);

        return comment;
    }
}
