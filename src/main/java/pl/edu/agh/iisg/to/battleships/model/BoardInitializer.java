package pl.edu.agh.iisg.to.battleships.model;

import pl.edu.agh.iisg.to.battleships.model.ai.EasyAI;

import java.util.Map;
import java.util.Random;

public class BoardInitializer {
    private static final int MAX_SHIP_PLACEMENT_TRIALS = 32;

    /**
     * Creates a NxN Board with randomly placed ships
     *
     * @param boardSize Board size given as the length of the board's edge (value 10 passed here
     *                  creates a 10x10 board to place ships on)
     * @param shipCounts Map describing sizes and counts of the ships to be placed on the board during
     *                   creation process. A key-value pair of (2, 3) in the map means that 3 ships
     *                   of length 2 will be created.
     */
    public static Board getBoardWithRandomlyPlacedShips(int boardSize, Map<Integer, Integer> shipCounts) {
        return getBoardCreatorWithRandomlyPlacedShips(boardSize, shipCounts).getBoard();
    }

    public static BoardCreator getBoardCreatorWithRandomlyPlacedShips(int boardSize, Map<Integer, Integer> shipCounts) {
        var boardCreator = new BoardCreator(boardSize, shipCounts);
        var random = new Random();

        while(!boardCreator.isCreationProcessFinished()) {
            var unsuccessfulTrialsCount = 0;
            while(!placeShipRandomly(boardCreator, random) && unsuccessfulTrialsCount < MAX_SHIP_PLACEMENT_TRIALS) {
                unsuccessfulTrialsCount++;
            }
            if(unsuccessfulTrialsCount >= MAX_SHIP_PLACEMENT_TRIALS) {
                boardCreator = new BoardCreator(boardSize, shipCounts);
            }
        }

        return boardCreator;
    }

    private static boolean placeShipRandomly(BoardCreator boardCreator, Random random) {
        var coords = EasyAI.getRandomCoordinates(boardCreator.getBoard(), random);
        var orientation = random.nextBoolean() ? Ship.Orientation.HORIZONTAL : Ship.Orientation.VERTICAL;
        var shipsYetToBePlaced = boardCreator.getLengthsOfShipsYetToBePlaced();
        var length = shipsYetToBePlaced.get(random.nextInt(shipsYetToBePlaced.size()));

        try {
            return boardCreator.placeShip(coords, length, orientation);
        } catch (Exception ignored) {
            return false;
        }
    }
}
