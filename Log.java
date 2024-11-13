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
    public static List<String> transactions = new ArrayList<>();
    public static List<Account> accounts = new ArrayList<>();
    public static Map<String, List<String>> userTransactions = new HashMap<>(); // Store user-specific transactions

    public static List<String> getTransactions(){
        return transactions;
    }

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

        // Print the log message to the console
        System.out.println(logMessage);

        // Write the log message to the log file
        try (BufferedWriter textWriter = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            textWriter.write(logMessage);
            textWriter.newLine();  // Move to the next line
        } catch (IOException e) {
            System.out.println("Failed to write to log file: " + e.getMessage());
        }
    }

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

    public static List<String> getUserTransactions(String userName) {
        return userTransactions.getOrDefault(userName, new ArrayList<>());
    }

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
