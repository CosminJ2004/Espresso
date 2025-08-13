package service;

import objects.domain.Post;
import objects.domain.Comment;
import objects.domain.Vote;
import objects.dto.PostRequestDto;
import objects.dto.PostRequestWithImageDto;
import objects.dto.VoteRequestDto;
import objects.dto.CommentRequestDto;
import infra.http.ApiResult;

import java.util.List;
import java.util.UUID;

public interface PostService {
    ApiResult<List<Post>> getAll();
    ApiResult<Post> getById(UUID id);
    ApiResult<Post> create(PostRequestDto dto);
    ApiResult<Post> createWithImage(PostRequestWithImageDto dto);
    ApiResult<Post> update(UUID id, PostRequestDto dto);
    ApiResult<Void> delete(UUID id);
    ApiResult<Vote> votePost(UUID id, VoteRequestDto dto);
    ApiResult<List<Comment>> getCommentsByPostId(UUID id);
    ApiResult<Comment> addComment(UUID postId, CommentRequestDto dto);
}
