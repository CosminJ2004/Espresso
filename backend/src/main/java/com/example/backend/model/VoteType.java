package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum VoteType {
    UP, DOWN, NONE;

    @JsonValue
    public String getValue() {
        return this.name().toLowerCase();
    }

    @JsonCreator
    public static VoteType fromString(String value) {
        if (value == null) return null;
        return VoteType.valueOf(value.toUpperCase());
    }
}
