package objects.domain;

import java.time.LocalDateTime;
import java.util.List;
import objects.domain.VoteType;

public record Comment(
        String id,
        String postId,
        String parentId,
        String content,
        String author,
        Long upvotes,
        Long downvotes,
        Long score,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        VoteType userVote,
        List<Comment> replies
) {
}