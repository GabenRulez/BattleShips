package pl.edu.agh.iisg.to.battleships.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import pl.edu.agh.iisg.to.battleships.Main;
import org.fxmisc.easybind.EasyBind;
import pl.edu.agh.iisg.to.battleships.model.*;
import pl.edu.agh.iisg.to.battleships.model.enums.FieldStatus;
import pl.edu.agh.iisg.to.battleships.model.enums.GameStatus;


public class BoardController implements Game.Callback {
    private Stage stage;
    private Game game;
    private Board playersBoard;

    private HumanPlayer humanPlayer;
    private BoardCreator boardCreator;

    private final IntegerProperty shipLength = new SimpleIntegerProperty(0);
    private final BooleanProperty isPlacingShipHorizontally = new SimpleBooleanProperty(true);

//    public void setModel(Game game){
//        this.game = game;
//        this.playersBoard = game.getHuman().getBoard();
//    }

    public void setModel(BoardCreator boardCreator, Game game) {
        this.boardCreator = boardCreator;
        this.playersBoard = boardCreator.getBoard();
        this.game = game;
    }

    @FXML
    public void initialize(Stage stage, HumanPlayer humanPlayer) {
        this.stage = stage;
        this.humanPlayer = humanPlayer;
    }

    public void controllerInit(){
//        playerName.textProperty().bind(game.getHuman().getNameProperty());

        int rowNum = this.playersBoard.getLimit().getY();
        int colNum = this.playersBoard.getLimit().getX();
        playerBoard.setHgap(3);
        playerBoard.setVgap(3);
        for(int row = 0; row < rowNum; row++){
            for(int col = 0; col < colNum; col++){
                Rectangle rec = new Rectangle();
                rec.setWidth(50);
                rec.setHeight(50);
                rec.setFill(Color.WHITE);
                rec.setOnMouseClicked(this::onPlayersBoardClick);
                GridPane.setRowIndex(rec, row);
                GridPane.setColumnIndex(rec, col);
                Field field = this.playersBoard.getFieldOnPosition(new Coordinates(row, col));
                rec.fillProperty().bind(
                        EasyBind.map(field.fieldStatusProperty(), this::calculateFieldColor));
                playerBoard.add(rec, row, col);
            }
        }

        computerBoard.setHgap(3);
        computerBoard.setVgap(3);
        var computersBoard = game.getOpponentsBoard();
        for(int row = 0; row < rowNum; row++){
            for(int col = 0; col < colNum; col++){
                Rectangle rec = new Rectangle();
                rec.setWidth(50);
                rec.setHeight(50);
                rec.setFill(Color.WHITE);
                rec.setOnMouseClicked(this::onOpponentsBoardClick);
                GridPane.setRowIndex(rec, row);
                GridPane.setColumnIndex(rec, col);
                Field field = computersBoard.getFieldOnPosition(new Coordinates(row, col));
                rec.fillProperty().bind(
                        EasyBind.map(field.fieldStatusProperty(), this::calculateComputersFieldColor));
                computerBoard.add(rec, row, col);
            }
        }

        instruction.textProperty().bind(Bindings.createStringBinding(() -> {
            if(shipLength.get() <= 0) {
                return "Ustaw statki na planszy";
            }
            return String.format(
                    "Ustawiam %d-masztowiec %s",
                    shipLength.get(), isPlacingShipHorizontally.get() ? "poziomo" : "pionowo"
            );
        }, shipLength, isPlacingShipHorizontally));

        setupShipButtonEnabled(shipPlace1, 1);
        setupShipButtonEnabled(shipPlace2, 2);
        setupShipButtonEnabled(shipPlace3, 3);
        setupShipButtonEnabled(shipPlace4, 4);
        startGame.disableProperty().bind(Bindings.not(boardCreator.getCreationProcessFinishedProperty()));
        undoBtn.disableProperty().bind(Bindings.not(boardCreator.getUndoEnabledProperty()));
        redoBtn.disableProperty().bind(Bindings.not(boardCreator.getRedoEnabledProperty()));
    }

