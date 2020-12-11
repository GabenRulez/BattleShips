package pl.edu.agh.iisg.to.battleships.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import pl.edu.agh.iisg.to.battleships.model.enums.FieldStatus;

public class Field {
    private final Coordinates position;


    private ObjectProperty<FieldStatus> fieldStatus;

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




    public Coordinates getPosition() {
        return this.position;
    }


}
