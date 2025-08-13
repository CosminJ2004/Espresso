package objects.dto;

import java.nio.file.Path;

public record PostRequestWithImageDto(
        String title,
        String content,
        String author,
        String subreddit,
        Path imagePath,
        Long filter
) {}