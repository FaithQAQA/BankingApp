package com.example.TransactionsGui;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.example.App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TransactionController implements Initializable
{
@FXML
    private TableColumn<?, ?> Amount;

    @FXML
    private TextField AmountText;

    @FXML
    private TableColumn<?, ?> DescForTable;

    @FXML
    private TextArea TextAreaDesc;

    @FXML
    private TableView<?> TransactionTable;

    @FXML
    private ChoiceBox<String> TransactionType;

    @FXML
    private TableColumn<?, ?> dateForTable;
    
     @FXML
    private Button TransactionButton;


String TransactionTypeInsertData = "";
        @Override
        public void initialize(URL location, ResourceBundle resources) {
            TransactionType.getItems().addAll(
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
           TransactionType.valueProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("Selected Item: " + newValue);
                 TransactionTypeInsertData = newValue;
            });
        }

        
        @FXML
    void AddNewData(ActionEvent event, String username) 
    {
        String desc = TextAreaDesc.toString();
        String amountString = AmountText.toString();

        Double amount = Double.parseDouble(amountString);

                   try {
           Connection connection = App.establishDatabaseConnection();
           if (connection != null) 
           {
            
               App.useDatabase(connection);
               App.InsertIntoTransactionTable(connection, App.getUserId(connection, username), amount, desc, TransactionTypeInsertData); 
               TextAreaDesc.clear();
               AmountText.clear();            
            
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }

    }


        public void setData(String dataToPass) 
        {
            AddNewData(null, dataToPass);
        }
}
