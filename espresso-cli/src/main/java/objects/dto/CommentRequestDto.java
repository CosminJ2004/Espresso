package objects.dto;

public record CommentRequestDto(String content, String author, Long parentId) {
}
