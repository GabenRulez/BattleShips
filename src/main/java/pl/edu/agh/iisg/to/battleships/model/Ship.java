package pl.edu.agh.iisg.to.battleships.model;

import pl.edu.agh.iisg.to.battleships.model.enums.ShipStatus;

import java.util.List;

public class Ship {
    public enum Orientation {
        VERTICAL, HORIZONTAL;

        @Override
        public String toString(){
            switch(this){
                case HORIZONTAL -> {
                    return "horizontal";
                }
                case VERTICAL -> {
                    return "vertical";
                }
            }
            return "";
        }
    }

    private final List<Field> shipElements;
    ShipStatus status;

    public Ship(List<Field> shipElements){
        if(shipElements == null){
            throw new IllegalArgumentException("Ship must consist of some elements");
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

    public boolean isSunk() {
        return shipElements.stream().noneMatch(f -> f.getFieldStatus().isShootable());
    }
}
