package backend;

import Sceny.StartupSceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App extends Application {

    Serialize reader;
    Serialize writer;
    Holder holder;

    @Override
    public void start(Stage stage) throws Exception {
        Path path = Paths.get("savefile.txt");
        StartupSceneController startupSceneController;

        if (Files.exists(path)) {
            reader = new Serialize("savefile.txt");
            holder = new Holder(reader.getBills(), reader.getMembers());

        } else {
            holder = new Holder();

        }

        startupSceneController = new StartupSceneController(holder);
        FXMLLoader fxmlLoader  = new FXMLLoader(getClass().getResource("../Sceny/StartupScene.fxml"));
        fxmlLoader.setController(startupSceneController);

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                writer = new Serialize(holder.getBills(), holder.getMembers());
                File saveFile = new File("savefile.txt");

                try {
                    saveFile.createNewFile();
                    writer.saveState(saveFile.getAbsolutePath());
                    Platform.exit();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
