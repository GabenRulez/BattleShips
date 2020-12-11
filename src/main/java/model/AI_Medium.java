package model;

import model.enums.Direction;
import model.enums.FieldStatus;

import java.util.ArrayList;
import java.util.Random;

public class AI_Medium implements AI {
    Random random = new Random();
    private ArrayList<Coordinates> possibleShipCords = new ArrayList<>();

    @Override
    public Coordinates getNextAttackPosition(Board enemyBoard) {
        int i = 0;
        while(i < possibleShipCords.size()){
            Coordinates tempCords = possibleShipCords.get(i);
            Field tempField = enemyBoard.getFieldOnPosition(tempCords);
            if(tempField.wasShot()){    // we found previously shot position

                if(tempField.getFieldStatus() == FieldStatus.FIELD_SHIP_HIT){   // we actually shot a ship
                    possibleShipCords = new ArrayList<>();
                    Direction direction = null;
                    for(Field aroundField : enemyBoard.getFieldsInCross(tempField)){    // for every Field in cross-like pattern (up, down, left, right)
                        if(aroundField.getFieldStatus() == FieldStatus.FIELD_SHIP_HIT){
                            direction = tempField.getDirectionFrom(aroundField);      // now we know in which direction we should be shooting next
                            break;
                        }
                    }
                    if(direction != null){  // if last shot position wasn't at the end of the map
                        Field nextPossibleField = enemyBoard.getFieldInDirection(tempField, direction);
                        if(nextPossibleField != null){
                            possibleShipCords.add(nextPossibleField.getPosition());
                        }
                    }
                }
                else{   // on our last shot we didn't hit the ship, so let's remove it
                    possibleShipCords.remove(tempCords);
                }
                break;  // if we found the previously chosen spot, then we no longer have to query the others
            }
        }


        if(possibleShipCords.isEmpty()){
            Field queriedField = enemyBoard.getFieldWithShipHit();
            if(queriedField != null){   // if we can find a field with ship_hit, that wasn't tried before (dosn't have
                for(Field field : enemyBoard.getFieldsInCross(queriedField)){
                    if(!field.wasShot()){
                        possibleShipCords.add(field.getPosition());
                    }
                }
                return possibleShipCords.get( (int)(Math.random() * possibleShipCords.size()) );
            }

            return pickRandomSpot(enemyBoard);
        }
        else{
            return possibleShipCords.get( (int)(Math.random() * possibleShipCords.size()) );    // return random cords from the previously chosen
        }
    }

    private Coordinates pickRandomSpot(Board enemyBoard){
        int randomX = random.nextInt(enemyBoard.limit.getX());
        int randomY = random.nextInt(enemyBoard.limit.getY());
        Coordinates chosenCords = new Coordinates(randomX,randomY);

        while( enemyBoard.getFieldOnPosition(chosenCords).wasShot() ){
            randomX = random.nextInt(enemyBoard.limit.getX());
            randomY = random.nextInt(enemyBoard.limit.getY());
            chosenCords.set(randomX, randomY);
        }
        return chosenCords;
    }
}
