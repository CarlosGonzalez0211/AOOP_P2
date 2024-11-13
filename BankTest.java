import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
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
    private ByteArrayOutputStream outputStream;  // Instance variable to capture System.out
    private PrintStream originalSystemOut;       // To restore original System.out

    @BeforeEach
    public void setUp() {
        // Initialize the HashMaps
        idMap = new HashMap<>();
        nameMap = new HashMap<>();

        // Create test data for customer and recipient
        Person testPerson = new Person("198", "Daniela", "Castro", "2003-Sep-19", "Random Address", "915667999");
        Checking checkingAccount = new Checking(1234, 1000.0, testPerson);
        Saving savingsAccount = new Saving(5678, 2000.0, testPerson);
        Credit creditAccount = new Credit(9101, 3000.0, 5000.0, testPerson);
        Account[] accounts = {checkingAccount, savingsAccount, creditAccount};
        customerTest = new Customer("198", "Daniela", "Castro", "2003-Sep-19", "Random Address", "915667999", accounts);

        Person recipientPerson = new Person("199", "Aylin", "Rodriguez", "2003-Oct-10", "Another Address", "915668000");
        Checking recipientCheckingAccount = new Checking(2345, 500.0, recipientPerson);
        Saving recipientSavingAccount = new Saving(6789, 1500.0, recipientPerson);
        Account[] recipientAccounts = {recipientCheckingAccount, recipientSavingAccount};
        recipientTest = new Customer("199", "Aylin", "Rodriguez", "2003-Oct-10", "Another Address", "915668000", recipientAccounts);

        // Populate the HashMaps
        idMap.put(customerTest.getIdNumber(), customerTest);
        idMap.put(recipientTest.getIdNumber(), recipientTest);
        nameMap.put("Daniela Castro", customerTest);
        nameMap.put("Aylin Rodriguez", recipientTest);

        // Capture System.out output to verify printed messages
        outputStream = new ByteArrayOutputStream();
        originalSystemOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testInquireBalance() {
        // Verify account details
        assertEquals("Account number: 1234\nAccount type: Checking\nAccount current balance: 1000.0",
                customerTest.getCheckingAccount().toString());

        assertEquals("Account number: 5678\nAccount type: Savings\nAccount current balance: 2000.0",
                customerTest.getSavingAccount().toString());

        assertEquals("Account number: 9101\nAccount type: Credit\nAccount current balance: 3000.0\nMaximum credit: 5000.0",
                customerTest.getCreditAccount().toString());

        // Call the inquireBalance method to ensure it works without exceptions
        Customer.inquireBalance(customerTest);
    }

    @Test
    public void testMakeWithdrawalFromCheckingAccount() {
        // Simulate input for selecting Checking account and withdrawing $200
        String input = "1\n200.0\n"; // '1' selects Checking, '200.0' as withdrawal amount
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        // Call makeWithdrawal to test the operation
        customerTest.makeWithdrawal(customerTest, scanner);

        // Check if the balance was updated correctly
        assertEquals(800.0, customerTest.getCheckingAccount().getBalance(),
                "Expected balance to be 800.0 after withdrawing 200.0 from Checking account.");

        // Verify output
        String output = outputStream.toString();
        assertTrue(output.contains("New Checking account balance: $800.0"),
                "Expected to display updated Checking balance.");
    }

    @Test
    public void testMakeTransfer() {
        // Simulate input for transferring from Checking to Saving
        String input = "1\n2\n500.0\n"; // '1' selects Checking, '2' selects Saving, '500.0' as transfer amount
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        // Call makeTransfer to test the operation
        customerTest.makeTransfer(customerTest, scanner);

        // Verify balances after transfer
        assertEquals(500.0, customerTest.getCheckingAccount().getBalance(),
                "Expected Checking account balance to be 500.0 after transferring 500.0.");
        assertEquals(2500.0, customerTest.getSavingAccount().getBalance(),
                "Expected Saving account balance to be 2500.0 after receiving transfer of 500.0.");

        // Verify output
        String output = outputStream.toString();
        System.out.println("Output: " + output);  // Debug: Print the actual output

        // Check the updated balance messages
        assertTrue(output.contains("Transfer successful!"),
                "Expected transfer success message.");
        assertTrue(output.contains("New balance for Checking account: $500.0"),
                "Expected to display updated Checking balance.");
        assertTrue(output.contains("New balance for Savings account: $2500.0"),
                "Expected to display updated Savings balance.");
    }

    @Test
    public void testMakeDeposit() {
        // Simulate input for depositing into Saving
        String input = "2\n500.0\n"; // '2' selects Saving, '500.0' as deposit amount
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Scanner scanner = new Scanner(System.in);

        // Call makeDeposit to test the operation
        customerTest.makeDeposit(customerTest, scanner);

        // Verify balance after deposit
        assertEquals(2500.0, customerTest.getSavingAccount().getBalance(),
                "Expected Savings account balance to be 2500.0 after depositing 500.0.");

        // Verify output
        String output = outputStream.toString();
        System.out.println("Output: " + output);  // Debug: Print the actual output

        // Check the updated balance messages
        assertTrue(output.contains("Deposit successful!"),
                "Expected deposit success message.");
        assertTrue(output.contains("New balance for Savings account: $2500.0"),
                "Expected to display updated Savings balance.");
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalSystemOut);  // Restore original System.out
    }
}