package service;

import objects.domain.Comment;
import objects.domain.Vote;
import objects.dto.CommentRequestDto;
import objects.dto.VoteRequestDto;
import infra.http.ApiResult;

public interface CommentService {
    ApiResult<Comment> getById(String id);
    ApiResult<Comment> update(String id, CommentRequestDto dto);
    ApiResult<Void> delete(String id);
    ApiResult<Vote> voteComment(String id, VoteRequestDto dto);
}
