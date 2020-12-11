package pl.edu.agh.iisg.to.battleships.model;

import model.ai.AI;
import model.ai.EasyAI;
import model.ai.MediumAI;

import java.util.Arrays;

public class ComputerPlayer extends Player{
    AI ai;

    public ComputerPlayer(Game game, AI ai){
        super(game);
        this.ai = ai;
    }

    public Coordinates chooseSpotToAttack(){ // difficultyLevel is from {"easy", "medium", "hard"}
        return this.ai.getNextAttackPosition(this.getEnemy().getBoard());
    }
}
