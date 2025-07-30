package org.example.mappers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.example.dto.PostDTO;
import org.example.models.Post;

public class PostMapper {
    private static PostMapper postMapper;

    public static PostMapper getInstance() {
        if (postMapper == null) {
            postMapper = new PostMapper();
        }
        return postMapper;
    }

    public Post mapDTOToPost(PostDTO postDTO) {
        return new Post(postDTO.getTitle(), postDTO.getContent(), postDTO.getAuthor(), postDTO.getSubreddit(), postDTO.getScore(), postDTO.getCommentCount(), postDTO.getUserVote(), postDTO.getCreatedAt(), postDTO.getUpdatedAt());
    }

    public PostDTO mapPostToDTO(Post post) {
        PostDTO postDTO = new PostDTO(post.getTitle(), post.getContent(), post.getAuthor());
        return postDTO;
    }

    public String DTOToJson(PostDTO postDTO) {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("title", postDTO.getTitle());
        jsonObject.addProperty("content", postDTO.getContent());
        jsonObject.addProperty("author", postDTO.getAuthor());
        String json = gson.toJson(jsonObject);
        return json;
    }


}
