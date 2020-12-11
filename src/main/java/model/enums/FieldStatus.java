package model.enums;

public enum FieldStatus {
    FIELD_EMPTY,
    FIELD_EMPTY_BLOCKED,
    FIELD_SHIP_ACTIVE,
    FIELD_SHIP_HIT;

    public boolean isShootable(){
        return switch (this) {
            case FIELD_SHIP_ACTIVE, FIELD_EMPTY -> true;
            default -> false;
        };
    }
}
