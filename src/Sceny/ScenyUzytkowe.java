package Sceny;

import backend.Rachunek;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class ScenyUzytkowe {

    protected Stage stage;
    protected Scene scene;
    protected Parent root;
    protected Rachunek rachunek;


    public void setRachunek(Rachunek rachunek) {
        this.rachunek = rachunek;
    }

}
