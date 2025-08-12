package service;

import objects.domain.Comment;
import objects.domain.Vote;
import objects.dto.CommentRequestDto;
import objects.dto.VoteRequestDto;
import infra.http.ApiResult;

public interface CommentService {
    ApiResult<Comment> getById(Long id);
    ApiResult<Comment> update(Long id, CommentRequestDto dto);
    ApiResult<Void> delete(Long id);
    ApiResult<Vote> voteComment(Long id, VoteRequestDto dto);
}
