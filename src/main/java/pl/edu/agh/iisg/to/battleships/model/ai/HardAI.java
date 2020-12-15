package pl.edu.agh.iisg.to.battleships.model.ai;

import pl.edu.agh.iisg.to.battleships.model.Board;
import pl.edu.agh.iisg.to.battleships.model.Coordinates;
import pl.edu.agh.iisg.to.battleships.model.Field;
import pl.edu.agh.iisg.to.battleships.model.Ship;
import pl.edu.agh.iisg.to.battleships.model.enums.Direction;
import pl.edu.agh.iisg.to.battleships.model.enums.FieldStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HardAI implements AI {
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
            Field queriedField = enemyBoard.getFirstFieldWithShipHit();
            if(queriedField != null){   // if we can find a field with ship_hit, that wasn't tried before (dosn't have
                for(Field field : enemyBoard.getFieldsInCross(queriedField)){
                    if(!field.wasShot()){
                        possibleShipCords.add(field.getPosition());
                    }
                }
                return possibleShipCords.get( (int)(Math.random() * possibleShipCords.size()) );
            }

            return chooseMostLikelySpotOnBoard(enemyBoard);
        }
        else{
            return possibleShipCords.get( (int)(Math.random() * possibleShipCords.size()) );    // return random cords from the previously chosen
        }

    }

    public Coordinates chooseMostLikelySpotOnBoard(Board enemyBoard){
        List<Ship> ships = enemyBoard.getShips();
        ArrayList<Integer> lengthsToFind = new ArrayList<>();
        /*for(Ship ship : ships){
            if(ship.getStatus() != ShipStatus.SHIP_SUNK){
                lengthsToFind.add(ship.getLength());
            }
        } // Now we have a list of all lengths of a ship to be found

        Coordinates boardLimit = enemyBoard.getLimit();
        boolean[][] spotTaken = new boolean[boardLimit.getX()][boardLimit.getY()];
        int[][] probabilityDensityValues = new int[boardLimit.getX()][boardLimit.getY()];

        for(Field field : enemyBoard.getAllFields()){
            if(field.wasShot()){
                spotTaken[field.getPosition().getX()][field.getPosition().getY()] = true;
            }
        } // Now we have marked all the spots, that we can't choose from (there is 0% chance that on those spots there is a ship, that wasn't yet hit
        */




        return null;

    }

    private void iteration(List<Integer> lengthsToFind, boolean[][] spotTaken, int[][] probabilityDensityValues, Coordinates boardLimit){
        // Take first length of a ship that we need to find
        // For every possible placement of that ship:
        //      Block the positions, that this ship would block and run recurrently for ship placement without this ship
        //          If it returns true (so all ships found their correct place), then this is a good possibility - > add values to probabilityDensityValues.

        int shipLength = lengthsToFind.get(0);
        // Looking horizontally
        for(int x = 0; x < boardLimit.getX() - shipLength + 1; x++){
            for(int y = 0; y < boardLimit.getY(); y++){
                boolean isFree = true;
                for(int delta_x = 0; delta_x < shipLength; delta_x++){  // Let's see, if all the necessary blocks are free for this ship to be
                    if(spotTaken[x + delta_x][y]){
                        isFree = false;
                        break;
                    }
                }
                if(isFree){ // If it is indeed free then run recurrently

                }

            }
        }

        //Looking vertically
        for(int x = 0; x < boardLimit.getX(); x++){
            for(int y = 0; y < boardLimit.getY() - shipLength + 1; y++){

            }
        }

    }

    private void iteration_horizontally(List<Integer> lengthsToFind, boolean[][] spotTaken, int[][] probabilityDensityValues, Coordinates boardLimit){
        int shipLength = lengthsToFind.get(0);
        for(int x = 0; x < boardLimit.getX() - shipLength + 1; x++){
            for(int y = 0; y < boardLimit.getY(); y++){
                boolean isFree = true;
                for(int delta_x = 0; delta_x < shipLength; delta_x++){  // Let's see, if all the necessary blocks are free for this ship to be
                    if(spotTaken[x + delta_x][y]){
                        isFree = false;
                        break;
                    }
                }
                if(isFree){ // If it is indeed free then run recurrently

                    List<Integer> newLengthsToFind = new ArrayList<>(lengthsToFind);
                    newLengthsToFind.remove(lengthsToFind.get(0));

                    /*boolean[][] newSpotTaken = Arrays.stream(spotTaken).map(a ->  Arrays.copyOf(a, a.length)).toArray(boolean[][]::new);
                    int[][] newProbabilityDensityValues = Arrays.stream(probabilityDensityValues).map(a ->  Arrays.copyOf(a, a.length)).toArray(int[][]::new);
*/


                    //iteration();
                }

            }
        }
    }


}