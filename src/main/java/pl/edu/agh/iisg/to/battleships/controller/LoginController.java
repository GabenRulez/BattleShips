package pl.edu.agh.iisg.to.battleships.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pl.edu.agh.iisg.to.battleships.Main;
import pl.edu.agh.iisg.to.battleships.dao.HumanPlayerDao;
import pl.edu.agh.iisg.to.battleships.model.Player;

import java.util.Optional;

public class LoginController {

    private Stage stage;

    @FXML
    public ProgressIndicator progress;

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
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Logs user in, if the user is registered in the database." /*"Loguje uzytkownika z podanym adresem e-mail oraz haslem jesli uzytkownik istnieje w bazie"*/);
        loginButton.setTooltip(tooltip);
    }

    @FXML
    public void register(){
        Main.showRegisterDialog(this.stage);
    }

    @FXML
    public void loginClickHandle(ActionEvent event) {
        this.message.setText("");
        if (this.login.getText().equals("") || this.password.getText().equals("")) {
            this.message.setText("Specify email address and the password." /*"Podaj adres e-mail i haslo!"*/);
        } else {
            progress.setVisible(true);
            new Thread(() -> {
                var dao = new HumanPlayerDao();
                dao.create("admin", "admin@admin.com", LoginController.encryptPassword("admin"), true);
                Optional<Player> player = dao.findByMail(this.login.getText());

                Platform.runLater(() -> {
                    if (player.isEmpty() || !this.isAuthenticated(player.get(), this.password.getText())) {
                        this.message.setText("Incorrect email address or password." /*"Nieprawidlowe haslo lub adres e-mail!"*/);
                    } else {
                        this.login(player.get());
                    }
                    progress.setVisible(false);
                });
            }).start();
        }
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
