package pl.edu.agh.to.weebs.battleships.model;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.util.List;
import java.util.Map;

/**
 * Class managing the process of placing ships on the board.
 */
public class BoardCreator {

    private final PublishSubject<Board> board;
    private final PublishSubject<List<Integer>> lengthsOfShipsYetToBePlaced;
    private final PublishSubject<Boolean> isUndoEnabled;
    private final PublishSubject<Boolean> isRedoEnabled;

    /**
     * Creates a BoardCreator managing placing the ships on a NxN board with specified ships
     *
     * @param boardSize Board size given as the length of the board's edge (value 10 passed here
     *                  creates a 10x10 board to place ships on)
     * @param shipCounts Map describing sizes and counts of the ships to be placed on the board during
     *                   creation process. A key-value pair of (2, 3) in the map means that 3 ships
     *                   of length 2 will be created.
     */
    public BoardCreator(int boardSize, Map<Integer, Integer> shipCounts) {
        board = PublishSubject.create();
        lengthsOfShipsYetToBePlaced = PublishSubject.create();
        isUndoEnabled = PublishSubject.create();
        isRedoEnabled = PublishSubject.create();
    }

    /**
     * @return Board representation during creation.
     */
    public Observable<Board> getBoard() {
        return board;
    }

    /**
     * @return Lengths of the ships that have to be placed before creation process can be finalized.
     */
    public Observable<List<Integer>> getLengthsOfShipsYetToBePlaced() {
        return lengthsOfShipsYetToBePlaced;
    }

    public PublishSubject<Boolean> getIsUndoEnabled() {
        return isUndoEnabled;
    }

    public PublishSubject<Boolean> getIsRedoEnabled() {
        return isRedoEnabled;
    }

    /**
     * Places a ship at the given position.
     * @param coords Upper right tile of the ship to be placed.
     * @param length Length of the ship to be placed.
     * @param orientation Orientation of the ship to be placed.
     * @return True if the ship was placed successfully or false if placing the ship
     * at the given place would overlap with another ship already present on the board.
     * @throws IllegalArgumentException Thrown when the ship would not fit in the board at the given place
     * or if all ships of given length have already been placed.
     */
    public boolean placeShip(
        Coordinates coords,
        int length,
        Ship.Orientation orientation
    ) throws IllegalArgumentException {

        return true;
    }

    /**
     * Removes a ship that is placed on a given position.
     * @param coords Coords of a position to remove a ship from.
     * @return True if the ship was successfully removed or false if there was
     * no ship found at the given position.
     * @throws IllegalArgumentException Thrown if given coords are not in range of the board.
     */
    public boolean removeShip(Coordinates coords) throws IllegalArgumentException {

        return true;
    }

    /**
     * Undoes last ship placement/removal action.
     */
    public void undo() {

    }

    /**
     * Redoes previously undone action of ship placement/removal.
     */
    public void redo() {

    }
}