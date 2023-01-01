package backend;

import Sceny.ScenaStartowaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


public class Main extends Application {

    public static ArrayList<Rachunek> rachuneks = new ArrayList<>();
    public static ArrayList<Osoba> osobas = new ArrayList<>();


    public static void main(String[] args) {
        // place holder
        // usunięcie da null pointer exception
        rachuneks.add(new Rachunek("TestRachunek"));
        osobas.add(new Osoba("TestOsoba"));


        launch(args);
    }

    public void start(Stage stage) {
    //inicjacja apki, wywołanie pierwszego ekranu startowego
        Parent root = null;
        try {

            //inicjacja ekranu startowego
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Sceny/ScenaStartowa.fxml"));
            root = loader.load();
            ScenaStartowaController scenaStartowaController = loader.getController();

            // gdy zaczytuje pierwszy raz ekran rachunku nie wiem jeszcze jaki wyświetlić więc domyślnie wyświetlać się
            // będzie pierwszy z listy [TO DO: co jeśli lista będzie pusta?]
            scenaStartowaController.setRachunek(Main.rachuneks.get(0));

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}