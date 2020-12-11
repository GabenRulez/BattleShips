package pl.edu.agh.iisg.to.battleships.model;

import javafx.collections.ObservableList;

public class RemoveShipCommand implements BoardCreatorCommand {
    private final Board board;
    private final ObservableList<Integer> lengthsOfShipsYetToBePlaced;
    private final Ship ship;

    public RemoveShipCommand(Board board, ObservableList<Integer> lengthsOfShipsYetToBePlaced, Ship ship) {
        this.board = board;
        this.lengthsOfShipsYetToBePlaced = lengthsOfShipsYetToBePlaced;
        this.ship = ship;
    }

    @Override
    public void execute() {
        board.removeShip(ship);
        lengthsOfShipsYetToBePlaced.add(ship.getLength());
    }

    @Override
    public void undo() {
        board.addShip(ship);

        var indexToRemoveAt = lengthsOfShipsYetToBePlaced.indexOf(ship.getLength());
        if(indexToRemoveAt >= 0) {
            lengthsOfShipsYetToBePlaced.remove(indexToRemoveAt);
        }
    }
}
