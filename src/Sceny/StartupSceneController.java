package Sceny;

import backend.Bill;
import backend.Holder;
import backend.Member;
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
import javafx.scene.control.SelectionMode;
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
    private ArrayList<Bill> selectedBills;


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

        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String selectedBillName = listView.getSelectionModel().getSelectedItem();
                for (Bill bill : bills) {
                    if(Objects.equals(bill.getTitle(), selectedBillName)){
                        if (selectedBills == null) {
                            selectedBills = new ArrayList<Bill>();
                        }
                        selectedBills.add(bill);
                        selectedBill = bill;
                    }
                }

                //TESTY
                System.out.println("selected bill:" + selectedBill.getTitle());
                if(selectedBills.size() >= 2) {
                    System.out.println("last selected bill:" + selectedBills.get(selectedBills.size() - 2).getTitle());
                }
                System.out.println("selected bills size:" + selectedBills.size());
            }
        });

        //TESTY
        System.out.println("holder members:" + holder.getMembers());
        System.out.println("holder bills:" + holder.getBills());
    }

    public void controllerAddBill() {
        ArrayList<Bill> bills = holder.getBills();
        Bill newBill = new Bill(newBillName.getText());

        if(!holder.checkBillExistence(newBill.getTitle())) {
            holder.addBill(newBill);
            listView.getItems().add((bills.get(bills.size() - 1)).getTitle());
        }
        newBillName.clear();

        //TESTY
        System.out.println("holder bills:" + holder.getBills());
    }

    public void controllerDeleteBill() {
        if (selectedBill == null) { return;}

        holder.getBills().remove(selectedBill);
        listView.getItems().remove(selectedBill.getTitle());

        //TESTY
        System.out.println("holder bills:" + holder.getBills());
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

    public void mergeSelectedBills() {
        if (selectedBills.size() < 2) {return;}
        Bill secondBill = selectedBills.get(selectedBills.size()-2);
        selectedBill.mergeBills(secondBill);

        holder.getBills().remove(secondBill);
        listView.getItems().remove(secondBill.getTitle());
    }

}
