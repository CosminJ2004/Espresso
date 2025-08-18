package undo.action;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public class UndoableAction {
    private final UUID id;
    private final Action action;
    private final String entityId;
    private final String entityType;
    private final String oldData;
    private final String newData;
    private final LocalDateTime timestamp;
    private final String username;

    @JsonCreator
    public UndoableAction(
            @JsonProperty("id") UUID id,
            @JsonProperty("action") Action action,
            @JsonProperty("entityId") String entityId,
            @JsonProperty("entityType") String entityType,
            @JsonProperty("oldData") String oldData,
            @JsonProperty("newData") String newData,
            @JsonProperty("timestamp") LocalDateTime timestamp,
            @JsonProperty("username") String username
    ) {
        this.id = id;
        this.action = action;
        this.entityId = entityId;
        this.entityType = entityType;
        this.oldData = oldData;
        this.newData = newData;
        this.timestamp = timestamp;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public Action getAction() {
        return action;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getOldData() {
        return oldData;
    }

    public String getNewData() {
        return newData;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }
}
