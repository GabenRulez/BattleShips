package pl.edu.agh.iisg.to.battleships.model;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.edu.agh.iisg.to.battleships.model.enums.FieldStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Class managing the process of placing ships on the board.
 */
public class BoardCreator {

    private final Board board;
    private final ObservableList<Integer> lengthsOfShipsYetToBePlaced;

    private final BoardCreatorCommandRegistry commandRegistry;

    private final BooleanProperty isUndoEnabled;
    private final BooleanProperty isRedoEnabled;
    private final BooleanProperty isCreationProcessFinished;

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
        board = new Board(new Coordinates(boardSize, boardSize));
        commandRegistry = new BoardCreatorCommandRegistry();
        isUndoEnabled = commandRegistry.isIsUndoEnabled();
        isRedoEnabled = commandRegistry.isIsRedoEnabled();

        var shipLengthsToBeCreated = shipCounts
                .entrySet()
                .stream()
                .map(entry -> Collections.nCopies(entry.getValue(), entry.getKey()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        lengthsOfShipsYetToBePlaced = FXCollections.observableList(shipLengthsToBeCreated);

        isCreationProcessFinished = new SimpleBooleanProperty();
        isCreationProcessFinished.bind(Bindings.isEmpty(lengthsOfShipsYetToBePlaced));
    }

    /**
     * @return Board on which created ships will be placed on.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return Lengths of the ships that have to be placed before creation process can be finalized.
     */
    public ObservableList<Integer> getLengthsOfShipsYetToBePlaced() {
        return lengthsOfShipsYetToBePlaced;
    }

    public BooleanProperty getUndoEnabledProperty() {
        return isUndoEnabled;
    }

    public BooleanProperty getRedoEnabledProperty() {
        return isRedoEnabled;
    }

    /**
     * @return Property indicating whether all ships are already placed.
     */
    public BooleanProperty getCreationProcessFinishedProperty() {
        return isCreationProcessFinished;
    }

    /**
     * Places a ship at the given position.
     * @param shipCoords Upper right tile of the ship to be placed.
     * @param length Length of the ship to be placed.
     * @param orientation Orientation of the ship to be placed.
     * @return True if the ship was placed successfully or false if placing the ship
     * at the given place would overlap with another ship already present on the board.
     * @throws IllegalArgumentException Thrown when the ship would not fit in the board at the given place
     * or if all ships of given length have already been placed.
     */
    public boolean placeShip(
        Coordinates shipCoords,
        int length,
        Ship.Orientation orientation
    ) throws IllegalArgumentException {
        if(lengthsOfShipsYetToBePlaced.stream().noneMatch(len -> len == length)) {
            throw new IllegalArgumentException("All ships of this length are already placed.");
        }

        var shipFieldCoords = getFieldCoordsForShipAtPosition(shipCoords, length, orientation);

        if(shipFieldCoords.stream().anyMatch(coords -> !board.areCoordsInRange(coords))) {
            throw new IllegalArgumentException(String.format(
                    "Ship of length %d at position %s and orientation %s would not fit in the board",
                    length, shipCoords.toString(), orientation.toString()
            ));
        }

        var shipFields = shipFieldCoords
                .stream()
                .map(board::getFieldOnPosition)
                .collect(Collectors.toList());

        var fieldsAroundShip = shipFields
                .stream()
                .map(board::getFieldsAround)
                .flatMap(List::stream);

        var isAnyShipFieldOrFieldAroundItOccupied =
                Stream.concat(shipFields.stream(), fieldsAroundShip)
                        .anyMatch(field -> field.getFieldStatus() == FieldStatus.FIELD_SHIP_ACTIVE);

        if(isAnyShipFieldOrFieldAroundItOccupied) {
            return false;
        }

        var command = new PlaceShipCommand(board, lengthsOfShipsYetToBePlaced, new Ship(shipFields));
        commandRegistry.executeCommand(command);

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
        if(!board.areCoordsInRange(coords)) {
            throw new IllegalArgumentException(
                    String.format("Given coords %s are not in board range", coords.toString())
            );
        }

        var shipToBeRemoved = board.getShipAtPosition(coords);

        if(shipToBeRemoved == null) {
            return false;
        }

        var command = new RemoveShipCommand(board, lengthsOfShipsYetToBePlaced, shipToBeRemoved);
        commandRegistry.executeCommand(command);

        return true;
    }

    /**
     * Undoes last ship placement/removal action.
     */
    public void undo() {
        commandRegistry.undo();
    }

    /**
     * Redoes previously undone action of ship placement/removal.
     */
    public void redo() {
        commandRegistry.redo();
    }

    private List<Coordinates> getFieldCoordsForShipAtPosition(
            Coordinates coords,
            int length,
            Ship.Orientation orientation
    ) {
        var shipFieldsXPositions = orientation == Ship.Orientation.VERTICAL ?
                Collections.nCopies(length, coords.getX()) :
                IntStream.range(coords.getX(), coords.getX() + length).boxed().collect(Collectors.toList());
        var shipFieldsYPositions = orientation == Ship.Orientation.HORIZONTAL ?
                Collections.nCopies(length, coords.getY()) :
                IntStream.range(coords.getY(), coords.getY() + length).boxed().collect(Collectors.toList());

        return IntStream.range(0, length).mapToObj(i ->
            new Coordinates(shipFieldsXPositions.get(i), shipFieldsYPositions.get(i))
        ).collect(Collectors.toList());
    }
}