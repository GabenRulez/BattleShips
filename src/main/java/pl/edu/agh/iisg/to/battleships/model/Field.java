package pl.edu.agh.iisg.to.battleships.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import pl.edu.agh.iisg.to.battleships.model.enums.Direction;
import pl.edu.agh.iisg.to.battleships.model.enums.FieldStatus;

public class Field {
    private final Coordinates position;


    private final ObjectProperty<FieldStatus> fieldStatus;

    public Field(Coordinates position){
        this.position = position;
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
        return !this.fieldStatus.get().isShootable();
    }

    public Direction getDirectionFrom(Field other){
        Coordinates myPosition = this.getPosition();
        Coordinates otherPosition = other.getPosition();

        if( myPosition.getX() == otherPosition.getX() ){
            if( myPosition.getY() < otherPosition.getY() ) return Direction.UP;
            if( myPosition.getY() > otherPosition.getY() ) return Direction.DOWN;
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
