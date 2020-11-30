package model;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Player {
    private final Game game;
    private final String name;
    private final Board myBoard;

    private Player enemy;

    public Player(Game game, String name){
        this.game = game;
        this.name = name;
        this.myBoard = new Board(Config.BOARD_SIZE); // TODO tworzenie boarda powiązane z pozycjonowaniem statków
    }

    public void makeMove(Coordinates cords){
        this.game.initializeAttack(enemy, cords);
    }

    public Board getBoard() {
        return myBoard;
    }

    public Player getEnemy() {
        return enemy;
    }

    public void setEnemy(Player enemy) {
        this.enemy = enemy;
    }


    public String getName(){
        return this.name;
    }

}
