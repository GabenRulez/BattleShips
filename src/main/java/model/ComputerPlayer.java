package model;

import model.ai.AI;
import model.ai.EasyAI;
import model.ai.MediumAI;

import java.util.Arrays;

public class ComputerPlayer extends Player{
    AI ai;
    private static final String[] possibleDifficultyLevels = new String[]{"easy", "medium"};

    public ComputerPlayer(Game game, String name){
        super(game, name);
    }

    public void setDifficultyLevel(String difficultyLevel) throws IllegalArgumentException {
        if(Arrays.asList(possibleDifficultyLevels).contains(difficultyLevel)){
            switch(difficultyLevel){
                case "easy" -> ai = new EasyAI();
                case "medium" -> ai = new MediumAI();
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

}
