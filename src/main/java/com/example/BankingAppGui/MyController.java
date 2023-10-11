package com.example.BankingAppGui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import com.example.App;
import com.example.Bean.UserInfo;
import com.example.MainBankingGui.BankingInfoController;

public class MyController 
{

   @FXML
    private PasswordField Password;

    @FXML
    private TextField Username;

    @FXML
    private Button Signin;

    @FXML
    private Button myButton;

    @FXML
    private Button Register;

   UserInfo CheckCreds = new UserInfo();
   @FXML
   <BankinginfoController> void WhenClickedOn(ActionEvent event) throws IOException {
       String PasswordGetter = Password.getText();
       String UsernameGetter = Username.getText();
   
       try {
           Connection connection = App.establishDatabaseConnection();
           if (connection != null) {
               App.useDatabase(connection);
               boolean validCredentials = App.verifyUserCredentials(connection, UsernameGetter, PasswordGetter);
               
               if (validCredentials) {
                   // Credentials are valid, perform actions like navigating to another scene
                   // or displaying a success message.

                   UserInfo user = new UserInfo();
                   System.out.println("Login successful");
                   user.setUsername(UsernameGetter);
                   
                   Username.clear();
                   Password.clear();


                   FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/MainBankingGui/BankinginfoFXML.fxml"));
                   Parent root = loader.load();
                   BankinginfoController bankinginfoController = loader.getController();
                   
                   // Pass data to the controller
                   String dataToPass = UsernameGetter;
                   ((BankingInfoController) bankinginfoController).setData(dataToPass);

    // Set the scene and how the edit window
    Stage primaryStage = new Stage();
    Scene scene = new Scene(root, 1150, 600);
    primaryStage.setTitle("MainPage");
    primaryStage.setScene(scene);
    primaryStage.show();
               } else {
                   // Credentials are invalid, display an error message or take appropriate action.
                   System.out.println("Login failed");
               }
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }



   }
   
   @FXML
   void RegisterNextStage(ActionEvent event) throws IOException {



    FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterGui.fxml"));
    Parent root = loader.load();


    // Set the scene and how the edit window
    Stage primaryStage = new Stage();
    Scene scene = new Scene(root, 1024, 600);
    primaryStage.setTitle("Edit Task");
    primaryStage.setScene(scene);
    primaryStage.show();










   }
   
}
