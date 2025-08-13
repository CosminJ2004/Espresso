package objects.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record Comment(
        UUID id,
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