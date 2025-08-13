package objects.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record Post(
        UUID id,
        String title,
        String content,
        String author,
        String subreddit,
        Long upvotes,
        Long downvotes,
        Long score,
        Long commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        String imageUrl,
        Long filter,
        String userVote
) {
}