import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

public class BankTest {
    private HashMap<String, Customer> idMap;
    private HashMap<String, Customer> nameMap;
    private Customer customerTest;
    private PopulationHashmap customerMap;

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

        // Populate the HashMaps
        String fullName = "Daniela" + " " + "Castro";
        idMap.put(customerTest.getIdNumber(), customerTest);
        nameMap.put(fullName, customerTest);
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

}
