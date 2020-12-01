package model;

import model.enums.ShipStatus;

import java.util.List;

public class Ship {
    public enum Orientation {
        VERTICAL, HORIZONTAL;
    }

    private final List<Field> shipElements;
    ShipStatus status;

    public Ship(List<Field> shipElements){
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

    public List<Field> getShipElements() {
        return shipElements;
    }

}
