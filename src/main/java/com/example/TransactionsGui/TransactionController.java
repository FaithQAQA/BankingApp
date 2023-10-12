package com.example.TransactionsGui;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.App;
import com.example.Bean.Transaction;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TransactionController implements Initializable
{
    @FXML
    private TableColumn<Transaction, String> TransactionType;

    @FXML
    private TableColumn<Transaction, Double> Amount;

    @FXML
    private TableColumn<Transaction, String> dateForTable;

    @FXML
    private TableColumn<Transaction, String> DescForTable;

    @FXML
    private TextArea TextAreaDesc;

    @FXML
    private Button TransactionButton;

    @FXML
    private TableView<Transaction> TransactionTable;

    @FXML
    private ChoiceBox<String> TransactionPicker;

    @FXML
    private TextField AmountText;

    String TransactionTypeInsertData = "";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TransactionPicker.getItems().addAll(
            "Shopping",
            "Expenses",
            "Groceries",
            "Entertainment",
            "Transportation",
            "Dining",
            "Utilities",
            "Healthcare",
            "Education",
            "Investment",
            "Gifts",
            "Salary",
            "Interest",
            "Other"
        );

       TransactionPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected Item: " + newValue);
            TransactionTypeInsertData = newValue;
        });

setData(null);
    }

    String username = "";
    @FXML
    void AddNewData(ActionEvent event) {
        String desc = TextAreaDesc.getText();
        String amountString = AmountText.getText();
        Double amount = Double.parseDouble(amountString);

        try {
            Connection connection = App.establishDatabaseConnection();
            if (connection != null) {
                App.useDatabase(connection);
                App.InsertIntoTransactionTable(connection, App.getUserId(connection, username), amount, desc, TransactionTypeInsertData, App.getTransactionAccountID(connection, App.getUserId(connection, username)));
                TextAreaDesc.clear();
                AmountText.clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setData(String dataToPass) 
    {
     username = dataToPass;
    }
}
