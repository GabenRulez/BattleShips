package pl.edu.agh.iisg.to.battleships.model.ai;

import pl.edu.agh.iisg.to.battleships.model.Board;
import pl.edu.agh.iisg.to.battleships.model.Coordinates;
import pl.edu.agh.iisg.to.battleships.model.Field;
import pl.edu.agh.iisg.to.battleships.model.Ship;
import pl.edu.agh.iisg.to.battleships.model.enums.Direction;
import pl.edu.agh.iisg.to.battleships.model.enums.FieldStatus;

import java.util.*;
import java.util.stream.Collectors;

public class HardAI implements AI{

    private ArrayList<Field> possibleShipFields = new ArrayList<>();

    public Coordinates getNextAttackPosition(Board enemyBoard) {

        updatePossibleShipFields(enemyBoard);

        if (possibleShipFields.isEmpty()) addNewPossibleFields(enemyBoard);

        if (possibleShipFields.isEmpty()) {
            return chooseMostLikelySpotOnBoard(enemyBoard);
        } else
            return possibleShipFields.get((int) (Math.random() * possibleShipFields.size())).getPosition();       // If either possibleShipFields weren't empty or addNewPossibleFields didn't fail

    }


    private void updatePossibleShipFields(Board enemyBoard) {
        /*  Iterates over all fields that are in the ArrayList<Field> possibleShipFields.
            Looks for position, that was shot last round. When it finds it, then:
                -   checks, whether it was a ship or just sea tile.
                    If it was ship:
                        -   Clears the possibleShipFields ArrayList, as we don't want to try and shoot in the wrong directions.
                        -   Checks whether we already shot other part of the same ship. Looks in all possible directions,
                            and then, if one of the fields was hit previously, we decide on the direction, of where we should shoot next.
                            So we add 2 possible fields - before the beginning or after the end of our 2 known shot pieces.
                            (it should always find it's neighbour, as later, we fill possibleShipFields with fields,
                            that are in fact the neighbours of hit, but not sunk ships.
                    If it wasn't a ship: (so it was sea tile)
                        -   We remove that tile from the ArrayList (so it won't be considered in the next iteration)
        */
        for (Field tempField : possibleShipFields) {
            if (tempField.wasShot()) {    // we found previously shot position

                if (tempField.getFieldStatus() == FieldStatus.FIELD_SHIP_HIT) {   // we actually shot a ship

                    possibleShipFields = new ArrayList<>();                     // so we won't need other directions => make new array

                    Direction direction = null;
                    for (Field aroundField : enemyBoard.getFieldsInCross(tempField)) {    // for every Field in cross-like pattern (up, down, left, right)
                        if (aroundField.getFieldStatus() == FieldStatus.FIELD_SHIP_HIT) {
                            direction = tempField.getDirectionFrom(aroundField);      // now we know in which direction we should be shooting next
                            break;
                        }
                    }

                    if (direction != null) {
                        addToPossibleFields(enemyBoard, enemyBoard.getFieldInDirection(tempField, direction));
                        addToPossibleFields(enemyBoard, enemyBoard.getFieldInDirection(tempField, direction.reverse(), 2));
                    }

                } else {   // on our last shot we didn't hit the ship, so let's remove it
                    possibleShipFields.remove(tempField);
                }
                break;  // if we found the previously chosen spot, then we no longer have to query the others
            }
        }
    }

