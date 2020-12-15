package pl.edu.agh.iisg.to.battleships.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.edu.agh.iisg.to.battleships.Main;
import pl.edu.agh.iisg.to.battleships.dao.HumanPlayerDao;
import pl.edu.agh.iisg.to.battleships.model.Player;

import java.util.Optional;

public class FinishedController {

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
    }

    @FXML
    public void restartClickHandle(ActionEvent event) {
        this.stage.close();
        this.gameStage.close();
        Main.showBoard(this.gameStage, this.player);
    }

    @FXML
    public void exit(ActionEvent event) {
        System.exit(0);
    }
}
