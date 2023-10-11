package com.example.BankingAppGui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginInPageGUI extends Application
{

    
    public static void main(String[] args) 
    {
        launch(args);
    
    }

    @Override
    public void start(Stage primaryStage) throws Exception 
    {

          FXMLLoader loader = new FXMLLoader(getClass().getResource("MyScene.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 1024, 600);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Scene Builder Example");
        primaryStage.show();
    } 

    }

