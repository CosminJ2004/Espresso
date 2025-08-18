package undo;

import com.fasterxml.jackson.databind.ObjectMapper;
import undo.action.Action;
import undo.action.UndoableAction;
import undo.revert.ActionReverter;

import java.util.*;


public class UndoRedoManager {
    private static UndoRedoManager instance;
    private final Deque<UndoableAction> undoStack = new ArrayDeque<UndoableAction>();
    private final Deque<UndoableAction> redoStack = new ArrayDeque<UndoableAction>();
    private final Map<Action, ActionReverter> handlers = new EnumMap<Action, ActionReverter>(Action.class);
    private final int maxOperations;
    //max operations e preluat din utilitarul UndoRedoUtils
    private UndoRedoManager(Collection<ActionReverter> reverters, int maxOperations) {
        if (reverters != null) {
            for (ActionReverter r : reverters) {
                for (Action a : Action.values()) {
                    if (r.supports(a)) {
                        handlers.put(a, r);
                    }
                }
            }
        }
        this.maxOperations = maxOperations;
    }
    public static UndoRedoManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UndoRedoManager cannot be used till initialized.");
        }
        return instance;
    }

    public static void initialize(Collection<ActionReverter> reverters, int maxOperations) {
        if (instance == null) {
            instance = new UndoRedoManager(reverters, maxOperations);
        }
    }

    //adaug actiune pe undo(cand updateul a reusit)
    public void record(UndoableAction undoableAction) {
        if (undoableAction != null) {
            undoStack.push(undoableAction);
            if (isLimitExceeded(undoStack)) {
                undoStack.removeLast();
            }
        }
        redoStack.clear(); //daca s a dat undo, redoStack-ul se goleste
    }

    public UndoableAction undo() throws Exception {
        if (undoStack.isEmpty()) {
            return null;
        }

        UndoableAction undoableAction = undoStack.pop();
        ActionReverter handler = handlers.get(undoableAction.getAction());
        if (handler != null) {
            handler.undo(undoableAction);
            redoStack.push(undoableAction);
            return undoableAction;
        }
        return null;
    }

    public UndoableAction redo() throws Exception {
        if (redoStack.isEmpty()) {
            return null;
        }

        UndoableAction op = redoStack.pop();
        ActionReverter handler = handlers.get(op.getAction());
        if (handler != null) {
            handler.redo(op);
            undoStack.push(op);
            return op;
        }

        return null;
    }

    public List<UndoableAction> historyRedo(int n) {
        List<UndoableAction> list = new ArrayList<UndoableAction>();
        if (n <= 0) return list;
        int count = 0;
        for (UndoableAction a : redoStack) {
            if (count++ >= n) break;
            list.add(a);
        }
        return list;
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void clearAll() {
        undoStack.clear();
        redoStack.clear();
    }

    private boolean isLimitExceeded(Deque<UndoableAction> q) {
        return q.size() > maxOperations;
    }


    public UndoableAction getLastOperation() {
        return undoStack.peekFirst();
    }
}
