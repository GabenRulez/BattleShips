package pl.edu.agh.iisg.to.battleships.model;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import pl.edu.agh.iisg.to.battleships.dao.HumanPlayerDao;
import pl.edu.agh.iisg.to.battleships.model.ai.AI;
import pl.edu.agh.iisg.to.battleships.model.ai.EasyAI;
import pl.edu.agh.iisg.to.battleships.model.ai.HardAI;
import pl.edu.agh.iisg.to.battleships.model.ai.MediumAI;
import pl.edu.agh.iisg.to.battleships.model.enums.GameStatus;
import pl.edu.agh.iisg.to.battleships.session.SessionService;

import javax.persistence.*;
import java.util.Map;
import java.util.Optional;

public class Game {

    public interface Callback {
        void onGameEnded(boolean hasPlayerWon);
        void onError(String errorMessage);
    }

    private Player player;

    private AI ai;

    private GameStatus currentState;

    private Integer difficultyLevel;

    private Callback callback;

    private Board playersBoard;

    private Board aisBoard;

    private Map<Integer, Integer> shipCounts;

    public Game(Player player, int boardSize, Map<Integer, Integer> shipCounts){
        if(player == null){
            player = getDefaultPlayer();
        }

        this.currentState = GameStatus.NOT_STARTED;

        this.setAI(new MediumAI());

        this.player = player;
        this.shipCounts = shipCounts;
        this.aisBoard = BoardInitializer.getBoardWithRandomlyPlacedShips(boardSize, shipCounts);
    }

    public Map<Integer, Integer> getShipCounts() {
        return shipCounts;
    }


    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private Player getDefaultPlayer(){

        SessionService.openSession();
        HumanPlayerDao playerDao = new HumanPlayerDao();
        Player player;
        Optional<Player> defaultPlayer = playerDao.findByMail("a@a.com");
        if(defaultPlayer.isEmpty()){
            defaultPlayer = playerDao.create("testowy", "a@a.com", "test"); //TODO: Debug Only. To delete later.
            player = defaultPlayer.orElseThrow(() -> new PersistenceException("Cannot create default player!"));
        }
        else{
            player = defaultPlayer.get();
        }

        SessionService.closeSession();
        return player;
    }


    public Player getPlayer() {
        return player;
    }

    public void updatePlayerInDb(){
        HumanPlayerDao playerDao = new HumanPlayerDao();
        playerDao.updatePlayer(this.player);
    }

    public void start(Board playersBoard) {
        this.currentState = GameStatus.IN_PROGRESS;
        this.playersBoard = playersBoard;
    }

    public void setCurrentState(GameStatus currentState) {
        this.currentState = currentState;
    }
    public GameStatus getCurrentState(){
        return this.currentState;
    }

    public void shoot(Coordinates coordinates) {
        if(currentState != GameStatus.IN_PROGRESS) return;
        try {
            if(aisBoard.shoot(coordinates)) {
                updateGameEnded();
            } else {
//                TODO Make computer move in another thread with delay, to improve visual effects.
                makeAiMove();
            }
        } catch (Exception e) {
            if(callback != null) {
                callback.onError(e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
    }

    public Integer getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(Integer difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Board getOpponentsBoard() {
        return aisBoard;
    }

    private void makeAiMove() throws InterruptedException {
        if(currentState != GameStatus.IN_PROGRESS) return;
        Coordinates positionToBeShot = ai.getNextAttackPosition(playersBoard);
        boolean hasHit = playersBoard.shoot(positionToBeShot);
        updateGameEnded();
        if(hasHit) {
            makeAiMove();
        }
    }

    private void updateGameEnded() {
        var hasPlayerWon = aisBoard.ships.stream().allMatch(Ship::isSunk);
        var hasAiWon = playersBoard.ships.stream().allMatch(Ship::isSunk);

        if(hasPlayerWon || hasAiWon) {
            this.currentState = GameStatus.FINISHED;
            if(callback != null) {
                callback.onGameEnded(hasPlayerWon);
            }
        }
    }

    public void setAI(AI ai){
        if(ai instanceof EasyAI){
            this.setDifficultyLevel(1);
        }
        else if(ai instanceof MediumAI){
            this.setDifficultyLevel(2);
        }
        else if(ai instanceof HardAI){
            this.setDifficultyLevel(3);
        }
        else throw new IllegalArgumentException("Invalid AI level!");

        this.ai = ai;

    }
}
