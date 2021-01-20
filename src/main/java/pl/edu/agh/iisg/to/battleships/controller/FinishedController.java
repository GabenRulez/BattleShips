package pl.edu.agh.iisg.to.battleships.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import pl.edu.agh.iisg.to.battleships.Main;
import pl.edu.agh.iisg.to.battleships.dao.HumanPlayerDao;
import pl.edu.agh.iisg.to.battleships.model.Config;
import pl.edu.agh.iisg.to.battleships.model.Player;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FinishedController {

    @FXML
    public Button resetButton;
    private Player player;
    private Stage stage;
    private Stage gameStage;

    @FXML
    public Label message;


    public void init(Stage stage, Player player, String message, Stage gameStage){
        this.stage = stage;
        this.player = player;
        this.message.setText(message);
        this.gameStage = gameStage;
        this.loadRatingList();
        this.resetButton.setVisible(this.player.isAdmin());
    }

    @FXML
    ListView<String> ratingList;

    private void loadRatingList(){
        List<Player> players = new HumanPlayerDao().getPlayersWithRating();
        List<String> playersStringList =
                players.stream()
                .filter(p -> p.getRating() != null)
                .sorted(Comparator.comparingInt(Player::getRating).reversed())
                .map(p -> p.getRating()+" - "+p.getName())
                .collect(Collectors.toList());
        int place = 1;
        int currentPlayerIdx = -1;
        for(int i=0; i< playersStringList.size(); i++){
            int firstRatingChar = String.valueOf(i-1).length()+2;
            if (i == 0 || !playersStringList.get(i).substring(0, 4)
                    .equals(playersStringList.get(i-1).substring(firstRatingChar, firstRatingChar+4))){
                place = i+1;
            }
            playersStringList.set(i, place + ". " + playersStringList.get(i));
            if (playersStringList.get(i).contains(" - "+this.player.getName())){
                currentPlayerIdx = i;
            }
        }
        this.ratingList.getItems().clear();
        this.ratingList.getItems().addAll(playersStringList);
        if(currentPlayerIdx != -1) {
            this.ratingList.getSelectionModel().select(currentPlayerIdx);
        }
    }

    @FXML
    public void restartClickHandle(ActionEvent event) {
        this.stage.close();
        this.gameStage.close();
        Main.showBoard(this.gameStage, this.player);
    }

    @FXML
    public void resetRatings(ActionEvent event){
        HumanPlayerDao playerDao = new HumanPlayerDao();
        List<Player> players = playerDao.getPlayersWithRating();
        for(Player player : players){
            player.setRating(Config.DEFAULT_RATING);
            playerDao.updatePlayer(player);
        }
        this.player.setRating(Config.DEFAULT_RATING);
        playerDao.updatePlayer(this.player);
        this.loadRatingList();

    }

    @FXML
    public void exit(ActionEvent event) {
        System.exit(0);
    }
}
