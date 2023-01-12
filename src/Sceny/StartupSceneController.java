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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        holder = new Holder();
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
        Bill newBill = new Bill(newBillName.getText());
        ArrayList<Bill> bills = holder.getBills();
        boolean sameNameBillExists = false;

        for (Bill bill : bills) {
            if (Objects.equals(bill.getTitle(), newBill.getTitle())) {
                sameNameBillExists = true;
                break;
            }
        }

        if(!sameNameBillExists) {
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
        FXMLLoader fxmlLoader  = new FXMLLoader(getClass().getResource("../Sceny/ScenaOgolna.fxml"));
        root = fxmlLoader.load();

        ScenaOgolnaController scenaOgolnaController = fxmlLoader.getController();
        scenaOgolnaController.setBill(selectedBill);
        scenaOgolnaController.setHolder(holder);
        scenaOgolnaController.displayBillName();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
