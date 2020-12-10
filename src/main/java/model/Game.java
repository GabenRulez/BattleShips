package model;

import model.enums.GameStatus;

public class Game {
    private HumanPlayer human;
    private ComputerPlayer computer;
    private Player winner;
    public GameStatus currentState;

    public Game(){
        this.currentState = GameStatus.GAME_NOT_STARTED;
    }

    public HumanPlayer getHuman() {
        return human;
    }

    public ComputerPlayer getComputer() {
        return computer;
    }

    public GameStatus getCurrentState(){
        return this.currentState;
    }

    public void initialize() {
        human = new HumanPlayer(this, "Gracz 1"); // TODO get username
        computer = new ComputerPlayer(this, "T-800");
        winner = null;
        this.currentState = GameStatus.GAME_SHIP_PLACEMENT;
    }

    public void setCurrentState(GameStatus currentState) {
        this.currentState = currentState;
    }

    public void initializeAttack(Player target, Coordinates targetCoords){

    }
}
