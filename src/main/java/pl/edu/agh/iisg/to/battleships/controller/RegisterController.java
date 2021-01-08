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
import pl.edu.agh.iisg.to.battleships.model.Player;
import pl.edu.agh.iisg.to.battleships.model.email.EmailSender;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Optional<Player> player = new HumanPlayerDao().create(username, mail, password);
        return player.isPresent();
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @FXML
    public void registerClickHandle(ActionEvent event) {
        if(!validateEmail(this.mail.getText())){
            this.message.setText("Niepoprawna postac adresu e-mail!");
            return;
        }

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


    @FXML
    public void sendRegistrationConfirmation(){
        EmailSender.sendEmail(this.mail.getText(), "Battleships App Registration", EmailSender.createTemplateHtmlEmail("Your registration was succesful.", this.username.getText()));
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

        this.sendRegistrationConfirmation();
    }
}
