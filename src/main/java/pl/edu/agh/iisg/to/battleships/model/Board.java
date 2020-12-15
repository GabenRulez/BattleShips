package pl.edu.agh.iisg.to.battleships.model;

import pl.edu.agh.iisg.to.battleships.model.enums.Direction;
import pl.edu.agh.iisg.to.battleships.model.enums.FieldStatus;

import java.util.ArrayList;
import java.util.Collection;
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

    public void addShip(Ship newShip) {
        this.ships.add(newShip);
        for(var field : newShip.getShipElements()){
            field.setFieldStatus(FieldStatus.FIELD_SHIP_ACTIVE);
        }
    }

    public Ship getShipAtPosition(Coordinates position) {
        return ships
            .stream()
            .filter(s -> s.getShipElements().stream().map(Field::getPosition).anyMatch(p -> p.equals(position)))
            .findFirst()
            .orElse(null);
    }

    public void removeShip(Ship shipToBeRemoved) {
        for(var field : shipToBeRemoved.getShipElements()) {
            field.setFieldStatus(FieldStatus.FIELD_EMPTY);
        }
        this.ships.remove(shipToBeRemoved);
    }

    public Field getFieldOnPosition(Coordinates position){
        return this.fields.get(position);
    }

    public boolean areCoordsInRange(Coordinates position){
        return position.less(this.limit) && position.getX() >= 0 && position.getY() >= 0;
    }

    public List<Field> getFieldsAround(Field field){
        ArrayList<Field> result = new ArrayList<>();
        for(int x=-1; x<=1; x++){
            for(int y=-1; y<=1; y++){
                if(x != 0 || y!= 0){
                    Coordinates toCheck = field.getPosition().add(new Coordinates(x, y));
                    if(this.areCoordsInRange(toCheck)){
                        result.add(this.getFieldOnPosition(toCheck));
                    }

                }
            }
        }
        return result;
    }

    public List<Field> getFieldsInCross(Field field){
        int[][] crossFieldVectors = new int[][]{new int[]{1,0}, new int[]{0,1}, new int[]{-1,0}, new int[]{0,-1}};
        ArrayList<Field> result = new ArrayList<>();
        Coordinates toCheck;

        for(int[] vector : crossFieldVectors){
            toCheck = field.getPosition().add(new Coordinates(vector[0], vector[1]));
            if(this.areCoordsInRange(toCheck)){
                result.add(this.getFieldOnPosition(toCheck));
            }
        }
        return result;
    }

    public Field getFieldInDirection(Field field, Direction direction){
        return getFieldInDirection(field, direction, 1);
    }

    public Field getFieldInDirection(Field field, Direction direction, int distance){
        Coordinates toCheck = field.getPosition();
        for(int i = 0; i < distance; i++) toCheck = toCheck.add(direction.getVector());
        if(this.areCoordsInRange(toCheck)){
            return this.getFieldOnPosition(toCheck);
        }
        return null;
    }

    public Field getFirstFieldWithShipHit(){ // returns the first field with ship, that was hit, but not already sunk
        for( Field field : this.fields.values() ){
            if(field.getFieldStatus() == FieldStatus.FIELD_SHIP_HIT){
                for( Field crossField : getFieldsInCross(field) ){
                    if( !crossField.wasShot() ){
                        return field;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Marks the field at given position as shot; If the shot has sunk a ship it marks all fields
     * around it as {@link FieldStatus#FIELD_EMPTY_BLOCKED}
     * @param coords target
     * @return true if the shot had any target; false otherwise
     * @throws IllegalArgumentException when given coords are out of the board range or the field
     * was already shot
     */
    public boolean shoot(Coordinates coords) throws IllegalArgumentException {
        if(!areCoordsInRange(coords)) {
            throw new IllegalArgumentException("Coords " + coords + " are out of range " + limit);
        }
        var field = getFieldOnPosition(coords);
        if(field.wasShot()) {
            throw new IllegalArgumentException("Field on position " + coords + " was already shot");
        }

        var ship = getShipAtPosition(coords);
        if(ship == null) {
            field.setFieldStatus(FieldStatus.FIELD_EMPTY_BLOCKED);
            return false;
        } else {
            field.setFieldStatus(FieldStatus.FIELD_SHIP_HIT);
            if(ship.isSunk()) {
                ship.getShipElements()
                    .stream()
                    .map(this::getFieldsAround)
                    .flatMap(List::stream)
                    .forEach(fieldAroundShip -> {
                        if(fieldAroundShip.getFieldStatus() == FieldStatus.FIELD_EMPTY) {
                            fieldAroundShip.setFieldStatus(FieldStatus.FIELD_EMPTY_BLOCKED);
                        }
                    });
            }
            return true;
        }
    }

    public List<Ship> getShips() {
        return this.ships;
    }

    public Collection<Field> getAllFields(){
        return this.fields.values();
    }
}
