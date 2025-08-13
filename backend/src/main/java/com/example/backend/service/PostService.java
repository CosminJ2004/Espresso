package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.model.*;
import com.example.backend.repository.CommentRepository;
import com.example.backend.repository.FilterRepository;
import com.example.backend.repository.PostRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VoteRepository;
import com.example.backend.util.logger.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final VoteRepository voteRepository;
    private final VoteService voteService;
    private final StorageService storageService;
    private final ProcessService processService;
    private final FilterRepository filterRepository;

    private static final LoggerManager loggerManager = LoggerManager.getInstance();

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, CommentService commentService, VoteRepository voteRepository, VoteService voteService, StorageService storageService, ProcessService processService, FilterRepository filterRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.voteRepository = voteRepository;
        this.voteService = voteService;
        this.storageService = storageService;
        this.processService = processService;
        this.filterRepository = filterRepository;
    }

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::postToPostResponseDto)
                .collect(Collectors.toList());
    }

    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));
        return postToPostResponseDto(post);
    }
    public PostResponseDto createPostWithoutImage(PostRequestDto postRequest) {
        User author = userRepository.findByUsername(postRequest.getAuthor())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Post post = new Post(author, postRequest.getTitle(), postRequest.getContent());
        postRepository.save(post);

        Vote authorVote = new Vote(author, post, VoteType.UP);
        voteRepository.save(authorVote);
        post.getVotes().add(authorVote);

        return postToPostResponseDto(post);
    }

    public PostResponseDto createPostWithImage(PostRequestDto postRequest) {
        User author = userRepository.findByUsername(postRequest.getAuthor())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        try {
            // if image
            if (postRequest.getImage() != null && !postRequest.getImage().isEmpty()) {
                Filter filter = filterRepository.findById(postRequest.getFilter())
                        .orElseThrow(() -> new RuntimeException("Filter not found"));
                Post post = new Post(author, postRequest.getTitle(), postRequest.getContent());
                Post newPost = postRepository.save(post);
                storageService.uploadImage(filter.getName(), newPost.getImageId(), postRequest.getImage());

                // start processing
//                processService.applyFilter(postRequest.getImage().getBytes(), post.getFilter().getName());
                Vote authorVote = new Vote(author, post, VoteType.UP);
                voteRepository.save(authorVote);

                return postToPostResponseDto(post);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to create post: " + e.getMessage(), e);
        }
        return null;
    }

    public PostResponseDto updatePost (Long id, PostRequestDto postRequest) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + id));

        existingPost.setTitle(postRequest.getTitle());
        existingPost.setContent(postRequest.getContent());

        User author = userRepository.findByUsername(postRequest.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + postRequest.getAuthor()));
        existingPost.setAuthor(author);

        return postToPostResponseDto(postRepository.save(existingPost));
    }

    public void deletePost (Long id){
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found with ID: " + id);
        }
        postRepository.deleteById(id);
    }

    public VoteResponseDto votePost (Long postId, VoteRequestDto voteRequest){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User user = userRepository.findByUsername("current_user")
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        voteService.vote(user, post, null, voteRequest.getVoteType());

        post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        VoteResponseDto voteResponse = new VoteResponseDto();
        voteResponse.setUpvotes(post.getUpvoteCount());
        voteResponse.setDownvotes(post.getDownvoteCount());
        voteResponse.setScore(post.getScore());

        Optional<Vote> currentVote = voteRepository.findByUserAndPost(user, post);
        voteResponse.setUserVote(currentVote.map(Vote::getType).orElse(VoteType.NONE));

        return voteResponse;
    }

    public List<CommentResponseDto> getCommentsByPostId (Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }

        List<Comment> rootComments = commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtAsc(postId);

        return rootComments.stream()
                .map(commentService::commentToCommentResponseDto)
                .collect(Collectors.toList());
    }

    public CommentResponseDto addComment (Long id, CommentRequestDto commentRequest){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        User author = userRepository.findByUsername(commentRequest.getAuthor())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));

        Comment parent = null;
        if (commentRequest.getParentId() != null) {
            parent = commentRepository.findById(commentRequest.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        }

        Comment newComment = new Comment(author, commentRequest.getContent(), post, parent);
        Comment updatedComment = commentRepository.save(newComment);

        Vote authorVote = new Vote(author, updatedComment, VoteType.UP);
        voteRepository.save(authorVote);

        commentRepository.flush();
        Comment refreshedComment = commentRepository.findById(updatedComment.getId()).orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        return commentService.commentToCommentResponseDto(refreshedComment);
    }

    private PostResponseDto postToPostResponseDto (Post post){
        return new PostResponseDto(post.getId(), post.getTitle(), post.getContent(), post.getImageUrl(), post.getFilter() != null ? post.getFilter().getId() : null, post.getAuthor().getUsername(), "echipa3_general", post.getUpvoteCount(), post.getDownvoteCount(), post.getScore(), post.getCommentCount(), post.getUserVote(userRepository.findByUsername("current_user").orElseThrow()), post.getCreatedAt(), post.getUpdatedAt());
    }
}


