package controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.Board;
import model.Coordinates;
import model.Field;
import model.Game;
import model.enums.FieldStatus;
import model.enums.GameStatus;
import org.fxmisc.easybind.EasyBind;


public class BoardController {
    private Game game;
    private Board playersBoard;




    public void setModel(Game game){
        this.game = game;
        this.playersBoard = game.getHuman().getBoard();
    }


    @FXML
    public void initialize() {
    }

    public void controllerInit(){
        playerName.textProperty().bind(game.getHuman().getNameProperty());

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
                rec.setOnMouseClicked(this::clickGrid);
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
        for(int row = 0; row < rowNum; row++){
            for(int col = 0; col < colNum; col++){
                Rectangle rec = new Rectangle();
                rec.setWidth(50);
                rec.setHeight(50);
                rec.setFill(Color.WHITE);
                rec.setOnMouseClicked(this::clickGrid);
                GridPane.setRowIndex(rec, row);
                GridPane.setColumnIndex(rec, col);
                Field field = this.game.getComputer().getBoard().getFieldOnPosition(new Coordinates(row, col));
                rec.fillProperty().bind(
                        EasyBind.map(field.fieldStatusProperty(), this::calculateFieldColor));
                computerBoard.add(rec, row, col);
            }
        }
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

    @FXML
    public void place1Ship(){
        this.game.setCurrentlyPlacingShipLength(1);
    }

    @FXML
    public void place2Ship(){
        this.game.setCurrentlyPlacingShipLength(2);
    }
    @FXML
    public void place3Ship(){
        this.game.setCurrentlyPlacingShipLength(3);
    }
    @FXML
    public void place4Ship(){
        this.game.setCurrentlyPlacingShipLength(4);
    }

    @FXML
    Label playerName;

    @FXML
    GridPane playerBoard;

    @FXML
    GridPane computerBoard;

    @FXML
    Button ShipPlace1;

    public void clickGrid(MouseEvent event) {

        Node clickedNode = event.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(clickedNode);
        Integer rowIndex = GridPane.getRowIndex(clickedNode);
        System.out.println("Mouse clicked cell: " + colIndex + " And: " + rowIndex);
        this.tryPlaceShip(colIndex, rowIndex);
//        this.game.getHuman().getBoard().setFieldStatusOnPosition(new Coordinates(colIndex,rowIndex), FieldStatus.FIELD_SHIP_ACTIVE);
    }

    private void tryPlaceShip(Integer col, Integer row) {
        if(this.game.getCurrentlyPlacingShipLength() > 0 && this.game.getCurrentState() == GameStatus.GAME_SHIP_PLACEMENT){
            if(this.playersBoard.addShip(new Coordinates(col, row), true, this.game.getCurrentlyPlacingShipLength()) == true){
                System.out.println("Dodano statek");
                this.game.setCurrentlyPlacingShipLength(0);
            }
        }
    }


}

