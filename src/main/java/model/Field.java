package model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import model.enums.Direction;
import model.enums.FieldStatus;

public class Field {
    private final Coordinates position;


    private final ObjectProperty<FieldStatus> fieldStatus;

//    private ObjectProperty<Paint> color;

    public Field(Coordinates position){
        this.position = position;
//        this.color = new SimpleObjectProperty<>();
        this.fieldStatus = new SimpleObjectProperty<>();
        this.setFieldStatus(FieldStatus.FIELD_EMPTY);
    }

    public FieldStatus getFieldStatus() {
        return fieldStatus.get();
    }
    public ObjectProperty<FieldStatus> fieldStatusProperty() {
        return fieldStatus;
    }

    public void setFieldStatus(FieldStatus fieldStatus) {
        this.fieldStatus.set(fieldStatus);

    }

    public boolean wasShot(){
        return this.fieldStatus.get() == FieldStatus.FIELD_EMPTY_BLOCKED || this.fieldStatus.get() == FieldStatus.FIELD_SHIP_HIT;
    }

    public Direction getDirectionFrom(Field other){
        Coordinates myPosition = this.getPosition();
        Coordinates otherPosition = other.getPosition();

        if( myPosition.getX() == otherPosition.getX() ){
            if( myPosition.getY() < otherPosition.getY() ) return Direction.BOTTOM;
            if( myPosition.getY() > otherPosition.getY() ) return Direction.TOP;
        }
        else if( myPosition.getY() == otherPosition.getY() ){
            if( myPosition.getX() < otherPosition.getX() ) return Direction.LEFT;
            if( myPosition.getX() > otherPosition.getX() ) return Direction.RIGHT;
        }
        return Direction.DIFFERENT;
    }


    public Coordinates getPosition() {
        return this.position;
    }


}
