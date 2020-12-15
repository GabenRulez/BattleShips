package pl.edu.agh.iisg.to.battleships;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.edu.agh.iisg.to.battleships.controller.BoardController;
import pl.edu.agh.iisg.to.battleships.model.BoardCreator;
import pl.edu.agh.iisg.to.battleships.model.Config;
import pl.edu.agh.iisg.to.battleships.model.Game;

import java.io.IOException;
import java.util.Map;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
//            SessionService.openSession();
//            HumanPlayerDao playerDao = new HumanPlayerDao();
//            GameDao gameDao = new GameDao();
//            Game testGame = new Game(null);
//            playerDao.create("Test4", "a6@a.com", "aaa");
//
//            gameDao.saveToDb(testGame);
//            SessionService.closeSession();

//            Game game = new Game(null);
//            game.start();

            var shipCounts = Map.ofEntries(
                Map.entry(1, 2),
                Map.entry(2, 2),
                Map.entry(3, 1),
                Map.entry(4, 1)
            );
            var boardCreator = new BoardCreator(Config.BOARD_SIZE.getX(), shipCounts);
            var game = new Game(null, Config.BOARD_SIZE.getX(), shipCounts);

            // load layout from FXML file
            var loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/mainView.fxml"));
            VBox rootLayout = loader.load();
            BoardController controller = loader.getController();
            controller.setModel(boardCreator, game);
            controller.controllerInit();
            // add layout to a scene and show them all
            configureStage(primaryStage, rootLayout);
            primaryStage.setResizable(false);
            primaryStage.show();


        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }
    }

    private void configureStage(Stage primaryStage, VBox rootLayout) {
        var scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Battleships");
//        primaryStage.minWidthProperty().bind(rootLayout.minWidthProperty());
//        primaryStage.minHeightProperty().bind(rootLayout.minHeightProperty());
    }
}
