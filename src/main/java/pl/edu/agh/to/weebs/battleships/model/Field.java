package pl.edu.agh.to.weebs.battleships.model;

import pl.edu.agh.to.weebs.battleships.model.enums.FieldStatus;

public class Field {
    private final Coordinates position;
    private FieldStatus fieldStatus;

    public Field(Coordinates position){
        this.position = position;
        this.fieldStatus = FieldStatus.FIELD_EMPTY;
    }

    public FieldStatus getFieldStatus() {
        return fieldStatus;
    }

    public void setFieldStatus(FieldStatus fieldStatus) {
        this.fieldStatus = fieldStatus;
    }

    public Coordinates getPosition() {
        return this.position;
    }


}
