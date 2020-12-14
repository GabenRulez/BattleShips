package pl.edu.agh.iisg.to.battleships.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.edu.agh.iisg.to.battleships.Main;
import pl.edu.agh.iisg.to.battleships.dao.HumanPlayerDao;
import pl.edu.agh.iisg.to.battleships.model.HumanPlayer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class RegisterController {

    private Stage stage;

    @FXML
    public Button returnSmallButton;
    @FXML
    public Button returnButton;
    @FXML
    public Button registerButton;

    @FXML
    public TextField username;
    @FXML
    public Label usernameLabel;

    @FXML
    public PasswordField password;
    @FXML
    public Label passwordLabel;

    @FXML
    public TextField mail;
    @FXML
    public Label mailLabel;

    @FXML
    public Label registerSuccessful;

    @FXML
    public Label message;


    public void init(Stage stage){
        this.stage = stage;

    }


    @FXML
    public boolean register(String username, String mail, String password){
        Optional<HumanPlayer> player = new HumanPlayerDao().create(username, mail, password);
        if(player.isPresent()) return true;
        else{
            return false;
        }
    }

    @FXML
    public void registerClickHandle(ActionEvent event) {
        if(this.mail.getText().equals("") || this.password.getText().equals("") || this.username.getText().equals("") ){
            this.message.setText("Wszystkie pola musza byc wypelnione!");
            return;
        }

        if(this.register(
                this.username.getText(),
                this.mail.getText(),
                LoginController.encryptPassword(this.password.getText()))){
            this.registerSuccessful();
        }

        this.message.setText("Nie udalo sie zarejestrowac. Sprobuj inny adres e-mail");
   }



    @FXML
    public void returnClickHandle(){
        Main.showLoginDialog(this.stage);
    }


    private void registerSuccessful(){
        this.message.setVisible(false);
        this.registerButton.setVisible(false);
        this.username.setVisible(false);
        this.usernameLabel.setVisible(false);
        this.mail.setVisible(false);
        this.mailLabel.setVisible(false);
        this.password.setVisible(false);
        this.passwordLabel.setVisible(false);
        this.returnSmallButton.setVisible(false);

        this.registerSuccessful.setVisible(true);
        this.returnButton.setVisible(true);
    }
}
