package objects.domain;

public enum VoteType {
    UP, DOWN, NONE;
    
    public String getValue() {
        return this.name().toLowerCase();
    }
    
    public static VoteType fromString(String value) {
        if (value == null) return null;
        return VoteType.valueOf(value.toUpperCase());
    }
}
