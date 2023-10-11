package com.example.Bean;

public class Transaction {
    private double amount;
    private String description;
    private String transactionType;

    public Transaction(double amount, String description, String transactionType) {
        this.amount = amount;
        this.description = description;
        this.transactionType = transactionType;
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
}
