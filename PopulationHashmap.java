import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * The PopulationHashmap class is responsible for reading customer data from a CSV file
 * and populating two HashMaps: one using the customer's identification number as the key
 * and another using the customer's full name as the key.
 * 
 * @author Daniela Castro Enriquez
 * @author Carlos Gonzalez
 * @author Aylin Rodriguez
 * 
 */
public class PopulationHashmap {

    /**
     * This mathod reads customer information from a specified CSV file and populates two HashMaps:
     * one keyed by identification number and the other by full nam (of the person).
     *
     * @return an array of HashMaps where the first HashMap uses identification numbers as keys
     *         and the second HashMap uses full names as keys.
     */
    public static HashMap<String, Customer>[] readFile() {
        HashMap<String, Customer> idMap = new HashMap<>();
        HashMap<String, Customer> nameMap = new HashMap<>();

        try {
            Scanner informationIndeces = new Scanner(new File("C:\\Users\\danic\\DocumentsAsus\\Part2_AOOP\\AOOP_ProjectPart2\\BankUsers.csv"));

            String headers = informationIndeces.nextLine();
            String[] titles = headers.split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

            // Retrieve the index of each relevant column header
            int idNumberIdx = findIndex(titles, "Identification Number");
            int firstNameIdx = findIndex(titles, "First Name");
            int lastNameIdx = findIndex(titles, "Last Name");
            int dateOfBirthIdx = findIndex(titles, "Date of Birth");
            int addressIdx = findIndex(titles, "Address");
            int phoneNumberIdx = findIndex(titles, "Phone Number");
            int savingsAccountNumberIdx = findIndex(titles, "Savings Account Number");
            int savingsStartingBalanceIdx = findIndex(titles, "Savings Starting Balance");
            int checkingAccountNumberIdx = findIndex(titles, "Checking Account Number");
            int checkingStartingBalanceIdx = findIndex(titles, "Checking Starting Balance");
            int creditAccountNumberIdx = findIndex(titles, "Credit Account Number");
            int creditStartingBalanceIdx = findIndex(titles, "Credit Starting Balance");
            int creditMaxIdx = findIndex(titles, "Credit Max");

            // Process each line of the CSV file
            while (informationIndeces.hasNextLine()) {
                String[] userInformation = informationIndeces.nextLine().split(",(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

                String idNumber = userInformation[idNumberIdx];
                String firstName = userInformation[firstNameIdx];
                String lastName = userInformation[lastNameIdx];
                String fullName = firstName + " " + lastName; // Add space between first and last name
                String dateOfBirth = userInformation[dateOfBirthIdx];
                String address = userInformation[addressIdx];
                String phoneNumber = userInformation[phoneNumberIdx];
                int savingsAccountNumber = Integer.parseInt(userInformation[savingsAccountNumberIdx]);
                double savingsStartingBalance = Double.parseDouble(userInformation[savingsStartingBalanceIdx]);
                int checkingAccountNumber = Integer.parseInt(userInformation[checkingAccountNumberIdx]);
                double checkingStartingBalance = Double.parseDouble(userInformation[checkingStartingBalanceIdx]);
                int creditAccountNumber = Integer.parseInt(userInformation[creditAccountNumberIdx]);
                double creditStartingBalance = Double.parseDouble(userInformation[creditStartingBalanceIdx]);
                double creditMax = Double.parseDouble(userInformation[creditMaxIdx]);

                // Create instances of Person, Checking, Saving, Credit, and Customer
                Person basicInformationUser = new Person(idNumber, firstName, lastName, dateOfBirth, address, phoneNumber);
                Checking checkingAccount = new Checking(checkingAccountNumber, checkingStartingBalance, basicInformationUser);
                Saving savingAccount = new Saving(savingsAccountNumber, savingsStartingBalance, basicInformationUser);
                Credit creditAccount = new Credit(creditAccountNumber, creditStartingBalance, creditMax, basicInformationUser);
                Account[] userAccounts = {checkingAccount, savingAccount, creditAccount};
                Customer bankCustomer = new Customer(idNumber, firstName, lastName, dateOfBirth, address, phoneNumber, userAccounts);

                // Populate both HashMaps
                idMap.put(idNumber, bankCustomer);       // Use ID as key
                nameMap.put(fullName, bankCustomer);     // Use full name as key
            }

        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        //Print the populated HashMaps
        printHashMap("ID Map", idMap);
        printHashMap("Name Map", nameMap);

        // Return an array of HashMap<String, Customer>
        return new HashMap[]{idMap, nameMap};
    }

    /**
     * This method writes the updated customer data to a CSV file.
     *
     * @param fileName    the name of the CSV file to write to
     * @param customerMap the HashMap containing customer data
     */
    public void writeToCSV(String fileName, HashMap<String, Customer> customerMap) {
        try (FileWriter writer = new FileWriter(fileName)) {
            // Write the CSV header
            writer.write(
                    "Identification Number,First Name,Last Name,Date of Birth,Address,Phone Number," +
                            "Checking Account Number,Checking Starting Balance," +
                            "Savings Account Number,Savings Starting Balance," +
                            "Credit Account Number,Credit Max,Credit Starting Balance\n"
            );

            // Write each customer's data
            for (Customer customer : customerMap.values()) {
                // Retrieve the accounts associated with the customer
                Checking checking = customer.getCheckingAccount();
                Saving savings = customer.getSavingAccount();
                Credit credit = customer.getCreditAccount();

                // Build a row with all necessary details
                String row =
                        customer.getIdNumber() + "," +
                                customer.getFirstName() + "," +
                                customer.getLastName() + "," +
                                customer.getDateOfBirth() + "," +
                                customer.getAddress() + "," +
                                customer.getPhoneNumber() + "," +
                                checking.getAccountNum() + "," + checking.getBalance() + "," +
                                savings.getAccountNum() + "," + savings.getBalance() + "," +
                                credit.getAccountNum() + "," + credit.getCreditMax() + "," + credit.getBalance() + "\n";

                writer.write(row);
            }

            System.out.println("CSV file saved successfully: " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    /**
     * This method will find the index of a specific header title in an array of header titles.
     *
     * @param headers an array of header titles
     * @param title   the title to find
     * @return the index of the specified title, or -1 if not found
     */
    private static int findIndex(String[] headers, String title) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equals(title)) {
                return i;
            }
        }
        return -1; // No title found
    }

    /**
     * This method prints the contents of a given HashMap.
     *
     * @param mapName the name of the HashMap
     * @param map     the HashMap to print
     */
    public static void printHashMap(String mapName, HashMap<String, Customer> map) {
        System.out.println(mapName + ":");

        if (map.isEmpty()) {
            System.out.println("The HashMap is empty.");
            return;
        }

        for (Map.Entry<String, Customer> entry : map.entrySet()) {
            String key = entry.getKey();
            Customer customer = entry.getValue();

            System.out.println("Key: " + key);
            System.out.println("Name: " + customer.getFirstName() + " " + customer.getLastName());
            System.out.println("Date of Birth: " + customer.getDateOfBirth());
            System.out.println("Address: " + customer.getAddress());
            System.out.println("Phone Number: " + customer.getPhoneNumber());

            System.out.println("Accounts:");
            for (Account account : customer.getAccounts()) {
                System.out.println("  - " + account.toString());
            }
            System.out.println("------------------------------------");
        }
    }

    /**
     * The main method to run the PopulationHashmap program.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // Create an instance of PopulationHashmap
        readFile();
    }
}
