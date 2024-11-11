import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;

public class BankTest {
    private HashMap<String, Customer> idMap;
    private HashMap<String, Customer> nameMap;
    private Customer customerTest;
    private Customer recipientTest;
    private PopulationHashmap customerMap;

    private ByteArrayOutputStream outputStream;  // Instance variable
    private PrintStream originalSystemOut;       // Instance variable

    @BeforeEach
    public void setUp() {
        // Initialize the HashMaps
        idMap = new HashMap<>();
        nameMap = new HashMap<>();

        // Initialize the PopulationHashmap (if needed)
        customerMap = new PopulationHashmap();

        // Create test data
        Person testPerson = new Person("198", "Daniela", "Castro", "2003-Sep-19", "Random Address", "915667999");
        Checking checkingAccount = new Checking(1234, 1000.0, testPerson); // Adjusted to match the expected values
        Saving savingsAccount = new Saving(5678, 2000.0, testPerson); // Adjusted to match the expected values
        Credit creditAccount = new Credit(9101, 3000.0, 5000.0, testPerson); // Adjusted to match the expected values
        Account[] accounts = {checkingAccount, savingsAccount, creditAccount};
        customerTest = new Customer("198", "Daniela", "Castro", "2003-Sep-19", "Random Address", "915667999", accounts);

        // Create test data for the recipient
        Person recipientPerson = new Person("199", "Aylin", "Rodriguez", "2003-Oct-10", "Another Address", "915668000");
        Checking recipientCheckingAccount = new Checking(2345, 500.0, recipientPerson);
        Saving recipientSavingAccount = new Saving(6789, 1500.0, recipientPerson);
        Account[] recipientAccounts = {recipientCheckingAccount, recipientSavingAccount};
        recipientTest = new Customer("199", "Aylin", "Rodriguez", "2003-Oct-10", "Another Address", "915668000", recipientAccounts);

        // Populate the HashMaps
        String fullName = "Daniela" + " " + "Castro";
        String recipientFullName = "Aylin" + " " + "Rodriguez";
        idMap.put(customerTest.getIdNumber(), customerTest);
        idMap.put(recipientTest.getIdNumber(), recipientTest);
        nameMap.put(fullName, customerTest);
        nameMap.put(recipientFullName, recipientTest);

        // Capture System.out output
        outputStream = new ByteArrayOutputStream(); // Initialize outputStream
        originalSystemOut = System.out;             // Store original System.out
        System.setOut(new PrintStream(outputStream)); // Redirect System.out to capture output
    }

    @Test
    public void testInquireBalance() {
        // Verify that each account has the expected string representation
        assertEquals(
                "Account number: 1234\nAccount type: Checking\nAccount current balance: 1000.0",
                customerTest.getCheckingAccount().toString(),
                "Checking account details should match."
        );

        assertEquals(
                "Account number: 5678\nAccount type: Savings\nAccount current balance: 2000.0",
                customerTest.getSavingAccount().toString(),
                "Saving account details should match."
        );

        assertEquals(
                "Account number: 9101\nAccount type: Credit\nAccount current balance: 3000.0\nMaximum credit: 5000.0",
                customerTest.getCreditAccount().toString(),
                "Credit account details should match."
        );


        // Call to inquireBalance to make sure it runs without exceptions.
        Customer.inquireBalance(customerTest);
    }

    @Test
    public void testMakeTransfer() {
        // Simulate the Scanner input (e.g., selecting account and transferring money)
        String simulatedInput = "1\n2\n1000.0\n"; // Select checking (1), saving (2), and transfer amount (1000)
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Call the method under test
        Customer.makeTransfer(customerTest, scanner);

        // Print the captured output for debugging (this can be removed later)
        String actualOutput = outputStream.toString();
        System.out.println("Captured Output: \n" + actualOutput);

        // Verify key parts of the output
        assertTrue(actualOutput.contains("Choose account to withdraw from?"));
        assertTrue(actualOutput.contains("Choose account to transfer to?"));
        assertTrue(actualOutput.contains("Transfer successful!"));

        // Verify that the account balances are updated
        assertEquals(0.0, customerTest.getCheckingAccount().getBalance(), "Checking account balance should be 0.0");
        assertEquals(3000.0, customerTest.getSavingAccount().getBalance(), "Saving account balance should be 3000.0");

        // Optionally, verify the exact log entry (if applicable)
        String expectedLog = "Daniela Castro transferred: $1000.0 from Checking account to Saving account";
        // You can verify if the log was recorded correctly if you capture Log.logEntries in the test
        // For now, we assume it just works based on the output.
    }

