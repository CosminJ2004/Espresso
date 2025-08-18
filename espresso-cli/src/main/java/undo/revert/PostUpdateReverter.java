package undo.revert;

import com.fasterxml.jackson.databind.ObjectMapper;
import infra.http.ApiClient;
import objects.dto.PostRequestDto;
import service.PostService;
import undo.action.Action;
import undo.action.UndoableAction;

public final class PostUpdateReverter implements ActionReverter {
    private final PostService postService;
    private final ObjectMapper mapper;

    public PostUpdateReverter(PostService postService, ObjectMapper mapper) {
        this.postService = postService;
        this.mapper = mapper;
    }

    @Override
    public boolean supports(Action a) {
        return a == Action.UPDATE_POST;
    }

    @Override public void undo(UndoableAction op) throws Exception {
        PostRequestDto oldDto = mapper.readValue(op.getOldData(), PostRequestDto.class);
        var response = postService.update(op.getEntityId(), oldDto);
        if (response == null || !response.isSuccess()) {
            throw new IllegalStateException("Could not revert post update!"); // TO DO: better error handling
        }
    }

    @Override public void redo(UndoableAction op) throws Exception {
        PostRequestDto newDto = mapper.readValue(op.getNewData(), PostRequestDto.class);
        var response = postService.update(op.getEntityId(), newDto);
        if (response == null || !response.isSuccess()) {
            throw new IllegalStateException("Could not redo post update!");
        }
    }
}
