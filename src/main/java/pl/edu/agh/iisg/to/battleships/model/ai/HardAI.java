package pl.edu.agh.iisg.to.battleships.model.ai;

import pl.edu.agh.iisg.to.battleships.model.Board;
import pl.edu.agh.iisg.to.battleships.model.Coordinates;
import pl.edu.agh.iisg.to.battleships.model.Field;
import pl.edu.agh.iisg.to.battleships.model.Ship;
import pl.edu.agh.iisg.to.battleships.model.enums.Direction;
import pl.edu.agh.iisg.to.battleships.model.enums.FieldStatus;

import java.util.*;
import java.util.function.Predicate;

public class HardAI extends MediumAI {

    @Override
    public Coordinates getNextAttackPosition(Board enemyBoard) {

        updatePossibleShipFields(enemyBoard);

        if(possibleShipFields.isEmpty()) addNewPossibleFields(enemyBoard);

        if(possibleShipFields.isEmpty()) {
            return chooseMostLikelySpotOnBoard(enemyBoard);
        }
        else return possibleShipFields.get( (int)(Math.random() * possibleShipFields.size()) ).getPosition();       // If either possibleShipFields weren't empty or addNewPossibleFields didn't fail

    }

    public Coordinates chooseMostLikelySpotOnBoard(Board enemyBoard){
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
        for(Ship ship : ships){
            if(!ship.isSunk()){
                int shipLength = ship.getLength();
                if(shipLength > 1) shipLengths.add(ship.getLength());
            }
        } // Now we have a list of all lengths of a ship to be found

        HashMap<Coordinates, Integer> probabilityDensityValues = new HashMap<>();

        for(int shipLength : shipLengths){
            for(Field field : enemyBoard.getAllFields()){
                if(!field.wasShot()){
                    for(Direction direction : Direction.values()){
                        if(direction == Direction.DIFFERENT) continue;

                        ArrayList<Field> shipFields = new ArrayList<>();
                        shipFields.add(field);
                        for(int i = 1; i <= shipLength; i++){
                            Field veryTempField = enemyBoard.getFieldInDirection(field, direction, i);
                            if(veryTempField != null) shipFields.add( veryTempField );
                        }

                        shipFields.removeIf(filterShotFields());

                        if(shipFields.size() == shipLength){    // So we didn't remove any ships => All positions are correct
                            for(int i = 0; i < shipLength; i++) {
                                Coordinates cords = shipFields.get(i).getPosition();
                                probabilityDensityValues.merge(cords, 1, Integer::sum);

                            }
                        }
                    }
                }
            }
        }
        return probabilityDensityValues.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : 0).get().getKey();
    }

    private static Predicate<Field> filterShotFields(){
        return Field::wasShot;
    }

}