package com.example.RegisterGui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import com.example.App;

public class RegisterController  implements Initializable
{
          @FXML
    private Button RegisterData;

   

        @FXML
    private PasswordField PasswordRegister;

    @FXML
    private TextField UsernameRegister;

      @FXML
    private ComboBox<String> Banktype;


    ComboBox<String> comboBox = new ComboBox<>();


   
    private static final int NUM_DIGITS = 16;
    private static final String DIGITS = "0123456789";
    private static final Random random = new Random();
    private static final Set<String> usedNumbers = new HashSet<>();
    private String bankAccountType= "Checkings,Saving ";

    public static long generateUniqueNumber() {
        while (true) {
            // Generate a random 16-digit number
            StringBuilder numberBuilder = new StringBuilder(NUM_DIGITS);
            for (int i = 0; i < NUM_DIGITS; i++) {
                int randomIndex = random.nextInt(DIGITS.length());
                char randomDigit = DIGITS.charAt(randomIndex);
                numberBuilder.append(randomDigit);
            }
            String randomNumber = numberBuilder.toString();
    
            // Check if the generated number is unique
            if (!usedNumbers.contains(randomNumber)) {
                usedNumbers.add(randomNumber);
                long done = Long.parseLong(randomNumber);
                return done;
            }
        }
    }
    


    @FXML
    void BankTypeChecker(ActionEvent event) {

    }


String bankAccount="";
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    
    {
        Banktype.getItems().addAll("Joint Account", "Student Account:");
    
        Banktype.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Selected Item: " + newValue);
             bankAccount = newValue;
        });

    }

     @FXML
    void RegisterDataHandler(ActionEvent event) 
    {
        String Password= PasswordRegister.getText();
        String Username = UsernameRegister.getText();
        System.out.println("TEST");
        Long CardNumber = generateUniqueNumber();
              try {
           Connection connection = App.establishDatabaseConnection();
           if (connection != null) 
           {
            
               App.useDatabase(connection);
               App.createUserWithAccount(connection, Username, Password, bankAccountType.concat(bankAccount), 20230000.00, CardNumber);
               
            
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }

       PasswordRegister.clear();
       UsernameRegister.clear();
    }
}


