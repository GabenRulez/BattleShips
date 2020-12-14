package pl.edu.agh.iisg.to.battleships;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pl.edu.agh.iisg.to.battleships.controller.BoardController;
import pl.edu.agh.iisg.to.battleships.controller.LoginController;
import pl.edu.agh.iisg.to.battleships.controller.RegisterController;
import pl.edu.agh.iisg.to.battleships.model.BoardCreator;
import pl.edu.agh.iisg.to.battleships.model.Config;
import pl.edu.agh.iisg.to.battleships.model.Game;
import pl.edu.agh.iisg.to.battleships.model.HumanPlayer;

import java.io.IOException;
import java.util.Map;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

//        showBoard(primaryStage);
        showLoginDialog(primaryStage);

    }

    public static void showBoard(Stage primaryStage, HumanPlayer player){
        try {
//            SessionService.openSession();
//            HumanPlayerDao playerDao = new HumanPlayerDao();
//            GameDao gameDao = new GameDao();
//            Game testGame = new Game(null);
//            playerDao.create("Test4", "a6@a.com", "aaa");
//
//            gameDao.saveToDb(testGame);
//            SessionService.closeSession();

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.exit(0);
                }
            });

            var shipCounts = Map.ofEntries(
                    Map.entry(1, 4),
                    Map.entry(2, 3),
                    Map.entry(3, 2),
                    Map.entry(4, 1)
            );
            var boardCreator = new BoardCreator(Config.BOARD_SIZE.getX(), shipCounts);
//

            // load layout from FXML file
            var loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/mainView.fxml"));
            VBox rootLayout = loader.load();
            BoardController controller = loader.getController();
//            controller.setModel(game);
            controller.initialize(primaryStage, player);
            controller.setModel(boardCreator);
            controller.controllerInit();
            // add layout to a scene and show them all
//            configureStage(primaryStage, rootLayout);
            var scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Battleships");
            primaryStage.setResizable(false);
            primaryStage.show();
            Game game = new Game(null);
            game.start();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showLoginDialog(Stage primaryStage){
        try {
            var loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/loginView.fxml"));
            AnchorPane rootLayout = loader.load();
            LoginController controller = loader.getController();
            controller.init(primaryStage);
            var scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Logowanie");
            primaryStage.setResizable(false);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showRegisterDialog(Stage primaryStage){
        try {
            // load layout from FXML file
            var loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/registerView.fxml"));
            AnchorPane rootLayout = loader.load();
            RegisterController controller = loader.getController();
            controller.init(primaryStage);
            var scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Rejestracja");
            primaryStage.setResizable(false);
            primaryStage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
