import controller.BoardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.BoardCreator;
import model.Config;
import model.Game;

import java.io.IOException;
import java.util.Map;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Game game = new Game();
            game.initialize();

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
            loader.setLocation(Main.class.getResource("view/mainView.fxml"));
            VBox rootLayout = loader.load();
            BoardController controller = loader.getController();
//            controller.setModel(game);
            controller.setModel(boardCreator);
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
