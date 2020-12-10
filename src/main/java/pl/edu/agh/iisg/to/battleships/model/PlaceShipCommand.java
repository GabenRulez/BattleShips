package pl.edu.agh.iisg.to.battleships.model;

import javafx.collections.ObservableList;

public class PlaceShipCommand implements BoardCreatorCommand {
    private final Board board;
    private final ObservableList<Integer> lengthsOfShipsYetToBePlaced;
    private final Ship ship;

    public PlaceShipCommand(Board board, ObservableList<Integer> lengthsOfShipsYetToBePlaced, Ship ship) {
        this.board = board;
        this.lengthsOfShipsYetToBePlaced = lengthsOfShipsYetToBePlaced;
        this.ship = ship;
    }

    @Override
    public void execute() {
        board.addShip(ship);

        var indexToRemoveAt = lengthsOfShipsYetToBePlaced.indexOf(ship.getLength());
        if(indexToRemoveAt >= 0) {
            lengthsOfShipsYetToBePlaced.remove(indexToRemoveAt);
        }
    }

    @Override
    public void undo() {
        board.removeShip(ship);
        lengthsOfShipsYetToBePlaced.add(ship.getLength());
    }
}
