package undo.revert;

import undo.action.Action;
import undo.action.UndoableAction;

public interface ActionReverter {
    boolean supports(Action action);
    void undo(UndoableAction op) throws Exception;
    void redo(UndoableAction op) throws Exception;
}