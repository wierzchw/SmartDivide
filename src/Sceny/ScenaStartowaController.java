package Sceny;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScenaStartowaController extends ScenyUzytkowe{

    public void switchToScenaOgolna(ActionEvent event) throws IOException {
        //  INTERFACE
        // import
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ScenaOgolna.fxml"));
        root = loader.load();
        ScenaOgolnaController scenaOgolnaController = loader.getController();

        // BACKEND
        // zaciągam rachunek ten co był ostatnio
        scenaOgolnaController.setRachunek(rachunek);

        // Label rachunku
        scenaOgolnaController.displayNazwaRachunku();

        // pierdoły
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
