package backend;

import Sceny.StartupSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        StartupSceneController startupSceneController = new StartupSceneController();
        FXMLLoader fxmlLoader  = new FXMLLoader(getClass().getResource("../Sceny/StartupScene.fxml"));
        fxmlLoader.setController(startupSceneController);

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}