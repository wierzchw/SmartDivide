package Sceny;

import backend.Bill;
import backend.Holder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class StartupSceneController implements Initializable {

    @FXML
    private Button addBillButton;

    @FXML
    private Button openBillButton;

    @FXML
    private ListView<String> listView;

    @FXML
    private TextField newBillName;



    Holder holder;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private Bill selectedBill;

    public StartupSceneController(Holder holder) {
        this.holder = holder;
    }

    public StartupSceneController() {
        this.holder = new Holder();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Bill> bills = holder.getBills();

        for (Bill bill : bills) {
            listView.getItems().add(bill.getTitle());
        }

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String selectedBillName = listView.getSelectionModel().getSelectedItem();
                for (Bill bill : bills) {
                    if(Objects.equals(bill.getTitle(), selectedBillName)){
                        selectedBill = bill;
                        break;
                    }
                }
            }
        });
    }

    public void controllerAddBill() {
        ArrayList<Bill> bills = holder.getBills();
        Bill newBill = new Bill(newBillName.getText());

        if(!holder.checkBillExistence(newBill.getTitle())) {
            holder.addBill(newBill);
            listView.getItems().add((bills.get(bills.size() - 1)).getTitle());
        }
        newBillName.clear();
    }

    public void controllerDeleteBill() {
        listView.getItems().remove(selectedBill.getTitle());
        holder.getBills().remove(selectedBill);
    }

    public void openBill(ActionEvent event) throws IOException {
        if(selectedBill == null){
            return;
        }

        ScenaOgolnaController scenaOgolnaController = new ScenaOgolnaController(holder, selectedBill);
        FXMLLoader fxmlLoader  = new FXMLLoader(getClass().getResource("../Sceny/ScenaOgolna.fxml"));
        fxmlLoader.setController(scenaOgolnaController);
        root = fxmlLoader.load();

        scenaOgolnaController.displayBillName();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
    }

}
