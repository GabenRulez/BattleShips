package model;

import model.enums.Direction;
import model.enums.FieldStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ComputerPlayer extends Player{
    String difficultyLevel;
    private static final String[] possibleDifficultyLevels = new String[]{"easy", "medium"};

    public ComputerPlayer(Game game, String name){
        super(game, name);
    }

    public void setDifficultyLevel(String difficultyLevel) throws IllegalArgumentException {
        if(Arrays.asList(possibleDifficultyLevels).contains(difficultyLevel)){
            this.difficultyLevel = difficultyLevel;
        }
        else{
            throw new IllegalArgumentException("Tried to set ComputerPlayer's difficulty to non-existant value: '" + difficultyLevel + "' . Possible values are as follows: " + Arrays.toString(possibleDifficultyLevels));
        }
    }



    public Coordinates chooseSpotToAttack(){ // difficultyLevel is from {"easy", "medium", "hard"}
        return switch (difficultyLevel) {
            case "easy" -> getEasyChoice();
            case "medium" -> getMediumChoice();
            default -> throw new IllegalStateException("Unexpected value in difficultyLevel: " + difficultyLevel);
        };
    }

    private Coordinates getEasyChoice(){
        Board enemyBoard = getEnemy().getBoard();
        Coordinates chosenCords = new Coordinates(0,0);

        while( enemyBoard.getFieldOnPosition(chosenCords).wasShot() ){
            int randomX = (int)(Math.random() * enemyBoard.limit.getX());
            int randomY = (int)(Math.random() * enemyBoard.limit.getY());
            chosenCords.set(randomX, randomY);
        }
        return chosenCords;
    }

    private ArrayList<Coordinates> possibleShipCords = new ArrayList<>();
    private Coordinates getMediumChoice(){
        Board enemyBoard = getEnemy().getBoard();

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

            return getEasyChoice();
        }
        else{
            return possibleShipCords.get( (int)(Math.random() * possibleShipCords.size()) );    // return random cords from the previously chosen
        }
    }





//    public void start() throws InterruptedException {
//        while(true){
//            while(this.isWorking() == false){
//                sleep(100);
//            }
//
//            System.out.println(getName() + ": Wykonuję ruch.");
//
//            // TODO get AI input
//            makeMove(new Coordinates((int)Math.floor(Math.random()*10),(int)Math.floor(Math.random()*10)));
//
//            endOfMyTurn();
//        }
//    }
}
