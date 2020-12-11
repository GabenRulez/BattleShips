package model;

import java.util.Arrays;
import java.util.Random;

public class ComputerPlayer extends Player{
    AI ai;
    private static final String[] possibleDifficultyLevels = new String[]{"easy", "medium"};

    public ComputerPlayer(Game game, String name){
        super(game, name);
    }

    public void setDifficultyLevel(String difficultyLevel) throws IllegalArgumentException {
        if(Arrays.asList(possibleDifficultyLevels).contains(difficultyLevel)){
            switch(difficultyLevel){
                case "easy" -> ai = new AI_Easy();
                case "medium" -> ai = new AI_Medium();
                default -> throw new IllegalStateException("Unexpected value in difficultyLevel: " + difficultyLevel);
            }
        }
        else{
            throw new IllegalArgumentException("Tried to set ComputerPlayer's difficulty to non-existant value: '" + difficultyLevel + "' . Possible values are as follows: " + Arrays.toString(possibleDifficultyLevels));
        }
    }

    public Coordinates chooseSpotToAttack(){ // difficultyLevel is from {"easy", "medium", "hard"}
        return this.ai.getNextAttackPosition(this.getEnemy().getBoard());
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
