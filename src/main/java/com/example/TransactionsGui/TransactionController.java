package com.example.TransactionsGui;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.example.App;
import com.example.Bean.Transaction;
import com.example.MainBankingGui.BankingInfoController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TransactionController implements Initializable {
    @FXML
    private TableColumn<Transaction, String> TransactionType;

    @FXML
    private TableColumn<Transaction, Double> Amount;

    @FXML
    private TableColumn<Transaction, Date> dateForTable;

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

    //BankingInfoController bankingInfoController = new BankingInfoController();
    String test = "";
    String TransactionTypeInsertData = "";
    private String username = "";
    Long Balance = -1L;
    private BankingInfoController bankingInfoController; // Reference to BankingInfoController

    // Constructor to accept the reference
    public TransactionController(BankingInfoController bankingInfoController) {
        this.bankingInfoController = bankingInfoController;
    }
    public TransactionController() {
    }
    private Connection databaseConnection; // Reuse the database connection

    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {

      
        
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
            "Income",
            "Interest",
            "Other"
        );

        TransactionPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected Item: " + newValue);
            TransactionTypeInsertData = newValue;
        });
        // ... Other initialization code ...
    }

    public void setData(String dataToPass, BankingInfoController bankingInfoController) {
        username = dataToPass;
        tableInt(username);
        this.bankingInfoController = bankingInfoController; // Store the reference

    }

    public String UpdateBalance() throws SQLException
    {
       String  amountUpdate = "";

            Connection connection = App.establishDatabaseConnection();
            if (connection != null) {
                App.useDatabase(connection);
             Long test =  App.getBalance(connection, App.getUserId(connection, username));

              amountUpdate = Long.toString(test);

            
    
}
            return amountUpdate;
    }

    public static String formatAsCurrency(String input) {
        return String.format("$%,.2f", Double.parseDouble(input));
    }
    

    @FXML
    void AddNewData(ActionEvent event) {
        String desc = TextAreaDesc.getText();
        test = AmountText.getText();
    
        // Check if the amountString is not empty
        if (!test.isEmpty()) {
            try {
                Double amount = Double.parseDouble(test);
    
                Connection connection = App.establishDatabaseConnection();
                if (connection != null) {
                    App.useDatabase(connection);
    
                    // Calculate the change in balance
                    long balanceChange = 0;
    
                    // Determine the transaction type (e.g., "Expenses", "Income")
                    if ("Expenses".equals(TransactionTypeInsertData)) {
                        balanceChange = (long) (-amount);
                    } else if ("Income".equals(TransactionTypeInsertData)) {
                        balanceChange = (long) (+amount);
                    }
    
                    // Update the balance
                    Balance += balanceChange;
    
                    // Update the balance in the database
                    App.UpdateBalance(connection, App.getUserId(connection, username), Balance);
    
                    // Insert the new transaction
                    App.InsertIntoTransactionTable(connection, App.getUserId(connection, username), amount, desc,
                        TransactionTypeInsertData, App.getTransactionAccountID(connection, App.getUserId(connection, username)));
    
                    TextAreaDesc.clear();
                    AmountText.clear();
                    processTransaction(TransactionTypeInsertData, amount);
                    tableInt(username);
              String pass =    UpdateBalance();
            String formattedBank= formatAsCurrency(pass);
              bankingInfoController.setLabel(formattedBank);
              
                   
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("AmountText is empty.");
        }
    }
    
    

    public void tableInt(String dataToPass) {
        try {
            if (databaseConnection == null) {
                databaseConnection = App.getExistingDatabaseConnection();
            }

            if (databaseConnection != null) {
                App.useDatabase(databaseConnection);
                TransactionTable.getItems().clear();
                List<Transaction> transactions = App.getTransactionMainPage(databaseConnection, App.getUserId(databaseConnection, username));
                ObservableList<Transaction> transactionData = FXCollections.observableArrayList(transactions);
                System.out.println("Number of transactions retrieved: " + transactions.size());
                TransactionTable.setItems(transactionData);
                Amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
                DescForTable.setCellValueFactory(new PropertyValueFactory<>("description"));
                TransactionType.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
                dateForTable.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
                BalanceCalculator();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception
        }
    }

    public void BalanceCalculator() {
        try {
            if (databaseConnection == null) {
                databaseConnection = App.getExistingDatabaseConnection();
            }

            if (databaseConnection != null) {
                App.useDatabase(databaseConnection);
                Balance = App.getBalance(databaseConnection, App.getUserId(databaseConnection, username));
                System.out.println(Balance);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception
        }
    }

    private boolean isUpdateInProgress = false;

    public void processTransaction(String transactionType, double amount) {
        if (!isUpdateInProgress) {
            isUpdateInProgress = true;
            switch (transactionType) {
                case "Expenses":
                    subtractAmount(amount);
                    break;
                case "Income":
                    addAmount(amount);
                    break;
                // Add more cases for other transaction types
                default:
                    // Handle unknown transaction type
                    break;
            }
            isUpdateInProgress = false;
            BalanceCalculator(); // Call BalanceCalculator after updating the balance
        }
    }

    private void subtractAmount(double amount) {
        // Convert Balance to a double, subtract the amount, and convert it back to Long
        Balance = (long) (Balance.doubleValue() - amount);
        // Update the balance in the database
        updateBalanceInDatabase();
    }

    private void addAmount(double amount) {
        Balance = (long) (Balance.doubleValue() + amount);
        updateBalanceInDatabase();
    }

    private void updateBalanceInDatabase() {
        // Update the balance in the database
        try {
            if (databaseConnection == null) {
                databaseConnection = App.getExistingDatabaseConnection();
            }

            if (databaseConnection != null) {
                App.useDatabase(databaseConnection);
                App.UpdateBalance(databaseConnection,
App.getUserId(databaseConnection, username), Balance);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception
        }
    }











    
}
