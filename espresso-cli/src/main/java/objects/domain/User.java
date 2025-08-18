package objects.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record User(
        String id,
        String username,
        LocalDateTime createdAt,
        int postCount,
        int commentCount
) {
}