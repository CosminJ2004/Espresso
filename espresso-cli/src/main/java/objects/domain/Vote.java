package objects.domain;

import java.time.LocalDateTime;

public record Vote(
        Long upvotes,
        Long downvotes,
        Long score,
        VoteType userVote,
        LocalDateTime createdAt
) {
}