    private Coordinates chooseMostLikelySpotOnBoard(Board enemyBoard) {
        /*  We will select coordinates, that are the most probable to contain a ship, we are looking for.
            For each length of a ship, that hasn't sunk yet:
                -   for each field on the map:
                    -   for each direction there is, except Direction.DIFFERENT:
                        -   we create an ArrayList of fields, that the ship could be on
                        -   we filter these positions, by whether they were hit or not
                        -   if none of the positions were hit previously, then we add 1 to this position counter
            Next we choose a spot, that has the highest counter and return it.
         */

        List<Ship> ships = enemyBoard.getShips();
        ArrayList<Integer> shipLengths = new ArrayList<>();
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                int shipLength = ship.getLength();
                if (shipLength > 1) shipLengths.add(ship.getLength());
            }
        } // Now we have a list of all lengths of a ship to be found

        HashMap<Coordinates, Integer> probabilityDensityValues = new HashMap<>();

        for (Field field : enemyBoard.getAllFields()) {
            if (!field.wasShot()) {
                int fieldWeight = 0;
                int horizontalLength = 0, verticalLength = 0;
                for (int shipLength : shipLengths) {
//                    ArrayList<Field> shipFields = new ArrayList<>();
                    for (int i = 1; i <= (shipLength - 1); i++) {
                        if (enemyBoard.getFieldInDirection(field, Direction.LEFT, i) != null &&
                                !enemyBoard.getFieldInDirection(field, Direction.LEFT, i).wasShot()) {
                            horizontalLength++;
                        } else break;
                    }
                    for (int i = 1; i <= (shipLength - 1); i++) {
                        if (enemyBoard.getFieldInDirection(field, Direction.RIGHT, i) != null &&
                                !enemyBoard.getFieldInDirection(field, Direction.RIGHT, i).wasShot()) {
                            horizontalLength++;
                        } else break;
                    }
                    if((horizontalLength + 1) >= shipLength) {
                        fieldWeight += (horizontalLength + 1) - shipLength + 1;
                    }
                    for (int i = 1; i <= (shipLength - 1); i++) {
                        if (enemyBoard.getFieldInDirection(field, Direction.UP, i) != null &&
                                !enemyBoard.getFieldInDirection(field, Direction.UP, i).wasShot()) {
                            verticalLength++;
                        } else break;
                    }
                    for (int i = 1; i <= (shipLength - 1); i++) {
                        if (enemyBoard.getFieldInDirection(field, Direction.DOWN, i) != null &&
                                !enemyBoard.getFieldInDirection(field, Direction.DOWN, i).wasShot()) {
                            verticalLength++;
                        } else break;
                    }

                    if((verticalLength + 1) >= shipLength) {
                        fieldWeight += (verticalLength + 1) - shipLength + 1;
                    }
                }
                probabilityDensityValues.put(field.getPosition(), fieldWeight);
//                    for(Direction direction : Direction.values()){
//                        if(direction == Direction.DIFFERENT) continue;
//
//                        ArrayList<Field> shipFields = new ArrayList<>();
//                        shipFields.add(field);
//                        for(int i = 1; i <= shipLength; i++){
//                            Field veryTempField = enemyBoard.getFieldInDirection(field, direction, i);
//                            if(veryTempField != null) shipFields.add( veryTempField );
//                        }
//
//                        shipFields.removeIf(Field::wasShot);
//
//                        if(shipFields.size() == shipLength){    // So we didn't remove any ships => All positions are correct
//                            for(int i = 0; i < shipLength; i++) {
//                                Coordinates cords = shipFields.get(i).getPosition();
//                                probabilityDensityValues.merge(cords, 1, Integer::sum);
//
//                            }
//                        }
//                    }
            }
        }
        Integer maxValue = Collections.max(probabilityDensityValues.values());
        return probabilityDensityValues
                .entrySet()
                .stream()
                .filter(x -> x.getValue() == maxValue)
                .map(Map.Entry::getKey)
                .findFirst()
                .get();

//        return probabilityDensityValues.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : 0).get().getKey();
    }

    private void addNewPossibleFields(Board enemyBoard) {
        /*  Gets first spot from enemyBoard that has:
            -   ship part, that was hit
            -   fields around, that weren't all hit

            If there is such a spot, then it adds those fields around to possibleShipFields.
        */

        Field queriedField = enemyBoard.getFirstFieldWithShipHit();
        if (queriedField != null) {
            for (Field field : enemyBoard.getFieldsInCross(queriedField)) {
                addToPossibleFields(enemyBoard, field);
            }
        }
    }

    protected void addToPossibleFields(Board enemyBoard, Field field){
        /*  Adds coordinates if the are compliant with the requirements. Which are as follows:
            - is it in bounds of the board
            - if it was shot before, it's not compliant
        */

        if(enemyBoard.areCoordsInRange(field.getPosition())){
            if(!field.wasShot()){
                this.possibleShipFields.add(field);
            }
        }
    }



}