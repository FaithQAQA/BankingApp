package com.example.MainBankingGui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import com.example.App;
import com.example.Bean.Transaction;
import com.example.Bean.UserInfo;
import com.example.TransactionsGui.TransactionController;

public class BankingInfoController {
    @FXML
    private Label Date;

    @FXML
    private Label WelcomeUser;

    @FXML
    private Label Balance;

    @FXML
    private TableView<Transaction> transactionData;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;
    
    @FXML
    private TableColumn<Transaction, String> descriptionColumn;
    
    @FXML
    private TableColumn<Transaction, String> transactionTypeColumn;
    
    @FXML
    private Button AddTransaction;
 

    @FXML
    private Label CardNumOfAccount;

    private DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
UserInfo idgetter = new UserInfo();
    public void initialize() throws SQLException {
        // You can initialize your controller here
        updateDateTimeLabel(); // Initial update
        startDateTimeUpdateTimer(); // Start updating the date and time label periodically
        getBalance(null);
        getCardNumber(null);
       TransactionDataGetter(null);
           
    }

           //     TransactionController controller  = new TransactionController();

String passdata ="";
    public void setData(String data) throws SQLException  {
        WelcomeUser.setText("Welcome back " + data);
        getBalance(data);
        getCardNumber(data);
        TransactionDataGetter(data);
        passdata = data;
        

    }

    private void updateDateTimeLabel() {
        LocalDateTime myDateObj = LocalDateTime.now();
        String formattedDate = myDateObj.format(myFormatObj);
        Date.setText(formattedDate);
    }
    public static void setBalanceText(Label balanceLabel, Long balance) {
        // Format the balance as a currency string
        String formattedBalance = String.format("$%,.2f", (double) balance);
    
        // Set the formatted balance text to the label
        balanceLabel.setText(formattedBalance);
    }
    
    private void getBalance( String data) throws SQLException
    {

          Connection connection = App.establishDatabaseConnection();
           if (connection != null) 
           {
            
               App.useDatabase(connection);
            //   App.getUserId(connection, data);
             Long bank =  App.GetBankingInfo(connection, App.getUserId(connection, data));  
             setBalanceText(Balance, bank); // Set the formatted balance text
             

           }


            
    }
    public void TransactionDataGetter(String data) throws SQLException {
        Connection connection = App.establishDatabaseConnection();
        if (connection != null) {
            App.useDatabase(connection);
            int userId = App.getUserId(connection, data);
    
            // Retrieve transaction data from the database
            List<Transaction> transactions = App.getTransactionMainPage(connection, userId);
    
        //    System.out.println("Number of transactions retrieved: " + transactions.size()); // Debug statement
    
    amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));

            // Set the items for your TableView
            transactionData.getItems().addAll(transactions);



            
        }
    }
    
    
    
 private void getCardNumber(String data) throws SQLException {
    Connection connection = App.establishDatabaseConnection();
    if (connection != null) {
        App.useDatabase(connection);
        Long cardNumber = App.getCardNumber(connection, App.getUserId(connection, data));
      String cardnum= cardNumberChecker(cardNumber);
       CardNumOfAccount.setText(cardnum);
   System.out.println(cardNumber);
    }
}


private String cardNumberChecker(Long cardNum) {
    String s = String.valueOf(cardNum);
    
    StringBuilder maskedString = new StringBuilder();

    if (s.length() >= 4) {
        for (int i = 0; i < s.length(); i++) {
            if (i < s.length() - 4) {
                maskedString.append('*');
            } else {
                maskedString.append(s.charAt(i));
            }
        }
    } else {
        maskedString.append(s);
    }

    // Convert the StringBuilder back to a String
    String maskedCardNumber = maskedString.toString();
    return maskedCardNumber;
}

    

    private void startDateTimeUpdateTimer() {
        // Create a timeline to update the date and time label every second
        Duration duration = Duration.seconds(1);
        KeyFrame keyFrame = new KeyFrame(duration, event -> updateDateTimeLabel());
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE); // Repeat indefinitely
        timeline.play();
    }


    @FXML
    void MakeTransaction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/TransactionsGui/Transaction.fxml"));
        Parent root = loader.load();
        TransactionController transactionController = loader.getController();
String dataToPass = passdata;
transactionController.setData(dataToPass, this); // Pass the reference to this controller

       
        Stage primaryStage = new Stage();
        Scene scene = new Scene(root, 1150, 600);
        primaryStage.setTitle("MainPage");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    


public void setLabel (String Text)
{
Balance.setText(Text);
}

}