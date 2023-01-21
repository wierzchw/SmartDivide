package Sceny;

import backend.Bill;
import backend.Holder;
import backend.Member;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ScenaDokladnaController implements Initializable {

    @FXML
    private Button addMemberButton;
    @FXML
    private Button addSavedMemberButton;
    @FXML
    private TextField addMemberTextField;
    @FXML
    private Label billName;
    @FXML
    private ListView<?> debtsListView;
    @FXML
    private Button deleteDebtButton;
    @FXML
    private Button deleteMemberButton;
    @FXML
    private Button forgetMemberButton;
    @FXML
    private ListView<String> membersListView;

    @FXML
    private Button saveMemberButton;
    @FXML
    private ListView<String> savedMemebersListView;
    @FXML
    private Button switchToScenaOgolnaButton;
    private Member selectedSavedMember;
    private Member selectedMember;

    Bill bill;
    Holder holder;
    private Stage stage;
    private Scene scene;
    private Parent root;

    public ScenaDokladnaController(Bill bill, Holder holder)  {
        this.bill = bill;
        this.holder = holder;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Member> savedMembers = holder.getMembers();
        for (Member savedMember : savedMembers) {
            savedMemebersListView.getItems().add(savedMember.getName());
        }

        ArrayList<Member> members = (ArrayList<Member>) bill.getMembers();
        for (Member member : members) {
            membersListView.getItems().add(member.getName());
        }

        savedMemebersListView.getSelectionModel().selectedItemProperty().addListener(this::changedSavedMember);
        membersListView.getSelectionModel().selectedItemProperty().addListener(this::changedMember);
    }

    private void changedMember(Observable observable) {
        String selectedMemberName = membersListView.getSelectionModel().getSelectedItem();

        ArrayList<Member> members = holder.getMembers();
        for (Member member : members)
            if (Objects.equals(member.getName(), selectedMemberName)) {
                selectedMember = member;
            }
    }

    private void changedSavedMember(Observable observable) {
        String selectedSavedMemberName = savedMemebersListView.getSelectionModel().getSelectedItem();

        ArrayList<Member> members = holder.getMembers();
        for (Member member : members)
            if (Objects.equals(member.getName(), selectedSavedMemberName)) {
                selectedSavedMember = member;
            }
    }


    @FXML
    void addMember(ActionEvent event) {
        Member newMember = new Member(addMemberTextField.getText());

        if(!holder.checkMemberExistence(newMember.getName())) {
            holder.addMember(newMember);
            bill.addMember(newMember);
            membersListView.getItems().add(newMember.getName());
            savedMemebersListView.getItems().add(newMember.getName());
            addMemberTextField.clear();
        }
    }

    @FXML
    void deleteDebt(ActionEvent event) {

    }

    @FXML
    void deleteMember(ActionEvent event) {

    }

    @FXML
    void forgetMember(ActionEvent event) {
        boolean savedMemberPresent = false;

        for(Bill bill: holder.getBills()) {
            for (Member member : bill.getMembers()) {
                if (Objects.equals(member.getName(), selectedSavedMember.getName())) {
                    savedMemberPresent = true;
                    break;
                }
            }
        }

        if (selectedSavedMember == null || savedMemberPresent){
            return;
        }

        savedMemebersListView.getItems().remove(selectedSavedMember.getName());
        holder.getMembers().remove(selectedSavedMember);
    }

    void addSavedMember(ActionEvent event) {
        bill.addMember(selectedSavedMember);
        membersListView.getItems().add(selectedSavedMember.getName());
    }

    @FXML
    void switchToScenaOgolna(ActionEvent event) throws IOException {
        ScenaOgolnaController scenaOgolnaController = new ScenaOgolnaController(holder, bill);
        FXMLLoader fxmlLoader  = new FXMLLoader(getClass().getResource("../Sceny/ScenaOgolna.fxml"));
        fxmlLoader.setController(scenaOgolnaController);
        root = fxmlLoader.load();

        scenaOgolnaController.displayBillName();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void displayBillName(){
        billName.setText(bill.getTitle());
    }

}

