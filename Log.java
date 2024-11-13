import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Log class provides the functionality to log messages to both the console and a log file when doing a customer performs bank transactions.
 * The log entries are written to a specified file, with each entry appended as a new line.
 * 
 * @author Daniela Castro Enriquez
 * @author Carlos Gonzalez
 * @author Aylin Rodriguez
 * 
 */
public class Log {

    /**
     * The path to the log file where messages are recorded.
     */
    public static final String LOG_FILE = "log.txt";

    /**
     * A list of general transaction log entries.
     */
    public static List<String> transactions = new ArrayList<>();

    /**
     * A list of accounts used for transaction tracking.
     */
    public static List<Account> accounts = new ArrayList<>();

    /**
     * A map storing user-specific transactions, where the key is the user's name and
     * the value is a list of transactions associated with that user.
     */
    public static Map<String, List<String>> userTransactions = new HashMap<>(); // Store user-specific transactions

    /**
     * Retrieves the list of general transactions.
     *
     * @return a list of general transactions
     */
    public static List<String> getTransactions(){
        return transactions;
    }

    /**
     * Logs a user-specific transaction message. If no transactions have been logged
     * for the user, initializes a new transaction list for that user.
     *
     * @param userName   the name of the user whose transaction is being logged
     * @param logMessage the transaction message to be logged
     */
    public static void logUserTransaction(String userName, String logMessage) {
        if (logMessage == null || logMessage.isEmpty()) {
            System.out.println("Empty log message for user " + userName + ". Nothing to log.");
            return;
        }

        // Initialize the transaction list for the user if not already done
        userTransactions.putIfAbsent(userName, new ArrayList<>());

        // Add the log message to the user's transaction list
        userTransactions.get(userName).add(logMessage);

        // Print the log message to the console (optional)
        System.out.println("Transaction logged for " + userName + ": " + logMessage);

        // Optionally, write to the general log file
        logEntries(logMessage);
    }

    /**
     * Logs a specified message to the console and appends it to the log file.
     * If the log message is null or empty, it outputs a message to the console indicating
     * that there is nothing to log.
     *
     * @param logMessage the message to be logged 
     */
    public static void logEntries(String logMessage) {
        if (logMessage == null || logMessage.isEmpty()) {
            System.out.println("Empty log message. Nothing to log.");
            return;
        }

       
        System.out.println(logMessage);

        try (BufferedWriter textWriter = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            textWriter.write(logMessage);
            textWriter.newLine();  // Move to the next line
        } catch (IOException e) {
            System.out.println("Failed to write to log file: " + e.getMessage());
        }
    }

    /**
     * Creates a user-specific transaction file containing their transactions and account balances.
     * The file is named according to the user's name.
     *
     * @param userName      the name of the user
     * @param userAccounts  the list of accounts for the user
     * @param transactions  the list of transactions to write
     */
    public static void createUserTransactionFile(String userName, List<Account> userAccounts, List<String> transactions) {
        // File name for the user-specific transaction log
        List<String> transactionsList = userTransactions.getOrDefault(userName, new ArrayList<>());

        String fileName = userName + "_TransactionReport.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write the date of the statement
            writer.write("Date: " + LocalDate.now());
            writer.newLine();
            writer.newLine();

            // Write account information and ending balances
            writer.write("Final Account Balances:");
            writer.newLine();
            for (Account account : userAccounts) {
                writer.write("Account " + account.getAccountType() + " (" + account.getAccountNum() + "): $" + String.format("%.2f", account.getBalance()));
                writer.newLine();
            }
            writer.newLine();

            // Write all transactions
            writer.write("Transactions:");
            writer.newLine();
            for (String transaction : transactionsList) {
                writer.write(transaction);
                writer.newLine();
            }

            System.out.println("User transaction file created successfully for " + userName);
        } catch (IOException e) {
            System.out.println("Error writing user transaction file for " + userName + ": " + e.getMessage());
        }

        
    }

    /**
     * Retrieves a list of transactions for the specified user.
     *
     * @param userName the name of the user
     * @return a list of transactions for the specified user, or an empty list if none are found
     */
    public static List<String> getUserTransactions(String userName) {
        return userTransactions.getOrDefault(userName, new ArrayList<>());
    }

    /**
     * Creates and writes a user-specific transactions file. This file contains a header
     * and a list of transactions made by the user. The file is overwritten each time.
     *
     * @param userName     the name of the user
     * @param transactions the list of transactions to write
     */
    public static void createUserTransactionsFile(String userName, List<String> transactions) {
        // File name for the user-specific transactions file
        String fileName = userName + "_Transactions.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) { // Overwrite mode
            writer.write("--- Transactions for " + userName + " ---");
            writer.newLine();
            writer.newLine();

            // Write all transactions
            if (transactions.isEmpty()) {
                writer.write("No transactions found.");
            } else {
                for (String transaction : transactions) {
                    writer.write(transaction);
                    writer.newLine();
                }
            }

            System.out.println("Transactions file created successfully for " + userName);
        } catch (IOException e) {
            System.out.println("Error writing transactions file for " + userName + ": " + e.getMessage());
        }
    }

   
}
