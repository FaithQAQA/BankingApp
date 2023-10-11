package com.example.TransactionsGui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TransactionController 
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
    private ChoiceBox<?> TransactionType;

    @FXML
    private TableColumn<?, ?> dateForTable;
    
     @FXML
    private Button TransactionButton;


        @FXML
    void AddNewData(ActionEvent event) {

    }
}