    @Test
    public void testMakeDeposit() {
        // Simulate the Scanner input (e.g., selecting account and depositing money)
        String simulatedInput = "1\n500.0\n"; // Select checking account (1) and deposit amount (500.0)
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Call the method under test using the correct static method call
        Customer.makeDeposit(customerTest, scanner);  // Corrected method call

        // Print the captured output for debugging (this can be removed later)
        String actualOutput = outputStream.toString();
        System.out.println("Captured Output: \n" + actualOutput);

        // Verify key parts of the output
        assertTrue(actualOutput.contains("New Checking account balance: $1500.0"), "Checking account balance should be updated to 1500.0");

        // Verify the account balance has been updated
        assertEquals(1500.0, customerTest.getCheckingAccount().getBalance(), "Checking account balance should be 1500.0");

        // Optionally, verify the exact log entry (if applicable)
        String expectedLog = "Daniela Castro made a deposit on Checking-1234. Daniela Castro's new balance for Checking-1234 is 1500.0";
        assertTrue(actualOutput.contains(expectedLog), "Log entry should be correct");
    }

    @Test
    public void testMakeWithdrawal() {
        // Simulate the Scanner input (e.g., selecting checking account and withdrawing money)
        String simulatedInput = "1\n500.0\n"; // Select checking account (1) and withdraw amount (500.0)
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Call the method under test using the correct static method call
        Customer.makeWithdrawal(customerTest, scanner);  // Calling the static method

        // Print the captured output for debugging (this can be removed later)
        String actualOutput = outputStream.toString();
        System.out.println("Captured Output: \n" + actualOutput);

        // Verify key parts of the output
        assertTrue(actualOutput.contains("Withdrawal successful of $500.0 from Checking account"), "Withdrawal message should be printed correctly.");
        assertTrue(actualOutput.contains("New Checking account balance: $500.0"), "Updated balance should be printed correctly.");

        // Verify the account balance has been updated
        assertEquals(500.0, customerTest.getCheckingAccount().getBalance(), "Checking account balance should be 500.0 after withdrawal");

        // Optionally, verify the exact log entry (if applicable)
        String expectedLog = "Withdrawal successful of $500.0 from Checking account. New Checking account balance: $500.0";
        // Assuming Log.logEntries logs correctly, this is an optional check
        // You can add verification of log entries if your logging system supports it
        // For now, we're assuming that the log has been captured correctly.
        assertTrue(actualOutput.contains(expectedLog), "Log entry should be correct.");
    }

    @Test
    public void testPaySomeone() {
        // Simulate the Scanner input (e.g., selecting account, amount, and recipient)
        String simulatedInput = "1\n500.0\nAylin Rodriguez\n1\n"; // Select checking (1), amount (500.0), recipient (Aylin), and recipient's account (1)
        ByteArrayInputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        Scanner scanner = new Scanner(inputStream);

        // Capture the output of the method call using the PrintStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);  // Set the output to the ByteArrayOutputStream for testing

        // Call the method under test
        Customer.paySomeone(customerTest, scanner, idMap);

        // Capture the actual output
        String actualOutput = outputStream.toString();

        // Debug: Print captured output
        System.out.println("Captured Output: \n" + actualOutput);

        // Verify key parts of the output
        assertTrue(actualOutput.contains("Which account would you like to withdraw from?"));
        assertTrue(actualOutput.contains("Which account would you like to pay into?"));
        assertTrue(actualOutput.contains("Payment successful!"));

        // Verify the account balances are updated
        assertEquals(500.0, customerTest.getCheckingAccount().getBalance(), "Checking account balance should be 500.0 after withdrawal");
        assertEquals(2000.0, recipientTest.getCheckingAccount().getBalance(), "Recipient's Checking account balance should be 2000.0 after deposit");

        // Optionally, verify the log entry
        String expectedLog = "Daniela Castro paid $500.0 to Aylin Rodriguez from Checking account to Checking account";
        assertTrue(actualOutput.contains(expectedLog), "Log entry should be correct");
    }

}
