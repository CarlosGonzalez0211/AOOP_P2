import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
}
