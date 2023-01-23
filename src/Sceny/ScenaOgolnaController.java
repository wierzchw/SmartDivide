package Sceny;

import backend.Bill;
import backend.Holder;
import backend.Member;
import backend.Transaction;
import javafx.beans.Observable;
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
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.math.BigDecimal;
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
    @FXML
    private Label transactionCreditor;
    @FXML
    private Label transactionDebtor;
    @FXML
    private Label transactionName;
    @FXML
    private Label transactionValue;
    @FXML
    private Label transactionTime;
    @FXML
    private ListView<String> transactionHistory;

    Bill bill;
    Holder holder;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ArrayList<Member> selectedTransactionDebtors;
    private Member selectedTransactionCreditor;
    private Transaction currentTransaction;

    public ScenaOgolnaController(Holder holder, Bill selectedBill) {
        this.holder = holder;
        this.bill = selectedBill;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // new transaction section
        ArrayList<Member> members = (ArrayList<Member>) bill.getMembers();
        for (Member member : members) {
            newTransactionDebtors.getItems().add(member.getName());
            newTransactionCreditor.getItems().add(member.getName());
        }

        newTransactionDebtors.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        newTransactionDebtors.getSelectionModel().selectedItemProperty().addListener(this::changedDebtors);

        newTransactionCreditor.getSelectionModel().selectedItemProperty().addListener(this::changedCreditor);

        // transaction section
        ArrayList<Transaction> transactions = (ArrayList<Transaction>) bill.getTransactionHistory();
        for (Transaction transaction : transactions) {
            transactionHistory.getItems().add(transaction.getTitle());
        }

        transactionHistory.getSelectionModel().selectedItemProperty().addListener(this::changedTransaction);

        // selected transaction section
    }

    private void changedTransaction(Observable observable) {
        String transactionTitle = transactionHistory.getSelectionModel().getSelectedItem();

        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.addAll(bill.getTransactionHistory());
        for (Transaction transaction : transactions) {
            if (transactionTitle.equals(transaction.getTitle())) {
                currentTransaction = transaction;
            }
        }
        displayTransactionDetails();
    }

    private void displayTransactionDetails() {
        transactionName.setText(currentTransaction.getTitle());
        transactionCreditor.setText(currentTransaction.getCreditor().getName());
        transactionDebtor.setText(currentTransaction.getDebtor().getName());
        transactionValue.setText(String.valueOf(currentTransaction.getAmount()));
        transactionTime.setText(currentTransaction.getTime().toLocalDate().toString());
    }

    private void changedCreditor(ObservableValue<? extends String> observableValue, String s, String t1) {
        String selectedCreditorName = newTransactionCreditor.getSelectionModel().getSelectedItem();

        ArrayList<Member> members = new ArrayList<>();
        members.addAll(holder.getMembers());
        members.addAll(bill.getMembers());
        for (Member member : members) {
            if (Objects.equals(member.getName(), selectedCreditorName)) {
                selectedTransactionCreditor = member;
            }
        }
        members.clear();
    }

    private void changedDebtors(ObservableValue<? extends String> observableValue, String s, String t1) {
        ObservableList<String> selectedDebtorsNames = newTransactionDebtors.getSelectionModel().getSelectedItems();

        ArrayList<Member> members = new ArrayList<>();
        members.addAll(holder.getMembers());
        members.addAll(bill.getMembers());
        for (Member member: members) {
            for(String debtorName : selectedDebtorsNames) {
                if (Objects.equals(member.getName(), debtorName)) {
                    if (selectedTransactionDebtors == null) {
                        selectedTransactionDebtors = new ArrayList<Member>();
                    }
                    if (!selectedTransactionDebtors.contains(member)) {
                        selectedTransactionDebtors.add(member);
                    }
                }
            }
        }
        members.clear();

        //TEST
        System.out.println("selected Debtors:" + selectedTransactionDebtors);
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
        if (selectedTransactionDebtors.isEmpty() ||
                newTransactionCreditor == null ||
                newTransactionValue.getText().isEmpty() ||
                newTransactionTItle.getText().isEmpty()) {
            return;
        }

        String title = newTransactionTItle.getText();

        Double value = Double.valueOf(newTransactionValue.getText());

        Member[] debtors = new Member[selectedTransactionDebtors.size()];
        debtors = selectedTransactionDebtors.toArray(debtors);

        Member creditor = selectedTransactionCreditor;

        int size = bill.getTransactionHistory().size();
//        if (selectedTransactionDebtors.size() > 1) {
//            bill.addGroupDebt(title, BigDecimal.valueOf(value), creditor, debtors);
//            for (int i = 0; i<selectedTransactionDebtors.size(); i++) {
//                transactionHistory.getItems().add(bill.getTransactionHistory().get(size - 1 - i).getTitle());
//            }
//        } else {
            bill.addDebt(title, BigDecimal.valueOf(value), debtors[debtors.length - 1], creditor);
            transactionHistory.getItems().add(bill.getTransactionHistory().get(size).getTitle());
//        }




    }

}
