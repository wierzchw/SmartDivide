package Sceny;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class ScenaDokladnaController extends ScenyUzytkowe{
    @FXML
    Label nazwaRachunku;

    public void displayNazwaRachunku() {
        nazwaRachunku.setText(rachunek.getNazwa());
    }

    public void switchToScenaOgolna(ActionEvent event) throws IOException {
        // import
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ScenaOgolna.fxml"));
        root = loader.load();
        ScenaOgolnaController scenaOgolnaController = loader.getController();
        scenaOgolnaController.setRachunek(rachunek);

        // display
        scenaOgolnaController.displayNazwaRachunku();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
