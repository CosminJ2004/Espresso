package objects.domain;

import java.time.LocalDateTime;
import objects.domain.VoteType;

public record Post(
        String id,
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
        VoteType userVote
) {
}