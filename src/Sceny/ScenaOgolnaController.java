package Sceny;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ScenaOgolnaController extends ScenyUzytkowe{
    @FXML
    Label nazwaRachunku;

    public void displayNazwaRachunku() {
        nazwaRachunku.setText(rachunek.getNazwa());
    }

    public void switchToScenaDokladna(ActionEvent event) throws IOException {
        // import
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ScenaDokladna.fxml"));
        root = loader.load();
        ScenaDokladnaController scenaDokladnaController = loader.getController();
        scenaDokladnaController.setRachunek(rachunek);

        // display
        scenaDokladnaController.displayNazwaRachunku();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
