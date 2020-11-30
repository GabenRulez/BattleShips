package pl.edu.agh.to.weebs.battleships.model;

import pl.edu.agh.to.weebs.battleships.model.enums.ShipStatus;

import java.util.ArrayList;

public class Ship {
    enum Orientation {
        VERTICAL, HORIZONTAL;
    }

    private final ArrayList<Field> shipElements;
    ShipStatus status;

    public Ship(ArrayList<Field> shipElements){
        if(shipElements == null){
            throw new IllegalArgumentException("Ship must consist of elements");
        }
        this.shipElements = shipElements;
        this.status = ShipStatus.SHIP_ACTIVE;
    }


    public Boolean isOnPosition(Coordinates position){
        for (Field field: shipElements){
            if(field.getPosition() == position){
                return true;
            }
        }
        return false;
    }
    public Integer getLength(){
        return this.shipElements.size();
    }

    public ArrayList<Field> getShipElements() {
        return shipElements;
    }

}
