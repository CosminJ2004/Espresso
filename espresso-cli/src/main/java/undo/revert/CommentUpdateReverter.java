package undo.revert;

import com.fasterxml.jackson.databind.ObjectMapper;
import objects.dto.CommentRequestDto;
import service.CommentService;
import undo.action.Action;
import undo.action.UndoableAction;

public final class CommentUpdateReverter implements ActionReverter {
    private final CommentService commentService;
    private final ObjectMapper mapper;

    public CommentUpdateReverter(CommentService commentService, ObjectMapper mapper) {
        this.commentService = commentService;
        this.mapper = mapper;
    }

    @Override
    public boolean supports(Action a) {
        return a == Action.UPDATE_COMMENT;
    }

    @Override
    public void undo(UndoableAction op) throws Exception {
        CommentRequestDto oldDto = mapper.readValue(op.getOldData(), CommentRequestDto.class);
        var response = commentService.update(op.getEntityId(), oldDto);
        if (response == null || !response.isSuccess()) {
            throw new IllegalStateException("Could not revert comment update!");
        }
    }

    @Override
    public void redo(UndoableAction op) throws Exception {
        CommentRequestDto newDto = mapper.readValue(op.getNewData(), CommentRequestDto.class);
        var response = commentService.update(op.getEntityId(), newDto);
        if (response == null || !response.isSuccess()) {
            throw new IllegalStateException("Could not redo comment update!");
        }
    }
}