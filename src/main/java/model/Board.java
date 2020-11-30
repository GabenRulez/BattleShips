package model;

import model.enums.FieldStatus;

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

    public Coordinates getLimit() {
        return limit;
    }

    public Boolean addShip(Coordinates position, Boolean horizontal, Integer length){
        if(length <= 0){
            throw new IllegalArgumentException("Length of ship must be positive");
        }
        if(!this.checkShipPlacementOnBoard(position, horizontal, length)){
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



        if(this.existCollisions(shipParts)){
           return false;
        }

        Ship newShip = new Ship(shipParts);
        this.ships.add(newShip);
        for(Field field : newShip.getShipElements()){
            field.setFieldStatus(FieldStatus.FIELD_SHIP_ACTIVE);
        }
        return true;
    }

    public Boolean existCollisions(ArrayList<Field> shipParts){
        for(Field part : shipParts){
            for(Field next: this.getFieldsAround(part)){
                if(next.getFieldStatus() == FieldStatus.FIELD_SHIP_ACTIVE){
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean checkShipPlacementOnBoard(Coordinates startPosition, Boolean horizontal, Integer length){
        if(!this.isOnBoard(startPosition)) return false;
        if(horizontal){
            if(!this.isOnBoard(startPosition.add(new Coordinates(length-1, 0)))) return false;
        }
        else {
            if(!this.isOnBoard(startPosition.add(new Coordinates(0, length-1)))) return false;
        }
        return true;
    }


    private Ship getShipOnPosition(Coordinates position){
        for(Ship ship : this.ships){
            if(ship.isOnPosition(position)) return ship;
        }
        return null;
    }

    public Field getFieldOnPosition(Coordinates position){
        return this.fields.get(position);
    }

    public void setFieldStatusOnPosition(Coordinates position, FieldStatus newStatus){
        Field field = this.fields.get(position);
        field.setFieldStatus(newStatus);
    }

    private boolean isOnBoard(Coordinates position){
        return position.less(this.limit) && position.getX() >= 0 && position.getY() >= 0;
    }

    private Field getField(Coordinates position){
        return this.fields.get(position);
    }


    private ArrayList<Field> getFieldsAround(Field field){
        ArrayList<Field> result = new ArrayList<Field>();
        for(int x=-1; x<=1; x++){
            for(int y=-1; y<=1; y++){
                if(x != 0 || y!= 0){
                    Coordinates toCheck = field.getPosition().add(new Coordinates(x, y));
                    if(this.isOnBoard(toCheck)){
                        result.add(this.getFieldOnPosition(toCheck));
                    }

                }
            }
        }
        return result;
    }
}
