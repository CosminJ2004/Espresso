package objects.domain;

import java.time.LocalDateTime;

public record Comment(
        Long id,
        Long postId,
        Long parentId,
        String content,
        String author,
        Long upvotes,
        Long downvotes,
        Long score,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}