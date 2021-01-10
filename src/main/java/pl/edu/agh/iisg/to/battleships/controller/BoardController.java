package pl.edu.agh.iisg.to.battleships.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import pl.edu.agh.iisg.to.battleships.Main;
import pl.edu.agh.iisg.to.battleships.dao.HumanPlayerDao;
import pl.edu.agh.iisg.to.battleships.model.*;
import pl.edu.agh.iisg.to.battleships.model.ai.EasyAI;
import pl.edu.agh.iisg.to.battleships.model.ai.HardAI;
import pl.edu.agh.iisg.to.battleships.model.ai.MediumAI;
import pl.edu.agh.iisg.to.battleships.model.email.EmailSender;
import pl.edu.agh.iisg.to.battleships.model.enums.FieldStatus;
import pl.edu.agh.iisg.to.battleships.model.enums.GameStatus;

import java.util.*;
import java.util.stream.Collectors;


public class BoardController implements Game.Callback {
    private Stage stage;
    private Game game;
    private Board playersBoard;

    private Player humanPlayer;
    private BoardCreator boardCreator;

    private final IntegerProperty shipLength = new SimpleIntegerProperty(0);
    private final BooleanProperty isPlacingShipHorizontally = new SimpleBooleanProperty(true);
    private final Map<Coordinates, Rectangle> playersBoardRectangles = new LinkedHashMap<>();
    private final Map<Coordinates, Rectangle> computersBoardRectangles = new LinkedHashMap<>();


    public void setModel(BoardCreator boardCreator, Game game) {
        this.boardCreator = boardCreator;
        this.playersBoard = boardCreator.getBoard();
        this.game = game;
    }

    @FXML
    public void initialize(Stage stage, Player humanPlayer) {
        this.stage = stage;
        this.humanPlayer = humanPlayer;
        shipLength.setValue(4);
    }

    public void refreshAllBoards(){
        for(var el : playersBoardRectangles.entrySet()){
            Coordinates position = el.getKey();
            refreshField(position);
        }
        for(var el : computersBoardRectangles.entrySet()){
            refreshComputerField(el.getKey());
        }
    }

    private void refreshField(Coordinates position){
        FieldStatus newStatus = playersBoard.getFieldOnPosition(position).getFieldStatus();
        playersBoardRectangles.get(position).setFill(this.calculateFieldColor(newStatus));
    }

    private void refreshComputerField(Coordinates position){
        FieldStatus newStatus = game.getOpponentsBoard().getFieldOnPosition(position).getFieldStatus();
        computersBoardRectangles.get(position).setFill(this.calculateComputersFieldColor(newStatus));
    }

