import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * The RunBank class serves as the entry point for the El Paso Miners Bank application.
 * enables users to perform banking operations.
 *
 * @author Daniela Castro Enriquez
 * @author Carlos Gonzalez
 * @author Aylin Rodriguez
 *
 * //Random intellij comment
 */
public class RunBank {
    /**
     * This method is the starting point for the El Paso Miners Bank application.
     *
     * The system allows users to perform various banking tasks, including:
     * <ul>
     *   <li>Checking balances</li>
     *   <li>Depositing funds into an account</li>
     *   <li>Withdrawing funds from an account</li>
     *   <li>Transferring funds between accounts</li>
     *   <li>Making payments to other users</li>
     * </ul>
     * Users can log in as either individuals or bank managers. A customer database
     * is loaded from a CSV file, and the program manages user input and provides
     * responses for each operation. The program allows the user to exit at any time.
     * Once the user exits, the program creates a new CSV file with the updated information.
     *
     * @param args command-line arguments (not used)
     */


    public static HashMap<String, Customer>[] customersMap = PopulationHashmap.readFile();
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("WELCOME TO EL PASO MINERS BANK");

            boolean validation = false;

            // Repeat until a valid user type or exit command is provided
            while (!validation) {
                try {
                    /**
                     * Prompt the user for their role type (individual, manager, or exit).
                     * Switch based on user input and call the appropriate handling method.
                     */
                    System.out.print("Are you an individual, a bank manager, or do you want to exit? (Type 'individual', 'manager', or 'exit'): ");
                    String userType = scanner.nextLine().trim().toLowerCase();

                    switch (userType) {
                        case "individual" -> {
                            /**
                             * Handles individual user actions. Calls handleIndividualUser to
                             * do balance checks, deposits, withdrawals, and transfers.
                             *
                             * @param scanner the Scanner object to get user inputs
                             * @param customersMap array of HashMaps containing customer data of the complete list of customers
                             * @return true if user is found and actions are completed
                             */
                            boolean userFound = handleIndividualUser(scanner, customersMap);
                            if (userFound) {
                                validation = true;
                            } else {
                                System.out.println("Input a valid user.");
                            }
                        }
                        case "manager" -> {
                            /**
                             * This case handles tasks that only managers can do such as viewing and managing
                             * accounts. Calls handleBankManager method to specifically handle manager actions.
                             *
                             * @param scanner the Scanner object for user input
                             * @param customersMap array of HashMaps containing customer data
                             */
                            handleBankManager(scanner, customersMap);
                            validation = true;
                        }
                        case "exit" -> {
                            // Exit message and termination of program
                            System.out.println("Thank you for using El Paso Miners Bank. Goodbye!");
                            validation = true;
                        }
                        default -> System.out.println("Invalid input. Please type 'individual', 'manager', or 'exit'.");
                    }
                } catch (NoSuchElementException e) {
                    // Handles cases of incorrect input type
                    System.out.println("Input error. Please try again.");
                } catch (Exception e) {
                    // Catches unexpected errors to prevent program crash
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            // Error handling for scanner closure
            System.out.println("Error closing the scanner: " + e.getMessage());
        }
    }

