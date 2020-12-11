package model;

import java.util.Random;

public class AI_Easy implements AI {
    Random random = new Random();

    @Override
    public Coordinates getNextAttackPosition(Board enemyBoard) {
        return getRandomCoordinates(enemyBoard, random);
    }

    static Coordinates getRandomCoordinates(Board enemyBoard, Random random) {
        int randomX = random.nextInt(enemyBoard.limit.getX());
        int randomY = random.nextInt(enemyBoard.limit.getY());
        Coordinates chosenCords = new Coordinates(randomX,randomY);

        while( enemyBoard.getFieldOnPosition(chosenCords).wasShot() ){
            randomX = random.nextInt(enemyBoard.limit.getX());
            randomY = random.nextInt(enemyBoard.limit.getY());
            chosenCords.set(randomX, randomY);
        }
        return chosenCords;
    }
}
