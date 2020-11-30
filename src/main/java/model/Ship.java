package model;

import model.enums.ShipStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ship {

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