    /**
     * Displays a menu for individual customer options in the banking system.
     * This method allows the user to check their balance, make deposits,
     * withdraw from accounts, make transfers, make payments to other users,
     * or exit the banking system if they are finished.
     *
     * <p>
     * The method continuously prompts the user for a choice until they choose
     * to exit. It handles invalid inputs, prompting the user to
     * re-enter their choice when necessary.
     * </p>
     *
     * @param customer      The Customer object representing the user
     *                      accessing the individual user menu.
     * @param customersMap  An array of HashMaps where each HashMap contains
     *                      customer information, allowing for lookup and
     *                      management of customer accounts.
     *
     * @throws InputMismatchException if the input provided by the user is not
     *                                an integer, or if an error occurs during
     *                                input handling.
     */
    private static void individualUserMenu(Customer customer, HashMap<String, Customer>[] customersMap) {

        Scanner scanner = new Scanner(System.in);
        // PopulationHashmap customerMap = new PopulationHashmap();

        while (true) {
            try {
                System.out.println("Choose an option:");
                System.out.println("1. Check balance");
                System.out.println("2. Make deposit");
                System.out.println("3. Withdraw from an account");
                System.out.println("4. Make a transfer");
                System.out.println("5. Make payment to a user");
                System.out.println("6. Exit bank");

                System.out.print("Enter your choice: ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number between 1 and 6.");
                    scanner.next();  // Clear invalid input
                    continue;  // Restart the loop
                }

                int userChoice = scanner.nextInt();


                if (userChoice < 1 || userChoice > 6) {
                    System.out.println("Invalid choice. Please choose a number between 1 and 6.");
                    continue;
                }
                //Change to 
                if (userChoice == 6) {
                    //
                    PopulationHashmap.writeToCSV("id_map.csv", customersMap[1]);
                    List<Account> userAccounts = Arrays.asList(customer.getAccounts());
                    List<String> listOfTransactions = Log.getTransactions();
                    String name = customer.getFirstName() + " " + customer.getLastName();
                    Log.createUserTransactionFile(name, userAccounts, listOfTransactions);
                    //PopulationHashmap.writebllabal
                    System.out.println("Thank you for visiting us!");


                    break;  // Exit the loop
                }

                handleOptionsMenu(userChoice, customer, scanner, customersMap);
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again.");

            }
        }
    }

    /**
     * This method handles the user's menu choice for different banking operations.
     * Based on the user's selection, this method selects the action
     * to the appropriate method to execute the banking operation.
     *
     * <p>
     * The method supports the following operations:
     * <ul>
     * <li>1: Inquire balance</li>
     * <li>2: Make a deposit</li>
     * <li>3: Withdraw from an account</li>
     * <li>4: Make a transfer</li>
     * <li>5: Pay a person</li>
     * </ul>
     * </p>
     *
     * @param userChoice   An integer representing the user's choice of
     *                     operation, which determines which banking action
     *                     to perform.
     * @param customer     The customer representing the user
     *                     for whom the operation will be executed.
     * @param scanner      A scanner for reading user input
     *                     from the console.
     * @param customersMap An array of HashMaps where each HashMap contains
     *                     customer information, used for performing bank
     *                     operations 
     */
    public static void handleOptionsMenu(int userChoice, Customer customer, Scanner scanner, HashMap<String, Customer>[] customersMap) {
        switch(userChoice){
            case 1 -> Customer.inquireBalance(customer);
            case 2 -> Customer.makeDeposit(customer, scanner);
            case 3 -> Customer.makeWithdrawal(customer, scanner);
            case 4 -> Customer.makeTransfer(customer, scanner);
            case 5 -> Customer.paySomeone(customer, scanner, customersMap[1]);
        }
    }

    /**
     * The method handles user input to identify an individual customer and navigate to their menu.
     * This method prompts the user for their name, retrieves the corresponding
     * customer from the provided map, and directs the user to
     * the individual user menu if a customer in the hashmap is found.
     *
     * <p>
     * The method operates in a loop, continuously prompting the user for their
     * name until a valid customer is found. If the customer exists in the
     * hashmap of customer the individual user menu will be displayed. If the
     * customer is not found, the user is asked to input a valid choice
     * </p>
     *
     * @param scanner       A scanner for reading user input
     *                      from the console.
     * @param customersMap  An array of hashmaps where the first
     *                      index contains customer data, mapping customer names
     *                      to their corresponding customers.
     * @return              A boolean indicating whether the customer was
     *                      found and their menu was displayed.
     */
    public static boolean handleIndividualUser(Scanner scanner, HashMap<String, Customer>[] customersMap) {
        while (true) {
            try {
                System.out.print("Enter your name: ");
                String name = scanner.nextLine().trim();

                Customer customer = customersMap[1].get(name);

                if (customer != null) {
                    individualUserMenu(customer, customersMap); // Call the menu directly
                    return true;  // Exit the loop if a valid customer is found
                } else {
                    System.out.println("Customer not found. Please enter a valid customer.");
                }
            } catch (NoSuchElementException e) {
                System.out.println("Input error. Please try again.");
            }
        }
    }



    /**
     * Manages the interactions for a bank manager, allowing them to inquire
     * about customer accounts based on name or account type/number.
     * This method prompts the bank manager for their request and
     * directs the flow accordingly.
     *
     * <p>
     * The method continuously prompts the user to select an inquiry option
     * (either by name or by type/number). Upon receiving a valid input,
     * the corresponding inquiry method is invoked. If the input is invalid,
     * the user is informed and prompted to try again.
     * </p>
     *
     * @param scanner        A scanner for reading user input
     *                       from the console.
     * @param customersMaps  An array of customer hashmaps where each
     *                       entry contains customer data. This allows the
     *                       bank manager to access account information based
     *                       on names or types/numbers.
     * @return              A boolean indicating whether the inquiry process
     *                      was completed successfully. In this implementation,
     *                      it always returns false as a placeholder.
     */
    public static boolean handleBankManager(Scanner scanner, HashMap<String, Customer>[] customersMaps) {
        String userInput;

        while(true){
            System.out.println("A. Inquire account by name.");
            System.out.println("B. Inquire account by type/number.");
            System.out.println("C. Add new bank user.");
            System.out.println("D. Transaction reader.");
            System.out.println("E. Generate bank statement.");

            System.out.print("Please enter your choice:  ");
            userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("A")){
                inquireByName(customersMaps, scanner);
                break;
            }else if(userInput.equalsIgnoreCase("B")){
                inquireByTypeAndNumber(customersMaps, scanner);
                break;
            }else if(userInput.equalsIgnoreCase("C")){
                addNewUser(scanner, customersMaps);
            }else if(userInput.equalsIgnoreCase("D")){
                transactionReader();
                break;
            }else if(userInput.equalsIgnoreCase("E")) {
                generateBankStatement();
            }else{
                System.out.println("Invalid choice. Input a valid option (A/B): ");
            }
        }
        return false;
    }

    /**
     * Allows a bank manager to inquire about a customer's account
     * by entering the customer's full name.
     *
     * <p>
     * The method prompts the bank manager to input a full name. It
     * checks the provided name against the customer records in
     * the given HashMap. If the customer exists, their accounts
     * are printed; if not, the user is informed and prompted to
     * enter a valid name again.
     * </p>
     *
     * @param customersMaps An array of hashmaps
     *                      containing customer data, enabling access
     *                      to customer accounts by name.
     * @param scanner      A customer for reading user
     *                     input from the console.
     */
    public static void inquireByName(HashMap<String, Customer>[] customersMaps, Scanner scanner){
        System.out.println("Whose account would you like to inquire about? (Enter full name): ");
        while (true) {
            String userFullName = scanner.nextLine().trim();
            Customer customer = customersMaps[1].get(userFullName);

            if(customer != null){
                Account[] accounts = customer.getAccounts();

                for(Account account: accounts){
                    System.out.println(account.toString());
                }
            }else{
                System.out.print("User does not exist. Input a valid user: ");
            }

        }
    }

    /**
     * Enables a bank manager to inquire about a specific account
     * by its type (Checking, Savings, or Credit) and account number.
     *
     * <p>
     * This method prompts the bank manager to specify the account type
     * and then enter the corresponding account number. It validates
     * the account type and allows the manager to input the account number.
     * If the account is found, its details are displayed; if not, the
     * user is prompted to retry or cancel the operation.
     * </p>
     *
     * @param customersMaps An array of customer hashmaps 
     *                      containing customer data, allowing for access
     *                      to customer accounts categorized by type and number.
     * @param scanner      A scanner used for reading user
     *                     input from the console.
     */
    public static void inquireByTypeAndNumber(HashMap<String, Customer>[] customersMaps, Scanner scanner){
        System.out.println("What is the account type? (Checking/Savings/Credit)");
        String accountType;

        while (true) {
            accountType = scanner.nextLine().trim();
            if(isValidAccountType(accountType)){
                break;
            }else{
                System.out.println("Invalid account type. Please enter (Checking/Savings/Credit):");
            }
        }
        while (true) {
            System.out.print("Enter account number (or type 'exit' to cancel): ");
            String accountNumber = scanner.nextLine().trim();

            if (accountNumber.equalsIgnoreCase("exit")) {
                System.out.println("Operation cancelled.");
                break;  // Exit the loop if the user types 'exit'.
            }

            try {
                int id = Integer.parseInt(accountNumber);
                Account foundAccount = findAccountByTypeAndId(customersMaps[0], accountType, id);

                if (foundAccount != null) {
                    System.out.println(foundAccount.toString());
                    break;  // Exit the loop after finding a valid account.
                } else {
                    System.out.println("Account not found. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a numeric value for the account number.");
            }
        }
    }

    /**
     * Checks if the given account type is valid.
     *
     * <p>
     * This method verifies if the provided account type matches
     * any of the recognized account types: "Checking", "Savings", or
     * "Credit". The comparison is case-insensitive.
     * </p>
     *
     * @param accountType The account type to validate. It should be a
     *                    string that represents the type of account.
     * @return trueif the account type is valid or false otherwise.
     */
    public static boolean isValidAccountType(String accountType) {
        return (accountType.equalsIgnoreCase("Checking") || accountType.equalsIgnoreCase("Savings") || accountType.equalsIgnoreCase("Credit"));
    }

    /**
     * Finds an account by its type and account number from the provided customer map.
     *
     * <p>
     * This method searches through a map of customers to locate an account
     * that matches the specified account type and account number. It iterates
     * over all customers and their associated accounts to find the first
     * matching account.
     * </p>
     *
     * @param idMap      A customer hashmap mapping customer identifiers to
     *                   customer objects, representing the customer
     *                   database.
     * @param accountType The type of account to search for (e.g., "Checking",
     *                    "Savings", "Credit").
     * @param idAccount   The account number of the account to find.
     * @return The account that matches the specified type and number,
     *         or null if no matching account is found.
     */
    public static Account findAccountByTypeAndId(HashMap<String, Customer> idMap, String accountType, int idAccount){
        for (Customer customer: idMap.values()){
            for (Account account: customer.getAccounts()){
                if((account.getAccountNum()) == idAccount && account.getAccountType().equals(accountType)){
                    return account;
                }
            }
        }
        return null;
    }

    /**
     * Displays the available account types in the menu.
     *
     * <p>
     * This method prints a list of account types that a user can choose from
     * when performing operations related to bank accounts. The options include
     * Checking, Savings, and Credit accounts.
     * </p>
     */
    public static void menuTypesAccount(){
        System.out.println("1. Checking account");
        System.out.println("2. Savings account");
        System.out.println("3. Credit account");
    }

    /**
     * Retrieves the account of a specified type based on the user's choice.
     *
     * @param customer The customer whose accounts are being accessed.
     * @param choice   An integer representing the account type selected by the user:
     *                 1 for Checking account,
     *                 2 for Savings account,
     *                 3 for Credit account.
     * @return The corresponding Account object if a valid choice is made;
     *         otherwise, returns null if the choice is invalid.
     */
    public static Account getAccountByChoice(Customer customer, String choice){
        Account account;
        if ("1".equals(choice)){
            account = customer.getCheckingAccount();
        }else if("2".equals(choice)){
            account = customer.getSavingAccount();
        }else if("3".equals(choice)){
            account = customer.getCreditAccount();
        }else{
            return null;
        }

        return account;
    }

    /**
     * Adds a new user to the system by prompting for their personal information and generating new account numbers.
     * This method creates a unique user ID, collects details such as name, date of birth, address, and phone number,
     * and generates unique account numbers for checking, credit, and savings accounts.
     *
     * @param scanner the Scanner object used to read input from the user
     * @param customersMaps the HashMap that stores the customer data, organized by their unique identifiers
     */
    private static void addNewUser(Scanner scanner, HashMap<String, Customer>[] customersMaps) {
        // Increment lastUserId for the new user (method located in Person class)
        int maxId = Person.getMaxId() + 1;
        String idNumber = String.valueOf(maxId);

        // user information
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
        String dateOfBirth = scanner.nextLine();

        System.out.print("Enter Address: ");
        String address = scanner.nextLine();

        System.out.print("Enter City: ");
        String city = scanner.nextLine();

        System.out.print("Enter State: ");
        String state = scanner.nextLine();

        System.out.print("Enter Zip Code: ");
        String zip = scanner.nextLine();

        System.out.print("Enter Phone Number: ");
        String phoneNumber = scanner.nextLine();

        //System.out.println(maxId); 


        int checkingAccountNumber = generateAccountNumber();
        int creditAccountNumber = generateAccountNumber();
        int savingsAccountNumber = generateAccountNumber();

        Random random = new Random();

        double creditScore = generateRandomCreditScore();
        double checkingBalance = 0.0;
        double savingsBalance = 0.0;


        // Generate random credit limit within the range
        double creditMax = determineCreditLimit(creditScore);

        System.out.println(checkingAccountNumber);
        System.out.println(creditAccountNumber);
        System.out.println(savingsAccountNumber);

        Person newPerson = new Person(idNumber, firstName, lastName, dateOfBirth, address, phoneNumber);
        Checking newChecking = new Checking(checkingAccountNumber, checkingBalance, newPerson);
        Saving newSaving = new Saving(savingsAccountNumber, savingsBalance, newPerson);
        Credit newCredit = new Credit(creditAccountNumber, creditScore, creditMax, newPerson);
        Account[] newAccounts = {newChecking, newSaving, newCredit};

        Customer newCustomer = new Customer(idNumber, firstName, lastName, dateOfBirth, address, phoneNumber, newAccounts);

        customersMaps[0].put(idNumber, newCustomer);
        customersMaps[1].put(firstName + " " + lastName, newCustomer);
        appendCustomerToCSV(newCustomer);

    }

    /**
     * Appends a customer's details and associated accounts to the "BankUsers.csv" file.
     *
     * This method formats the provided customer and their associated accounts into a CSV row
     * and writes the data to a file named "BankUsers.csv". If the file does not exist, it will be created.
     * Each customer's information, including their ID, name, date of birth, address, phone number,
     * and account details (account number, type, and balance), will be written to a new line in the CSV file.
     * The accounts are appended after the basic customer information, with each account's details separated
     * by commas.
     *
     * @param customer The customer whose information and accounts will be written to the CSV file.
     * @throws IOException If an error occurs while writing to the CSV file.
     */
    public static void appendCustomerToCSV(Customer customer) {
        try (FileWriter fw = new FileWriter("BankUsers.csv", true);
             PrintWriter writer = new PrintWriter(fw)) {

            // start the row with customer basic details
            StringBuilder csvRow = new StringBuilder(String.join(",",
                    customer.getIdNumber(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getDateOfBirth(),
                    customer.getAddress(),
                    customer.getPhoneNumber()
            ));

            // loop through each account and append its details to the row, separated by commas
            for (Account account : customer.getAccounts()) {
                csvRow.append(","); // Separate accounts with commas
                csvRow.append(account.getAccountNum()).append(",")
                        .append(account.getAccountType()).append(",")
                        .append(account.getBalance());
            }

            // this would be writing the code all in one line like in the csv file
            writer.println(csvRow.toString());

            System.out.println("Customer with all accounts added to CSV file successfully.");

        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    private static int generateRandomCreditScore() {
        Random random = new Random();
        return 300 + random.nextInt(551); // Credit score between 300 and 850
    }


    private static double determineCreditLimit(double creditScore) {
        Random random = new Random();
        if (creditScore <= 580) {
            return 100 + (random.nextDouble() * 600); // $100 - $699
        } else if (creditScore <= 669) {
            return 700 + (random.nextDouble() * 4300); // $700 - $4999
        } else if (creditScore <= 739) {
            return 5000 + (random.nextDouble() * 2500); // $5000 - $7499
        } else if (creditScore <= 799) {
            return 7500 + (random.nextDouble() * 8500); // $7500 - $15999
        } else {
            return 16000 + (random.nextDouble() * 9000); // $16000 - $25000
        }
    }

    /**
     * Generates a unique account number by randomly selecting a number and ensuring it is not already in use.
     * It uses a set of existing account numbers to check for duplicates and ensures that the generated number is unique.
     *
     * @return a unique account number
     */
    private static int generateAccountNumber(){
        Random random = new Random();
        int number = random.nextInt(9000);


        while(Account.getSetAccountsNumbers().contains(random)){
            System.out.println(number);
            number = random.nextInt(9000);
        }
        Account.addIdToAccountSet(number);
        return number;
    }

    /**
     * Reads transactions from the "Transactions.csv" file and executes the corresponding action for each transaction.
     * <p>
     * The method processes each line of the CSV file, extracting the transaction details (such as sender, receiver, action, amount, etc.)
     * and then performing the specified action (inquire balance, deposit, withdraw, transfer, or pay). It also includes error handling for invalid
     * input formats and ensures that empty fields are treated as null.
     * </p>
     *
     * <p>Possible actions are:</p>
     * <ul>
     *     <li><b>inquires</b>: Inquires about the balance of a specified account.</li>
     *     <li><b>deposits</b>: Deposits a specified amount into a recipient's account.</li>
     *     <li><b>withdraws</b>: Withdraws a specified amount from a sender's account.</li>
     *     <li><b>transfers</b>: Transfers a specified amount from one account to another.</li>
     *     <li><b>pays</b>: Pays a specified amount from one account to another.</li>
     * </ul>
     *
     * <p>If the file cannot be found, a message is printed, and if any amount cannot be parsed as a number, that transaction is skipped.</p>
     *
     * @throws FileNotFoundException If the "Transactions.csv" file is not found.
     * @throws Exception If an unexpected error occurs while processing the transactions.
     */
    private static void transactionReader() {
        try {
            Scanner user = new Scanner(System.in);
            Scanner scanner = new Scanner(new File("Transactions.csv"));
            String infoHeaders = scanner.nextLine();
            String[] headers = infoHeaders.split(",");
            System.out.println(headers.length);

            while (scanner.hasNextLine()){

                String[] informationLine = scanner.nextLine().split(",");
                for (int i = 0; i < informationLine.length; i++) {
                    if (informationLine[i].trim().isEmpty()) {
                        informationLine[i] = null; // Replace with null or any default value
                    }
                }
                String fromFirstName = (informationLine.length > 0) ? informationLine[0] : null;
                String fromLastName = (informationLine.length > 1) ? informationLine[1] : null;
                String fromWhere = (informationLine.length > 2) ? informationLine[2] : null;
                String action = (informationLine.length > 3) ? informationLine[3] : null;
                String toFirstName = (informationLine.length > 4) ? informationLine[4] : null;
                String toLastName = (informationLine.length > 5) ? informationLine[5] : null;
                String toWhere = (informationLine.length > 6) ? informationLine[6] : null;
                double amount = (informationLine.length > 7 && informationLine[7] != null) ? Double.parseDouble(informationLine[7]) : 0.0;

                String fromFullName = fromFirstName + " " + fromLastName;
                String toFullName = toFirstName + " " + toLastName;

                switch (action){
                    case "inquires" ->{
                        Customer.inquireBalancaTransaction(fromFullName, fromWhere);
                        break;
                    }
                    case "deposits" ->{
                        Customer.depositsTransaction(toFullName, toWhere, amount);
                        break;
                    }
                    case "withdraws" ->{
                        Customer.withdrawTransaction(fromFullName, fromWhere, amount);
                        break;
                    }
                    case "transfers" ->{
                        Customer.makeTransferTransaction(fromFullName, toFullName, fromWhere, toWhere, amount);
                        break;
                    }

                    case "pays" ->{
                        Customer.paySomeoneTransaction(fromFullName, toFullName, fromWhere, toWhere, amount);
                        break;
                    }
                    default ->{
                        break;
                    }
                }
            }
            return;
        } catch (FileNotFoundException e) {
            System.out.println("Transactions file not found.");
        }
    }

    /**
     * This method prompts the bank manager to enter a customer's name and attempts to generate a bank statement for that customer.
     * <p>
     * The method continuously prompts for the customer's name until a valid customer is entered. If the customer is not found,
     * it displays an error message and asks for the name again. Once a valid customer is provided, the method would ideally generate
     * a bank statement for that customer (though the functionality to generate the bank statement has yet to be implemented).
     * </p>
     *
     * <p>If there is an input error (e.g., the user enters invalid input or there's an issue with retrieving the customer),
     * the method will handle it and prompt for input again.</p>
     *
     * @throws NoSuchElementException If there is an input error or the customer name does not exist in the customer map.
     */
    private static void generateBankStatement(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print("Please enter the user's name to generate a bank statement: ");
                String name = scanner.nextLine().trim();

                Customer customer = customersMap[1].get(name);

                if (customer == null) {
                    System.out.println("Customer not found. Please enter a valid customer.");
                } else {

                    try (PrintWriter writer = new PrintWriter(new File("BankStatement_" + customer.getFirstName() + customer.getLastName() + ".txt"))) {
                        writer.println("--- Bank Statement ---");
                        writer.println("Customer Name: " + customer.getFirstName() + " " + customer.getLastName());
                        writer.println("Address: " + customer.getAddress());
                        writer.println("Phone Number: " + customer.getPhoneNumber());
                        writer.println("Date of Birth: " + customer.getDateOfBirth());

                        writer.println("\n--- Transaction History ---");

                        File file = new File(customer.getFirstName() + " " + customer.getLastName() + "_TransactionReport.txt");
                        Scanner fileScanner = new Scanner(file);
                        fileScanner.nextLine();
                        while(fileScanner.hasNextLine()){
                            String line = fileScanner.nextLine();
                            writer.println(line);
                        }


                        System.out.println("Bank statement generated successfully for " + customer.getFirstName() + " " + customer.getLastName());
                    } catch (FileNotFoundException e) {
                        System.out.println("Error generating bank statement file: " + e.getMessage());
                    }

                    break;
                }

            } catch (NoSuchElementException e) {
                System.out.println("Input error. Please try again.");
            }

        }

    }
}