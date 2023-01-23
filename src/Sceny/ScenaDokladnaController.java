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
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
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
    private ListView<String> debtsListView;
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
    @FXML
    private Label selectedMemberBilans;
    @FXML
    private Label selectedMemberName;
    @FXML
    private TextField changeMemberNameTextField;
    @FXML
    private Button changeMemberNameButton;


    private Member selectedSavedMember;
    private Member selectedMember;

    Bill bill;
    Holder holder;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Member[] selectedDebt;

    public ScenaDokladnaController(Bill bill, Holder holder)  {
        this.bill = bill;
        this.holder = holder;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // sekcja zapisanych osób
        ArrayList<Member> savedMembers = holder.getMembers();
        for (Member savedMember : savedMembers) {
            savedMemebersListView.getItems().add(savedMember.getName());
        }
        savedMemebersListView.getSelectionModel().selectedItemProperty().addListener(this::changedSavedMember);

        // sekcja osob w rachunku
        ArrayList<Member> members = (ArrayList<Member>) bill.getMembers();
        for (Member member : members) {
            membersListView.getItems().add(member.getName());
        }
        membersListView.getSelectionModel().selectedItemProperty().addListener(this::changedMember);

        // sekcja długów osoby
        debtsListView.getSelectionModel().selectedItemProperty().addListener(this::changedDebt);

        //TESTY
        System.out.println("bill members:" + bill.getMembers());
    }

    private void changedDebt(Observable observable) {
        String selectedDebtName = debtsListView.getSelectionModel().getSelectedItem();

        Map<Member[], BigDecimal> result = bill.getSolution();
        for (Member[] members : result.keySet().stream().toList()){
            if (selectedDebtName == null) {break;}
            String checkedName = result.get(members) + " => " + members[1].getName();
            if (selectedDebtName.equals(checkedName)) {
                selectedDebt = members;
            }
        }

        //TESTY
        System.out.println("selected Debt:" + selectedDebt);
    }


    private void changedMember(Observable observable) {
        String selectedMemberName = membersListView.getSelectionModel().getSelectedItem();

        ArrayList<Member> members = new ArrayList<>();
        members.addAll(holder.getMembers());
        members.addAll(bill.getMembers());
        for (Member member : members)
            if (Objects.equals(member.getName(), selectedMemberName)) {
                selectedMember = member;
                updateDebtList();
            }
        members.clear();

        //TESTY
        System.out.println("selected member:" + selectedMember);
    }

    private void updateDebtList() {
        selectedMemberName.setText(selectedMember.getName());
        selectedMemberBilans.setText(String.valueOf(bill.getDebtList().get(selectedMember)));

        debtsListView.getItems().clear();
        bill.minTransfers();

        Map<Member[], BigDecimal> result = bill.getSolution();
        for (Member[] members : result.keySet().stream().toList()){
            if (members[0].getName().equals(selectedMember.getName())) {
                debtsListView.getItems().add(result.get(members) + " => " + members[1].getName());
            }
        }
    }

    private void changedSavedMember(Observable observable) {
        String selectedSavedMemberName = savedMemebersListView.getSelectionModel().getSelectedItem();

        ArrayList<Member> members = new ArrayList<>();
        members.addAll(holder.getMembers());
        for (Member member : members)
            if (Objects.equals(member.getName(), selectedSavedMemberName)) {
                selectedSavedMember = member;
            }

        //TESTY
        System.out.println("selected saved member name:" + selectedSavedMemberName);
        System.out.println("holder members:" + members);
        System.out.println("selected saved member:" + selectedSavedMember);
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
        if (selectedDebt == null) {return;}
        bill.settleDebtFromSolution(selectedDebt[0], selectedDebt[1]);
        updateDebtList();
    }

    @FXML
    void deleteMember(ActionEvent event) {
        if (selectedMember == null || !bill.getDebtList().get(selectedMember).equals(BigDecimal.valueOf(0))) {
            return;
        }
        bill.removeMember(selectedMember);
        membersListView.getItems().remove(selectedMember.getName());

        //TESTY
        System.out.println("bill members:" + bill.getMembers());
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

        if (selectedSavedMember == null ||  savedMemberPresent){
            return;
        }

        holder.getMembers().remove(selectedSavedMember);
        savedMemebersListView.getItems().remove(selectedSavedMember.getName());

        //TESTY
        System.out.println("holder members:" + holder.getMembers());
    }

    @FXML
    void addSavedMember(ActionEvent event) {
        boolean sameNameMemberExists = false;
        for (Member member : bill.getMembers()) {
            if (Objects.equals(member.getName(), selectedSavedMember.getName())) {
                sameNameMemberExists = true;
                break;
            }
        }

        if (!sameNameMemberExists) {
            bill.addMember(selectedSavedMember);
            membersListView.getItems().add(selectedSavedMember.getName());
        }
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

    @FXML
    public void changeMemberName() {
        String newName = changeMemberNameTextField.getText();
        if(!holder.checkMemberExistence(newName)) {
            savedMemebersListView.getItems().remove(selectedSavedMember.getName());
            membersListView.getItems().remove(selectedSavedMember.getName());

            selectedSavedMember.setName(newName);

            savedMemebersListView.getItems().add(selectedSavedMember.getName());
            membersListView.getItems().add(selectedSavedMember.getName());

            changeMemberNameTextField.clear();
        }
    }

}

