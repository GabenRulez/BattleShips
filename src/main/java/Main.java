import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pl.edu.agh.to.weebs.battleships.model.Game;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Game game = new Game();
            Thread gameThread = new Thread(){
                public void run(){
                    game.start();
                }
            };
            gameThread.start();

            // load layout from FXML file
            var loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/mainView.fxml"));
            BorderPane rootLayout = loader.load();
            // add layout to a scene and show them all
            configureStage(primaryStage, rootLayout);
            primaryStage.show();




        } catch (IOException e) {
            // don't do this in common apps
            e.printStackTrace();
        }
    }

    private void configureStage(Stage primaryStage, BorderPane rootLayout) {
        var scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Battleships");
        primaryStage.minWidthProperty().bind(rootLayout.minWidthProperty());
        primaryStage.minHeightProperty().bind(rootLayout.minHeightProperty());
    }
}
