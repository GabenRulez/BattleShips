import javafx.application.Application;
import controller.BoardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.BoardCreator;
import model.Game;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Game game = new Game();
            game.initialize();
//

            // load layout from FXML file
            var loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/mainView.fxml"));
            VBox rootLayout = loader.load();
            BoardController controller = loader.getController();
            controller.setModel(game);
            controller.controllerInit();
            // add layout to a scene and show them all
            configureStage(primaryStage, rootLayout);
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
