package model.ai;

import model.Board;
import model.Coordinates;

import java.util.Random;

public class EasyAI implements AI {
    Random random = new Random();

    @Override
    public Coordinates getNextAttackPosition(Board enemyBoard) {
        return getRandomCoordinates(enemyBoard, random);
    }

    static Coordinates getRandomCoordinates(Board enemyBoard, Random random) {
        Coordinates enemyBoardLimit = enemyBoard.getLimit();

        int randomX = random.nextInt(enemyBoardLimit.getX());
        int randomY = random.nextInt(enemyBoardLimit.getY());
        Coordinates chosenCords = new Coordinates(randomX,randomY);

        while( enemyBoard.getFieldOnPosition(chosenCords).wasShot() ){
            randomX = random.nextInt(enemyBoardLimit.getX());
            randomY = random.nextInt(enemyBoardLimit.getY());
            chosenCords.set(randomX, randomY);
        }
        return chosenCords;
    }
}
