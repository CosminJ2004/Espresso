package objects.domain;

import java.time.LocalDateTime;

public record Post(
        Long id,
        String title,
        String content,
        String author,
        String subreddit,
        Long upvotes,
        Long downvotes,
        Long score,
        Long commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}