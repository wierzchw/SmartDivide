package Sceny;

import backend.Bill;
import backend.Holder;
import backend.Member;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ScenaOgolnaController implements Initializable {

    @FXML
    private Label billName;

    @FXML
    private TextField changeBillNameField;

    @FXML
    private ListView<String> memberListView;


    Bill bill;
    Holder holder;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Member selectedMember;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        holder = new Holder();
        ArrayList<Member> members = holder.getMembers();

        for (Member member : members) {
            memberListView.getItems().add(member.getName());
        }

        memberListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                String selectedMemberName = memberListView.getSelectionModel().getSelectedItem();
                for (Member member: members) {
                    if(Objects.equals(member.getName(), selectedMemberName)){
                        selectedMember = member;
                        break;
                    }
                }
            }
        });
    }

    public void changeBillName(){
        //todo
    }

    public void displayBillName(){
        billName.setText(bill.getTitle());
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
    }
}
