package pl.edu.agh.iisg.to.battleships.controller;

import javafx.application.Platform;
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
import pl.edu.agh.iisg.to.battleships.model.*;

import java.util.List;
import java.util.Optional;

public class LoginController {

    private Stage stage;

    @FXML
    public Button loginButton;

    @FXML
    public TextField login;

    @FXML
    public PasswordField password;

    @FXML
    public Label message;


    public void init(Stage stage){
        this.stage = stage;

    }


    @FXML
    public void register(){
        Main.showRegisterDialog(this.stage);
    }

    @FXML
    public void loginClickHandle(ActionEvent event) {
        new Thread(() -> {
            Platform.runLater(() -> {
                if (this.login.getText().equals("") || this.password.getText().equals("")) {
                    this.message.setText("Podaj adres e-mail i haslo!");
                    return;
                }
                Optional<Player> player = new HumanPlayerDao().findByMail(this.login.getText());

                if (player.isEmpty() || !this.isAuthenticated(player.get(), this.password.getText())) {
                    this.message.setText("Nieprawidlowe haslo lub adres e-mail!");
                    return;
                }
                this.login(player.get());
            });
        }).start();



    }

    private boolean isAuthenticated(Player player, String password){
        return comparePassword(password, player.getPassword());
    }


    private void login(Player player){
        this.stage.close();
        Main.showBoard(new Stage(), player);
    }

    public static String encryptPassword(String plaintext){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(plaintext);
    }

    public static boolean comparePassword(String plaintext, String databasePassword){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(plaintext, databasePassword);
    }

}
