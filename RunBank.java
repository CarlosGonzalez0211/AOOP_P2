import java.util.HashMap;
import java.util.InputMismatchException;
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
    public static void main(String[] args) {
        // Load customer data from file into two HashMaps (ID-based and name-based as keys)
        PopulationHashmap customerMap = new PopulationHashmap();
        HashMap<String, Customer>[] customersMap = customerMap.readFile();

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
        PopulationHashmap customerMap = new PopulationHashmap();

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

                if (userChoice == 6) {
                    customerMap.writeToCSV("id_map.csv", customersMap[1]);
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

    private static void addNewUser(Scanner scanner, HashMap<String, Customer>[] customersMaps) {
        // Increment lastUserId for the new user
        int maxId = getMaxUserId(customersMaps[0]) + 1;
        String newUserId = String.valueOf(maxId);

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
        
        
        // create new account numbers for accounts savingsAccountNumber = generateAccountNumber();
        int checkingAccountNumber = generateAccountNumber();
        int creditAccountNumber = generateAccountNumber();
        int savingsAccountNumber = generateAccountNumber();

        //Credit Score part

    }

    public static int getMaxUserId(HashMap<String, Customer> customerMap) {
        int maxId = 0; // Start with the minimum possible value

        for (String userId : customerMap.keySet()) {
            try {
                int id = Integer.parseInt(userId); // Convert the user ID to an integer
                if (id > maxId) {
                    maxId = id; // Update maxId if a larger ID is found
                }
            } catch (NumberFormatException e) {
                // Handle any non-numeric user IDs gracefully
                System.out.println("Invalid user ID format: " + userId);
            }
        }

        return maxId;
    }

    private static int generateAccountNumber(){
        Random random = new Random();
        int number = random.nextInt(6001); // Generates a number between 0 and 6000 (inclusive)
        return number;
    }
}
