package pl.edu.agh.to.weebs.battleships.model;

import pl.edu.agh.to.weebs.battleships.model.enums.FieldStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Board {
    Coordinates limit;
    HashMap<Coordinates,Field> fields;
    List<Ship> ships;

    public Board(Coordinates boardSize){
        this.limit = boardSize;
        this.fields = new HashMap<>();
        for(int i=0; i< boardSize.getX(); i++){
            for(int j=0; j<boardSize.getY(); j++){
                Coordinates location = new Coordinates(i, j);
                fields.put(location, new Field(location));
            }
        }
        this.ships = new ArrayList<>();
    }


    public Boolean addShip(Coordinates position, Boolean horizontal, Integer length){
        if(length <= 0){
            throw new IllegalArgumentException("Length of ship must be positive");
        }
        if(!this.checkShipPlacementAvailability(position, horizontal, length)){
            return false;
        }

        ArrayList<Field> shipParts = new ArrayList<Field>();
        for(int i=0; i< length; i++){
            if(horizontal) {
                shipParts.add(this.getField(position.add(new Coordinates(i, 0))));
            }else{
                shipParts.add(this.getField(position.add(new Coordinates(0, i))));
            }
        }
        Ship newShip = new Ship(shipParts);
        this.ships.add(newShip);
        return true;
    }

    private Boolean checkShipPlacementAvailability(Coordinates startPosition, Boolean horizontal, Integer length){
        if(!this.isOnBoard(startPosition)) return false;
        if(horizontal){
            if(!this.isOnBoard(startPosition.add(new Coordinates(length-1, 0)))) return false;
        }
        else {
            if(!this.isOnBoard(startPosition.add(new Coordinates(0, length-1)))) return false;
        }
        return true;
    }

    private void addShip(Ship ship){
        this.ships.add(ship);
        for(Field field : ship.getShipElements()){
            field.setFieldStatus(FieldStatus.FIELD_SHIP_ACTIVE);
        }
    }

    private Ship getShipOnPosition(Coordinates position){
        for(Ship ship : this.ships){
            if(ship.isOnPosition(position)) return ship;
        }
        return null;
    }

    public FieldStatus getFieldStatusOnPosition(Coordinates position){
        Field field = this.fields.get(position);
        return field.getFieldStatus();
    }

    public void setFieldStateOnPosition(Coordinates position, FieldStatus newStatus){
        Field field = this.fields.get(position);
        field.setFieldStatus(newStatus);
    }

    private boolean isOnBoard(Coordinates position){
        return position.lessOrEqual(this.limit);
    }

    private Field getField(Coordinates position){
        return this.fields.get(position);
    }

}
