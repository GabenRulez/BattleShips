package model;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Player {
    private final Game game;
    private final String name;
    protected final Board myBoard;
    private final Board enemyBoard;
    private Player enemy;
    private final AtomicBoolean waiting;    // dopóki true - czekaj

    public Player(Game game, String name){
        this.waiting = new AtomicBoolean(true);
        this.game = game;
        this.name = name;
        this.myBoard = new Board(); // TODO tworzenie boarda powiązane z pozycjonowaniem statków
        this.enemyBoard = new ShootingBoard();
    }

    public void setEnemy(Player enemy){
        this.enemy = enemy;
    }

    public boolean attackPosition(Coordinates cords){
        boolean result = enemy.getHit(cords);
        enemyBoard.shoot(cords);    // zaznaczmy sobie, czy trafiliśmy
        return result;
    }

    public boolean getHit(Coordinates cords){
        return this.myBoard.shoot(cords);
    }

    public void myTurn(){
        this.waiting.set(false);
    }

    public boolean isWorking(){
        return !this.waiting.get();
    }

    public void endOfMyTurn(){
        this.waiting.set(true);
    }



    public String getName(){
        return this.name;
    }

}
