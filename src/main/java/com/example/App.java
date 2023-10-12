package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.example.Bean.Transaction;
import com.example.Bean.UserInfo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class App {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "gt64ince";
    private static final String DATABASE_NAME = "bankinginfo";

    static UserInfo idGetter = new UserInfo();
    
    public static Connection establishDatabaseConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    public static void useDatabase(Connection connection) throws SQLException {
        String useDatabaseQuery = "USE " + DATABASE_NAME;
        try (PreparedStatement useDatabaseStatement = connection.prepareStatement(useDatabaseQuery)) {
            useDatabaseStatement.execute();
        }
    }

    public static boolean verifyUserCredentials(Connection connection, String userName, String password) throws SQLException {
        String selectQuery = "SELECT * FROM Users WHERE username = ?";
        //SELECT balance FROM account WHERE user_id = ?;
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            while (resultSet.next()) {
                String usernameValue = resultSet.getString("username");
                String passwordHash = resultSet.getString("password_hash");
                System.out.println(passwordHash);
    
             
                if (userName.equals(usernameValue) && BCrypt.checkpw(password, passwordHash)) {
                    return true; 
                }
            }
        }
    
        return false; 
    }

    public static int getUserId(Connection connection, String userName) throws SQLException {
    String selectQuery = "SELECT user_id FROM Users WHERE username = ?";
    int userId = -1; // Initialize with a default value that indicates no user found

    try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
        preparedStatement.setString(1, userName);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            userId = resultSet.getInt("user_id");
            idGetter.setId(userId);
        }
    }

    return userId;
}

    public static int getTransactionAccountID(Connection connection, int id) throws SQLException {
    String selectQuery = "SELECT account_id FROM transaction WHERE user_id = ?";
    int account_id = -1; // Initialize with a default value that indicates no user found

    try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            account_id = resultSet.getInt("account_id");
            idGetter.setId(account_id);
        }
    }

    return account_id;
}



    public static void createUserWithAccount(Connection connection, String username, String plaintextPassword, String accountType, double initialBalance, Long cardNumber) throws SQLException {
        // Hash the plaintext password
        String hashedPassword = BCrypt.hashpw(plaintextPassword, BCrypt.gensalt());
    
        // SQL statements
        String insertUserQuery = "INSERT INTO Users (username, password_hash) VALUES (?, ?)";
        String insertAccountQuery = "INSERT INTO account (user_id, account_type, balance, card_number) VALUES (?, ?, ?, ?)";
        String insertTransactionQuery = "INSERT INTO transaction (user_id, account_id, transaction_type, amount, transaction_date, description) VALUES (?, ?, ?, ?, ?, ?)";
    
        connection.setAutoCommit(false);
    
        try {
            try (PreparedStatement userStatement = connection.prepareStatement(insertUserQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                userStatement.setString(1, username);
                userStatement.setString(2, hashedPassword);
                int userRowsInserted = userStatement.executeUpdate();
    
                if (userRowsInserted > 0) {
                    System.out.println("User added successfully");
                    // Get the generated user ID
                    int userId = -1;
                    try (ResultSet generatedKeys = userStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            userId = generatedKeys.getInt(1);
                            idGetter.setId(userId);
                        }
                    }
    
                    try (PreparedStatement accountStatement = connection.prepareStatement(insertAccountQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        accountStatement.setInt(1, userId);
                        accountStatement.setString(2, accountType);
                        accountStatement.setDouble(3, initialBalance);
                        accountStatement.setLong(4, cardNumber); // Use index 4 for card number
                        int accountRowsInserted = accountStatement.executeUpdate();
    
                        if (accountRowsInserted > 0) {
                            System.out.println("Account added successfully");
                            // Get the generated account ID
                            int accountId = -1;
                            try (ResultSet generatedKeys = accountStatement.getGeneratedKeys()) 
                            {
                                if (generatedKeys.next()) {
                                    accountId = generatedKeys.getInt(1);

                                }
                            }
    
                            // Insert a transaction record for account creation
                            try (PreparedStatement transactionStatement = connection.prepareStatement(insertTransactionQuery)) {
                                transactionStatement.setInt(1, userId);
                                transactionStatement.setInt(2, accountId);
                                transactionStatement.setString(3, "Account Creation");
                                transactionStatement.setDouble(4, 0.0);
                                transactionStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                                transactionStatement.setString(6, "Making of transaction data for " + username); // Provide a value for the 5th parameter
                                int transactionRowsInserted = transactionStatement.executeUpdate();
                            
                                if (transactionRowsInserted > 0) {
                                    System.out.println("Transaction logged for account creation");
                                } else {
                                    System.out.println("Failed to log transaction for account creation");
                                }
                            }
                            
                        } else {
                            System.out.println("Account not added");
                        }
                    }
                } else {
                    System.out.println("User not added");
                }
            }
    
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            e.printStackTrace();
        } finally {
            connection.setAutoCommit(true);
        }
    }
    
    
    public static int GetBankingInfo(Connection connection, int id) throws SQLException {
        String selectBalanceQuery = "SELECT balance FROM account WHERE user_id = ?;";
    
        int balance= -1;
    
        connection.setAutoCommit(false);
    
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectBalanceQuery)) {
            preparedStatement.setInt(1, id);
    
            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Check if there is at least one row in the result set
            if (resultSet.next()) {
                // Retrieve and assign the balance
                balance = resultSet.getInt("balance");
                System.out.println("Balance: " + balance);
            } else {
                // Handle the case where no records are found
                System.out.println("No balance found for user ID: " + id);
                // You can throw an exception or return a default value here
                // For example, you can return 0:
                // balance = 0;
            }
        }
    
        return balance;
    }
    
    
    public static Long getCardNumber(Connection connection, int id) throws SQLException
{
    Long CardNumber = -1L;
        String getCardNumber= "SELECT card_number FROM account where user_id = ?";

        connection.setAutoCommit(false);

           try (PreparedStatement preparedStatement = connection.prepareStatement(getCardNumber)) {
            preparedStatement.setInt(1, id);
            
            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Check if there is at least one row in the result set
            if (resultSet.next()) {
                // Retrieve and assign the balance
                CardNumber = resultSet.getLong("card_number");
                System.out.println("Card Number: " + CardNumber);
            }
        }

        return CardNumber;
}

