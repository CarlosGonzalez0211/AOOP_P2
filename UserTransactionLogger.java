import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class UserTransactionLogger {

    /**
     * Logs user-specific transaction information to a file.
     *
     * @param userName          The name of the user
     * @param accountInfo       The account information of the user
     * @param startingBalance   The starting balance for the user at the beginning of the session
     * @param endingBalance     The ending balance for the user
     * @param transactions      A list of transactions for the user
     */
    public static void createUserTransactionFile(
            String userName, 
            String accountInfo, 
            double startingBalance, 
            double endingBalance, 
            List<String> transactions
    ) {
        // File name for the user-specific transaction log
        String fileName = userName + "_TransactionReport.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write the date of the statement

            try (BufferedWriter textWriter = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
                textWriter.write(logMessage);
                textWriter.newLine();  // Move to the next line
            } 
            writer.write("Date: " + LocalDate.now());
            writer.newLine();
            writer.newLine();

            // Write the starting balance
            writer.write("Starting Balance: $" + String.format("%.2f", startingBalance));
            writer.newLine();

            // Write the ending balance
            writer.write("Ending Balance: $" + String.format("%.2f", endingBalance));
            writer.newLine();
            writer.newLine();

            // Write all transactions
            writer.write("Transactions:");
            writer.newLine();

            System.out.println("User transaction file created successfully for " + userName);
        } catch (IOException e) {
            System.out.println("Error writing user transaction file for " + userName + ": " + e.getMessage());
        }
    }
}