//    public PostResponseDto getPostWithGrayscale(PostRequestDto dto) {
//            if (dto.getImage() == null || dto.getImage().isEmpty()) {
//                loggerManager.log("file",LogLevel.INFO,"getting post with image");
//            }
//
//            try {
//                // Pregateste requestul
//                HttpClient client = HttpClient.newHttpClient();
//
//                HttpRequest.BodyPublisher imageBodyPublisher;
//
//                try {
//                    imageBodyPublisher = HttpRequest.BodyPublishers.ofInputStream(() -> {
//                        try {
//                            return dto.getImage().getInputStream();
//                        } catch (IOException e) {
//                            throw new UncheckedIOException(e); // convertim excepția verificată într-una neverificată
//                        }
//                    });
//                } catch (UncheckedIOException e) {
//                    loggerManager.log("exception", LogLevel.ERROR, e.getMessage());
//                    imageBodyPublisher = HttpRequest.BodyPublishers.noBody(); // fallback în caz de eroare
//                }
//
//                String boundary = "----WebKitFormBoundary" + UUID.randomUUID();
//                String CRLF = "\r\n";
//
//                // Construcție multipart manuală
//                var multipart = new StringBuilder();
//                multipart.append("--").append(boundary).append(CRLF);
//                multipart.append("Content-Disposition: form-data; name=\"image\"; filename=\"")
//                        .append(dto.getImage().getOriginalFilename()).append("\"").append(CRLF);
//                multipart.append("Content-Type: ").append(dto.getImage().getContentType()).append(CRLF);
//                multipart.append(CRLF);
//
//                byte[] imageBytes = dto.getImage().getBytes();
//                byte[] multipartHeader = multipart.toString().getBytes(StandardCharsets.UTF_8);
//                byte[] multipartFooter = (CRLF + "--" + boundary + "--" + CRLF).getBytes(StandardCharsets.UTF_8);
//
//                byte[] fullBody = new byte[multipartHeader.length + imageBytes.length + multipartFooter.length];
//                System.arraycopy(multipartHeader, 0, fullBody, 0, multipartHeader.length);
//                System.arraycopy(imageBytes, 0, fullBody, multipartHeader.length, imageBytes.length);
//                System.arraycopy(multipartFooter, 0, fullBody, multipartHeader.length + imageBytes.length, multipartFooter.length);
//
//                // Construiește requestul
//                HttpRequest request = HttpRequest.newBuilder()
//                        .uri(URI.create("http://16.171.148.84/filter?filter=grayscale")) // sau IP EC2
//                        .header("Content-Type", "multipart/form-data; boundary=" + boundary)
//                        .POST(HttpRequest.BodyPublishers.ofByteArray(fullBody))
//                        .build();
//
//                HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
//
//                if (response.statusCode() != 200) {
//                    throw new RuntimeException("Image processing failed");
//                }
//
//                // Salvează imaginea procesată (ex: local sau pe MinIO)
//                String filename = UUID.randomUUID() + "_grayscale.png";
//                Path outputPath = Paths.get("uploads/" + filename);
//                Files.createDirectories(outputPath.getParent());
//                Files.write(outputPath, response.body());
//
//                // Construiește Post-ul
//                Post post = new Post();
//                post.setTitle(dto.getTitle());
//                post.setContent(dto.getContent());
//                post.setFilePath("/uploads/" + filename);
//
//                User author = userRepository.findByUsername(dto.getAuthor())
//                        .orElseThrow(() -> new RuntimeException("Author not found"));
//                post.setAuthor(author);
//
//                postRepository.save(post);
//
//                return postToPostResponseDto(post);
//
//            } catch (Exception e) {
//                throw new RuntimeException("Error processing grayscale post", e);
//            }
//        }

//    public byte[] getPostImageWithFilter(Long postId, String filterType) throws IOException {
//        // 1. Găsește postarea în DB
//        Post post = postRepository.findById(postId)
//                .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + postId));
//
//        // 2. Ia filepath-ul din S3
//        String filePath = post.getFilePath();
//
//        // 3. Descarcă imaginea din S3
//        byte[] originalImage = storageService.downloadImage(filePath);
//        // (s3Service.downloadFile returnează byte[])
//
//        // 4. Aplică filtrul (apelezi metoda ta existentă de procesare)
//        byte[] filteredImage = processService.applyFilter(originalImage, filterType);
//
//        // 5. Returnează imaginea filtrată ca byte[]
//        return filteredImage;
//    }
