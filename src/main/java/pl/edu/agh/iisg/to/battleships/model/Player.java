package pl.edu.agh.iisg.to.battleships.model;

public abstract class Player {

    private Game currentGame;
    private final Board myBoard;

    private Player enemy;

    public Player(){
        this.myBoard = new Board(Config.BOARD_SIZE); // TODO tworzenie boarda powiązane z pozycjonowaniem statków
    }

    public Player(Game currentGame){
        this();
        this.currentGame = currentGame;
    }

    public void makeMove(Coordinates cords){
        this.currentGame.initializeAttack(enemy, cords);
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


    public Game getCurrentGame() {
        return currentGame;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

}
