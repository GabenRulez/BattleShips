package pl.edu.agh.iisg.to.battleships.model;

import pl.edu.agh.iisg.to.battleships.model.ai.AI;

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