public static void InsertIntoTransactionTable(Connection connection, int id, double amount, String desc, String transactionType, int accountid) throws SQLException
{
    String insertQuery = "INSERT INTO bankinginfo.transaction (transaction_type, amount, description, user_id, account_id) VALUES (?,?,?,?,?)";

    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
        preparedStatement.setString(1, transactionType);
        preparedStatement.setDouble(2, amount);
        preparedStatement.setString(3, desc);
        preparedStatement.setInt(4, id);
        preparedStatement.setInt(5, accountid);

        preparedStatement.executeUpdate();
    }

}

public static List<Transaction> getTransactionMainPage(Connection connection, int userId) throws SQLException {
    List<Transaction> transactions = new ArrayList<>();

    // Your SQL query to retrieve transaction data here
    String sqlQuery = "SELECT amount, description, transaction_type FROM transaction WHERE user_id = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
        preparedStatement.setInt(1, userId);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                double amount = resultSet.getDouble("amount");
                String description = resultSet.getString("description");
                String transactionType = resultSet.getString("transaction_type");

                // Create a Transaction object and add it to the list
                System.out.println(amount);
    

                Transaction transaction = new Transaction(amount, description, transactionType);
                transactions.add(transaction);
                System.out.println(transaction.getTransactionType());
                System.out.println(transaction.getAmount());
                System.out.println(transaction.getDescription());

            }
        }
    }

    return transactions;
}














    
    // You should implement a method to verify the password securely, for example using a hashing library
    private static boolean verifyPassword(String plainPassword, String storedHashedPassword) {
        

        return true;
    }
    
}
