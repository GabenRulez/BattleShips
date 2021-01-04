package pl.edu.agh.iisg.to.battleships.model.ai;

import pl.edu.agh.iisg.to.battleships.model.Board;
import pl.edu.agh.iisg.to.battleships.model.Coordinates;
import pl.edu.agh.iisg.to.battleships.model.Field;
import pl.edu.agh.iisg.to.battleships.model.enums.Direction;
import pl.edu.agh.iisg.to.battleships.model.enums.FieldStatus;

import java.util.ArrayList;
import java.util.Random;

public class MediumAI implements AI {
    Random random = new Random();
    private ArrayList<Field> possibleShipFields = new ArrayList<>();

    @Override
    public Coordinates getNextAttackPosition(Board enemyBoard) {

        updatePossibleShipFields(enemyBoard);

        if(possibleShipFields.isEmpty()) addNewPossibleFields(enemyBoard);

        if(possibleShipFields.isEmpty()) return EasyAI.getRandomCoordinates(enemyBoard, random);                    // If addNewPossibleFields failed to add any fields of significance
        else return possibleShipFields.get( (int)(Math.random() * possibleShipFields.size()) ).getPosition();       // If either possibleShipFields weren't empty or addNewPossibleFields didn't fail

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

    private void updatePossibleShipFields(Board enemyBoard){
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
        for(Field tempField : possibleShipFields){
            if(tempField.wasShot()){    // we found previously shot position

                if(tempField.getFieldStatus() == FieldStatus.FIELD_SHIP_HIT){   // we actually shot a ship

                    possibleShipFields = new ArrayList<>();                     // so we won't need other directions => make new array

                    Direction direction = null;
                    for(Field aroundField : enemyBoard.getFieldsInCross(tempField)){    // for every Field in cross-like pattern (up, down, left, right)
                        if(aroundField.getFieldStatus() == FieldStatus.FIELD_SHIP_HIT){
                            direction = tempField.getDirectionFrom(aroundField);      // now we know in which direction we should be shooting next
                            break;
                        }
                    }

                    if(direction != null){
                        addToPossibleFields( enemyBoard, enemyBoard.getFieldInDirection(tempField, direction) );
                        addToPossibleFields( enemyBoard, enemyBoard.getFieldInDirection(tempField, direction.reverse(), 2));
                    }

                }
                else{   // on our last shot we didn't hit the ship, so let's remove it
                    possibleShipFields.remove(tempField);
                }
                break;  // if we found the previously chosen spot, then we no longer have to query the others
            }
        }
    }

    private void addNewPossibleFields(Board enemyBoard){
        /*  Gets first spot from enemyBoard that has:
            -   ship part, that was hit
            -   fields around, that weren't all hit

            If there is such a spot, then it adds those fields around to possibleShipFields.
        */

        Field queriedField = enemyBoard.getFirstFieldWithShipHit();
        if(queriedField != null){
            for(Field field : enemyBoard.getFieldsInCross(queriedField)){
                addToPossibleFields(enemyBoard, field);
            }
        }
    }
}
