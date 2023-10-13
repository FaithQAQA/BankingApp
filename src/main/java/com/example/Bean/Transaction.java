package com.example.Bean;

import java.sql.Timestamp;
import java.util.Date;

public class Transaction {
    private double amount;
    private String description;
    private String transactionType;
    private Date  dateOfTransaction;
    private Timestamp transactionDate; 


   
    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public Transaction(double amount, String description, String transactionType, Date dateOfTransaction , Timestamp transactionDate) {
        this.amount = amount;
        this.description = description;
        this.transactionType = transactionType;
        this.dateOfTransaction = dateOfTransaction;
        this.transactionDate = transactionDate;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getTransactionType() {
        return transactionType;
    }
     public Date getDateOfTransaction() {
        return dateOfTransaction;
    }

    public void setDateOfTransaction(Date dateOfTransaction) {
        this.dateOfTransaction = dateOfTransaction;
    }

}