    public void controllerInit(){
//        playerName.textProperty().bind(game.getHuman().getNameProperty());

        int rowNum = this.playersBoard.getLimit().getY();
        int colNum = this.playersBoard.getLimit().getX();
        playerBoard.setHgap(0);
        playerBoard.setVgap(0);
        for(int row = 0; row < rowNum; row++){
            for(int col = 0; col < colNum; col++){
                Rectangle rec = new Rectangle();
                rec.setWidth(50);
                rec.setHeight(50);
                rec.setFill(Color.WHITE);
                rec.setStroke(Color.BLACK);
                rec.setOnMouseClicked(this::onPlayersBoardClick);
                rec.setOnMouseEntered(this::onPlayersBoardHover);
                rec.setOnMouseExited(this::onPlayersBoardHoverExit);
                GridPane.setRowIndex(rec, row);
                GridPane.setColumnIndex(rec, col);
//                Field field = this.playersBoard.getFieldOnPosition(new Coordinates(row, col));
//                rec.fillProperty().bind(
//                        EasyBind.map(field.fieldStatusProperty(), this::calculateFieldColor));
                this.playersBoardRectangles.put(new Coordinates(row,col), rec);
                playerBoard.add(rec, row, col);
            }
        }

        computerBoard.setHgap(0);
        computerBoard.setVgap(0);
        var computersBoard = game.getOpponentsBoard();
        for(int row = 0; row < rowNum; row++){
            for(int col = 0; col < colNum; col++){
                Rectangle rec = new Rectangle();
                rec.setWidth(50);
                rec.setHeight(50);
                rec.setFill(Color.WHITE);
                rec.setStroke(Color.BLACK);
                rec.setOnMouseClicked(this::onOpponentsBoardClick);
                rec.setOnMouseEntered(this::onOpponentsBoardHover);
                rec.setOnMouseExited(this::onOpponentsBoardHoverExit);
                GridPane.setRowIndex(rec, row);
                GridPane.setColumnIndex(rec, col);
//                Field field = computersBoard.getFieldOnPosition(new Coordinates(row, col));
//                rec.fillProperty().bind(
//                        EasyBind.map(field.fieldStatusProperty(), this::calculateComputersFieldColor));

                this.computersBoardRectangles.put(new Coordinates(row, col), rec);
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

        this.bindButtons();
        this.refreshAllBoards();
    }

    private void bindButtons(){
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
            case FIELD_EMPTY -> {
                return Color.LIGHTBLUE;
            }
            case FIELD_SHIP_ACTIVE -> {
                return Color.GREEN;     //TODO: Change to GREY; Debug only
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
        this.refreshAllBoards();
    }

    @FXML
    public void redo(){
        boardCreator.redo();
        this.refreshAllBoards();
    }

    @FXML
    Menu editMenu;

    @FXML
    public void startGame() {
        this.statusText.setText("W trakcie gry");
        this.clearSettingsPanel();
        this.editMenu.setDisable(true);

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
    Button randomize;
    @FXML
    Button undoBtn;
    @FXML
    Button redoBtn;
    @FXML
    Button rotateBtn;
    @FXML
    Label levelLabel;
    @FXML
    RadioButton easy;
    @FXML
    RadioButton medium;
    @FXML
    RadioButton hard;

    @FXML
    AnchorPane centerPanel;


    @FXML
    Label statusText;


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
                this.refreshAllBoards();
                this.onPlayersBoardHover(event);
            }
            else if(event.getButton() == MouseButton.MIDDLE){
                this.toggleOrientation();
                this.onPlayersBoardHoverExit(event);
                this.onPlayersBoardHover(event);
            }
            else {
                if(shipLength.get() > 0) {
                    if (boardCreator.placeShip(
                        coords,
                        shipLength.get(),
                        isPlacingShipHorizontally.get() ? Ship.Orientation.HORIZONTAL : Ship.Orientation.VERTICAL
                    )){
                        this.refreshAllBoards();
                    }
                    if(boardCreator.getLengthsOfShipsYetToBePlaced().stream().noneMatch(it -> it == shipLength.get())) {
                        if(boardCreator.getLengthsOfShipsYetToBePlaced().stream().anyMatch(it -> it == shipLength.get() - 1)){
                            shipLength.setValue(shipLength.get() - 1);
                        }
                        else if(!boardCreator.getLengthsOfShipsYetToBePlaced().isEmpty()){
                            shipLength.setValue(boardCreator.getLengthsOfShipsYetToBePlaced().get(0));
                        }
                        else{
                            shipLength.setValue(0);
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

    }

    private Coordinates getEventCoordinates(MouseEvent e){
        Node clickedNode = e.getPickResult().getIntersectedNode();
        Integer colIndex = GridPane.getColumnIndex(clickedNode);
        Integer rowIndex = GridPane.getRowIndex(clickedNode);
        return new Coordinates(colIndex, rowIndex);
    }

    public void onPlayersBoardHover(MouseEvent event) {
        if (this.game.getCurrentState() == GameStatus.NOT_STARTED && shipLength.get() > 0) {
            Coordinates fieldCoords = getEventCoordinates(event);
            Color color;
            if(this.boardCreator.canPlaceShip(fieldCoords,shipLength.get(),isPlacingShipHorizontally.get() ? Ship.Orientation.HORIZONTAL : Ship.Orientation.VERTICAL)){
                color = Color.GREEN;
            }
            else{
                color = Color.DARKRED;
            }
            List<Coordinates> coords = this.boardCreator.getFieldCoordsForShipAtPosition(fieldCoords,shipLength.get(),isPlacingShipHorizontally.get() ? Ship.Orientation.HORIZONTAL : Ship.Orientation.VERTICAL);
            for (Coordinates c : coords) {
                if(this.playersBoard.areCoordsInRange(c)) {
                    this.playersBoardRectangles.get(c).setFill(color);
                }
            }
        }
        if(this.game.getCurrentState() == GameStatus.IN_PROGRESS || (game.getCurrentState() == GameStatus.NOT_STARTED && shipLength.get() == 0)) {
            Coordinates fieldCoords = getEventCoordinates(event);
            Color currentColor = (Color) this.playersBoardRectangles.get(fieldCoords).getFill();
            this.playersBoardRectangles.get(fieldCoords).setFill(currentColor.brighter());
        }
    }

    public void onPlayersBoardHoverExit(MouseEvent event) {
        if (this.game.getCurrentState() == GameStatus.NOT_STARTED ){
            this.refreshAllBoards();
        }
        else if(this.game.getCurrentState() == GameStatus.IN_PROGRESS) {
            this.refreshAllBoards();
        }
    }

    public void onOpponentsBoardHover(MouseEvent event) {
        if (this.game.getCurrentState() == GameStatus.NOT_STARTED) {
            return;
        }
        if(this.game.getCurrentState() == GameStatus.IN_PROGRESS) {
            this.stage.getScene().setCursor(Cursor.HAND);
            Coordinates fieldCoords = getEventCoordinates(event);
            Color currentColor = (Color) this.computersBoardRectangles.get(fieldCoords).getFill();
            this.computersBoardRectangles.get(fieldCoords).setFill(currentColor.brighter());
        }
    }

    public void onOpponentsBoardHoverExit(MouseEvent event) {
        this.onPlayersBoardHoverExit(event);
        this.stage.getScene().setCursor(Cursor.DEFAULT);
        this.stage.getScene().setCursor(Cursor.DEFAULT);
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


    public Player getHumanPlayer() {
        return humanPlayer;
    }

    @Override
    public void onGameEnded(boolean hasPlayerWon) {
        this.statusText.setText("Zakonczono");

        HashMap<Player,Integer> oldRatings = getCurrentPlayersRatings();

        int oldPlayerRating = this.humanPlayer.getRating();
        Integer ratingChange = humanPlayer.updateRating(this.game.getDifficultyLevel(), hasPlayerWon);

        String message = hasPlayerWon ? "Gratulacje "+this.getHumanPlayer().getName()+"! Wygrana!" :
                "Niestety, tym razem komputer okazal sie byc lepszy od Ciebie, "+this.getHumanPlayer().getName()+".";
        message += ("\nRanking: "+this.humanPlayer.getRating());
        message += ratingChange >= 0 ? (" (+"+ratingChange+")") : (" ("+ratingChange+")");
        this.game.updatePlayerInDb();

        System.out.println("Wynik: " + hasPlayerWon);
        Main.showFinishedDialog(new Stage(), this.getHumanPlayer(), message, this.stage);

        sendEmailsToLosers( oldRatings, oldPlayerRating, oldPlayerRating + ratingChange, this.humanPlayer.getName() );
    }

    private HashMap<Player, Integer> getCurrentPlayersRatings(){
        List<Player> players = new HumanPlayerDao().getPlayersWithRating();
        HashMap<Player, Integer> ratings = new HashMap<>();
        for(Player player : players) if(player.getRating() != null) ratings.put(player, player.getRating());
        return ratings;
    }


    private void sendEmailsToLosers(HashMap<Player,Integer> oldRatings, int oldRating, int newRating, String playingPlayerName){
        if(newRating <= oldRating) return;  // As player didn't get any points, thus noone could be dethroned.
        Optional<Integer> maxOldRating = oldRatings.values().stream().max(Comparator.naturalOrder());
        if(maxOldRating.isEmpty() || maxOldRating.get().equals(Config.DEFAULT_RATING)) return;
        Integer prevMaxRating = maxOldRating.get();

        if(newRating > prevMaxRating && oldRating <= prevMaxRating ){

            List<Player> previouslyFirst = oldRatings.keySet().stream().filter(e -> e.getRating().equals(prevMaxRating)).collect(Collectors.toList());
            for(Player dethronedPlayer : previouslyFirst){
                if(dethronedPlayer.getName().equals(playingPlayerName)) continue;
                System.out.println("Debug: Dethroned player " + dethronedPlayer.getName() + " , rating: " + dethronedPlayer.getRating() + " is more than " + oldRating + " and less than " + newRating);
                String message = "You were overrun in ranking by " + playingPlayerName + ". <br> Your rating: " + dethronedPlayer.getRating() + "<br> " + playingPlayerName + " rating: " + newRating;
                EmailSender.sendEmail(dethronedPlayer.getMail(), "Battleships App - You've been defeated!", EmailSender.createTemplateHtmlEmail(message, dethronedPlayer.getName()));
            }
        }

//        oldRatings.entrySet()
//                .removeIf(
//                    entry -> (entry.getValue() < oldRating || entry.getValue() >= newRating || entry.getValue().equals(Config.DEFAULT_RATING))
//                );


    }


    @Override
    public void onError(String errorMessage) {
        System.out.println("Błąd: " + errorMessage);
    }

    @Override
    public void onShootMade() {
        this.refreshAllBoards();
    }

    private void clearSettingsPanel(){
        this.centerPanel.setVisible(false);
//        this.shipPlace2.setVisible(false);
//        this.shipPlace3.setVisible(false);
//        this.shipPlace4.setVisible(false);
//        this.startGame.setVisible(false);
//        this.redoBtn.setVisible(false);
//        this.undoBtn.setVisible(false);
//        this.undoBtn.setVisible(false);
    }

    @FXML
    private void setEasy(){
        this.game.setAI(new EasyAI());
    }
    @FXML
    private void setMedium(){
        this.game.setAI(new MediumAI());
    }
    @FXML
    private void setHard(){
        this.game.setAI(new HardAI());
    }

    @FXML
    private void setPlayersShipsRandom(){
//        this.playersBoard = BoardInitializer.getBoardWithRandomlyPlacedShips(this.playersBoard.getLimit().getX(), this.game.getShipCounts());
        this.boardCreator = BoardInitializer.getBoardCreatorWithRandomlyPlacedShips(this.playersBoard.getLimit().getX(), this.game.getShipCounts());
        this.playersBoard = boardCreator.getBoard();
        this.bindButtons();
        this.shipLength.setValue(0);
        this.refreshAllBoards();
//        List<Ship> ships = newBoardSetting.getShips();
//        this.boardCreator.getBoard().getShips().forEach(ship -> this.boardCreator.getBoard().removeShip(ship));
//        ships.forEach(ship -> this.boardCreator.getBoard().addShip(ship));
    }

}

