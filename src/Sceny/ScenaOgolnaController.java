package Sceny;

import backend.Bill;
import backend.Holder;
import backend.Member;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class ScenaOgolnaController implements Initializable {

    @FXML
    public Button changeBillNameButton;
    @FXML
    private Label billTitleLabel;
    @FXML
    private TextField changeBillNameField;
    @FXML
    private Button closeBllButton;
    @FXML
    private ChoiceBox<String> newTransactionCreditor;

    @FXML
    private ListView<String> newTransactionDebtors;
    @FXML
    private Button addTransactionButton;

    @FXML
    private TextField newTransactionTItle;
    @FXML
    private TextField newTransactionValue;
    @FXML
    private Button openMembersViewButton;


    Bill bill;
    Holder holder;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ArrayList<Member> selectedTransactionDebtors;
    private Member selectedTransactionCreditor;

    public ScenaOgolnaController(Holder holder, Bill selectedBill) {
        this.holder = holder;
        this.bill = selectedBill;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Member> members = (ArrayList<Member>) bill.getMembers();
        for (Member member : members) {
            newTransactionDebtors.getItems().add(member.getName());
            newTransactionCreditor.getItems().add(member.getName());
        }

        selectedTransactionDebtors = new ArrayList<>();
        newTransactionDebtors.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        newTransactionDebtors.getSelectionModel().selectedItemProperty().addListener(this::changedDebtors);

        newTransactionCreditor.getSelectionModel().selectedItemProperty().addListener(this::changedCreditor);
    }

    private void changedCreditor(ObservableValue<? extends String> observableValue, String s, String t1) {
        String selectedCreditorName = newTransactionCreditor.getSelectionModel().getSelectedItem();

        ArrayList<Member> members = holder.getMembers();
        for (Member member : members)
                if (Objects.equals(member.getName(), selectedCreditorName)) {
                    selectedTransactionCreditor = member;
                }
    }

    private void changedDebtors(ObservableValue<? extends String> observableValue, String s, String t1) {
        ObservableList<String> selectedDebtorsNames = newTransactionDebtors.getSelectionModel().getSelectedItems();

        selectedTransactionDebtors.clear();
        ArrayList<Member> members = holder.getMembers();
        for (Member member: members) {
            for(String debtorName : selectedDebtorsNames) {
                if (Objects.equals(member.getName(), debtorName)) {
                    selectedTransactionDebtors.add(member);
                }
            }
        }
        System.out.println(selectedTransactionDebtors);
    }

    public void closeBill(ActionEvent event) throws IOException {
        StartupSceneController startupSceneController = new StartupSceneController(holder);
        FXMLLoader fxmlLoader  = new FXMLLoader(getClass().getResource("../Sceny/StartupScene.fxml"));
        fxmlLoader.setController(startupSceneController);
        root = fxmlLoader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void openMembersView(ActionEvent event) throws IOException {
        ScenaDokladnaController scenaDokladnaController = new ScenaDokladnaController(bill, holder);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Sceny/ScenaDokladna.fxml"));
        fxmlLoader.setController(scenaDokladnaController);
        root = fxmlLoader.load();

        scenaDokladnaController.displayBillName();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void changeBillName(){
        String newTitle = changeBillNameField.getText();
        if(!holder.checkBillExistence(newTitle)) {
            bill.setTitle(newTitle);
            displayBillName();
        }
        changeBillNameField.clear();
    }

    public void displayBillName(){
        billTitleLabel.setText(bill.getTitle());
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public void controllerAddDebt() {
        if (selectedTransactionDebtors.isEmpty() || newTransactionCreditor == null || newTransactionValue.getText().isEmpty() ||  newTransactionTItle.getText().isEmpty()) {
            return;
        }

        Member[] debtors = new Member[selectedTransactionDebtors.size()];
        debtors = selectedTransactionDebtors.toArray(debtors);
        System.out.println(Arrays.toString(debtors));

        Double value = Double.valueOf(newTransactionValue.getText());
        System.out.println(value);

        String title = newTransactionTItle.getText();
        System.out.println(title);

        Member creditor = selectedTransactionCreditor;
        System.out.println(creditor);

        if (selectedTransactionDebtors.size() > 1) {
            bill.addGroupDebt(value, creditor, debtors);
        } else {
            bill.addDebtForTime(value, debtors[0], creditor, LocalDateTime.now());
        }
    }

}