    private Paint calculateFieldColor(FieldStatus fieldStatus){
        switch (fieldStatus) {
            case FIELD_EMPTY -> {
                return Color.LIGHTBLUE;
            }
            case FIELD_EMPTY_BLOCKED -> {
                return Color.GREY;
            }
            case FIELD_SHIP_ACTIVE -> {
                return Color.GREEN;
            }
            case FIELD_SHIP_HIT -> {
                return Color.DARKRED;
            }
            default -> {
                return Color.WHITE;
            }
        }
    }

    private Paint calculateComputersFieldColor(FieldStatus fieldStatus) {
        switch (fieldStatus) {
            case FIELD_EMPTY, FIELD_SHIP_ACTIVE -> {
                return Color.LIGHTBLUE;
            }
            case FIELD_EMPTY_BLOCKED -> {
                return Color.GREY;
            }
            case FIELD_SHIP_HIT -> {
                return Color.DARKRED;
            }
            default -> {
                return Color.WHITE;
            }
        }
    }

    @FXML
    public void place1Ship(){
        shipLength.setValue(1);
    }

    @FXML
    public void place2Ship(){
        shipLength.setValue(2);
    }
    @FXML
    public void place3Ship(){
        shipLength.setValue(3);
    }
    @FXML
    public void place4Ship(){
        shipLength.setValue(4);
    }

    @FXML
    public void toggleOrientation() {
        isPlacingShipHorizontally.setValue(!isPlacingShipHorizontally.get());
    }

    @FXML
    public void undo(){
        boardCreator.undo();
    }

    @FXML
    public void redo(){
        boardCreator.redo();
    }

    @FXML
    public void startGame() {
        game.start(boardCreator.getBoard());
        game.setCallback(this);
    }

    @FXML
    Label playerName;

    @FXML
    Label instruction;

    @FXML
    GridPane playerBoard;

    @FXML
    GridPane computerBoard;

    @FXML
    Button shipPlace1;
    @FXML
    Button shipPlace2;
    @FXML
    Button shipPlace3;
    @FXML
    Button shipPlace4;
    @FXML
    Button startGame;
    @FXML
    Button undoBtn;
    @FXML
    Button redoBtn;

    public void menuNewGame(){
        this.stage.close();
        Main.showBoard(this.stage, this.getHumanPlayer());
    }

    public void menuLogout(){
        this.stage.close();
        Main.showLoginDialog(new Stage());
    }

    public void menuClose(){
        System.exit(0);
    }

    public void onPlayersBoardClick(MouseEvent event) {
        if(this.game.getCurrentState() != GameStatus.NOT_STARTED) return;

        Node clickedNode = event.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(clickedNode);
        Integer rowIndex = GridPane.getRowIndex(clickedNode);
        System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);

        var coords = new Coordinates(colIndex, rowIndex);
        try {
            if(event.getButton() == MouseButton.SECONDARY) {
                boardCreator.removeShip(coords);
            } else {
                if(shipLength.get() > 0) {
                    boardCreator.placeShip(
                        coords,
                        shipLength.get(),
                        isPlacingShipHorizontally.get() ? Ship.Orientation.HORIZONTAL : Ship.Orientation.VERTICAL
                    );
                    if(boardCreator.getLengthsOfShipsYetToBePlaced().stream().noneMatch(it -> it == shipLength.get())) {
                        shipLength.setValue(0);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void onOpponentsBoardClick(MouseEvent event) {
        if(this.game.getCurrentState() != GameStatus.IN_PROGRESS) return;

        Node clickedNode = event.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(clickedNode);
        Integer rowIndex = GridPane.getRowIndex(clickedNode);
        System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);

        var coords = new Coordinates(colIndex, rowIndex);
        try {
            this.game.shoot(coords);
        } catch (Exception ignored) {}
    }

    private void setupShipButtonEnabled(Button button, int shipLength) {
        button.disableProperty().bind(Bindings.createBooleanBinding(
            () -> boardCreator.getLengthsOfShipsYetToBePlaced().stream().noneMatch(it -> it == shipLength),
            boardCreator.getLengthsOfShipsYetToBePlaced()
        ));
    }


    public HumanPlayer getHumanPlayer() {
        return humanPlayer;
    }

    @Override
    public void onGameEnded(boolean hasPlayerWon) {
        System.out.println("Wynik: " + hasPlayerWon);
    }

    @Override
    public void onError(String errorMessage) {
        System.out.println("Błąd: " + errorMessage);
    }
}

