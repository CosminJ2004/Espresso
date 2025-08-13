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

public interface PostService {
    ApiResult<List<Post>> getAll();
    ApiResult<Post> getById(Long id);
    ApiResult<Post> create(PostRequestDto dto);
    ApiResult<Post> createWithImage(PostRequestWithImageDto dto);
    ApiResult<Post> update(Long id, PostRequestDto dto);
    ApiResult<Void> delete(Long id);
    ApiResult<Vote> votePost(Long id, VoteRequestDto dto);
    ApiResult<List<Comment>> getCommentsByPostId(Long id);
    ApiResult<Comment> addComment(Long postId, CommentRequestDto dto);
}
