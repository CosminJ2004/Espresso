package objects.dto;

public record CommentRequestDto(String content, String author, String parentId) {
}
