package objects.domain;

import java.time.LocalDateTime;

public record User(
        Long id,
        String username,
        LocalDateTime createdAt,
        int postCount,
        int commentCount
) {
}