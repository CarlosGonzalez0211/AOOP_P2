import java.util.HashMap;
import java.util.Scanner;

//WHERE?
/**
 * The Customer class represents a bank customer, inheriting from the Person class.
 * This class includes account management functionalities such as performing actions in handling multiple accounts,
 * such as transferring money, paying someone, checking balances, and making deposits.
 * 
 * @author Daniela Castro Enriquez
 * @author Carlos Gonzalez
 * @author Aylin Rodriguez
 * 
 */
public class Customer extends Person {

    /** The credit account of a customer. */
    private Credit creditAccount;

    /** The checking account of a customer. */
    private Checking checkingAccount;

    /** The savings account of a customer. */
    private Saving savingsAccount;

    /** An array of all accounts associated with this customer (credit, checking, savings). */
    private Account[] accounts;

    public static HashMap<String, Customer> [] userMaps = PopulationHashmap.readFile();
    public static HashMap<String, Customer> nameMap = userMaps[1];

    /**
     * This constructor constructs a Customer object with the specified personal information and associated accounts.
     *
     * @param idNumber     the unique identifier for the customer
     * @param firstName    the first name of the customer
     * @param lastName     the last name of the customer
     * @param dateOfBirth  the date of birth of the customer
     * @param address      the address of the customer
     * @param phoneNumber  the phone number of the customer
     * @param accounts     an array of Account objects associated with the customer (all the accounts of the customer)
     */
    public Customer(String idNumber, String firstName, String lastName, String dateOfBirth, String address,
                    String phoneNumber, Account[] accounts) {
        super(idNumber, firstName, lastName, dateOfBirth, address, phoneNumber);
        this.accounts = accounts;
    }

    /**
     * Constructs a new Customer object with the specified personal and account information.
     *
     * @param idNumber     the unique identifier for the customer
     * @param firstName    the first name of the customer
     * @param lastName     the last name of the customer
     * @param dateOfBirth  the date of birth of the customer
     * @param address      the address of the customer
     * @param city         the city where the customer resides
     * @param state        the state where the customer resides
     * @param zip          the zip code of the customer's address
     * @param phoneNumber  the phone number of the customer
     * @param accounts     an array of Account objects associated with the customer
     */
    public Customer(String idNumber, String firstName, String lastName, String dateOfBirth, String address, String city, String state, String zip,
                    String phoneNumber, Account[] accounts) {
        super(idNumber, firstName, lastName, dateOfBirth, address, city, state, zip, phoneNumber);
        this.accounts = accounts;
    }


    /**
     * This method retrieves the customer's credit account.
     *
     * @return the credit account associated with this customer
     */
    public Credit getCreditAccount() {
        return (Credit) this.accounts[2];
    }

    /**
     * This method retrieves the customer's checking account.
     *
     * @return the checking account associated with this customer
     */
    public Checking getCheckingAccount() {
        return (Checking) this.accounts[0];
    }

    /**
     * This methods gets the customer's savings account.
     *
     * @return the savings account associated with this customer
     */
    public Saving getSavingAccount() {
        return (Saving) this.accounts[1];
    }

    /**
     * This method establishes the accounts associated with this customer in an array of Accounts.
     *
     * @param accounts an array of accounts that belong to the customer
     */
    public void setAccounts(Account[] accounts) {
        this.accounts = accounts;
    }

    /**
     * This method retrieves the accounts associated with this customer.
     *
     * @return an array of 3 accounts
     */
    public Account[] getAccounts() {
        return this.accounts;
    }


    public static void inquireBalance(Customer customer) {
        //Inquire Balance will get all the customer's accounts balance.
        System.out.println(customer.getCheckingAccount().toString() + "\n");
        System.out.println(customer.getSavingAccount().toString() + "\n");
        System.out.println(customer.getCreditAccount().toString() + "\n");

        String name = customer.getFirstName() + " " + customer.getLastName();
        String message = name + " made a balance inquiry on their accounts."; 
        Log.logEntries(message);
        Log.transactions.add(message);
        Log.logUserTransaction(name, message);
    }
    /**
     * This method deposits a specified amount into the given account.
     *
     * @param account the account to deposit money into
     * @param amount  the amount of money to deposit
     */
    public static void makeDeposit(Customer customer, Scanner scanner) {
        System.out.println("Which account would you like to make a deposit?");
        RunBank.menuTypesAccount();
        Account account = null;
        System.out.print("Enter your choice (1 to 3): ");
        while (account == null) {
            String choice = scanner.next().trim();
            account = RunBank.getAccountByChoice(customer, choice);
            if (account == null) {
                System.out.println("Input valid choice. 1 to 3:");
            }
        }

        System.out.print("Input amount to deposit: ");
        double amount = scanner.nextDouble();
        amount = validateAmount(amount, scanner, account);

        if (account == customer.getCreditAccount()) {
            double creditMax = Math.abs(customer.getCreditAccount().getCreditMax());
            double currentBalance = account.getBalance();
            while (currentBalance + amount > creditMax) {
                System.out.println("You cannot deposit more money than the credit max: $" + creditMax);
                System.out.print("Input a valid amount: ");
                amount = scanner.nextDouble();
                amount = validateAmount(amount, scanner, account);
            }
        }

        account.setBalance(account.getBalance() + amount);
        System.out.println("New " + account.getAccountType() + " account balance: $" + account.getBalance());

        String name = customer.getFirstName() + " " + customer.getLastName();
        String accountTitle = account.getAccountType() + "-" + account.getAccountNum();
        String message = name + " made a deposit on " + accountTitle + ". " + name + "'s new balance for " + accountTitle + " is " + account.getBalance();
        Log.logEntries(message);
        Log.logUserTransaction(name, message);
    }

    /**
     * Retrieves and displays the balances for all of a customer's accounts: checking, savings, and credit.
     * Logs the balance inquiry action to the system log and the user's transaction log.
     *
     * @param customer the Customer object whose account balances are to be inquired
     */
    public static void makeWithdrawal(Customer customer, Scanner scanner) {
        System.out.println("Which account would you like to make a withdrawal from?");
        RunBank.menuTypesAccount();
        Account account = null;
        System.out.print("Enter your choice (1 to 3): "); // Include credit as option 3
    
        while (account == null) {
            String choice = scanner.next().trim(); // Read and trim input
            account = RunBank.getAccountByChoice(customer, choice);
    
            if (account == null) {
                System.out.println("Input a valid choice. 1 to 3:");
            }
        }
    
    
        // Update account balance accordingly
        
        double amount = withdrawMoney(account, scanner); // Only call to get withdrawal amount
        account.setBalance(account.getBalance() - amount);
        // Display updated balance without further subtraction
        System.out.println("New " + account.getAccountType() + " account balance: $" + account.getBalance());
        
        String message = "Withdrawal of $" + amount + " from " + account.getAccountType() + " account. New balance: $" + account.getBalance();
        Log.logEntries(message);
        Log.transactions.add(message);
        Log.logUserTransaction(customer.getFirstName() + " " + customer.getLastName(), message);
    }

    /**
     * Facilitates the transfer of funds between two of a customer's accounts.
     * The customer selects the source account to withdraw from and the destination account to transfer to.
     * Validates user input, ensures compliance with credit account limits, and logs the transfer.
     *
     * @param customer the Customer object performing the transfer
     * @param scanner  the Scanner object used to capture user input
     */
    public static void makeTransfer(Customer customer, Scanner scanner) {
        // Choose account to withdraw from:
        System.out.println("Choose account to withdraw from:");
        RunBank.menuTypesAccount();
        Account accountFrom = null;
        System.out.print("Enter your choice (1 to 3): ");
        
        // Input validation loop for accountFrom
        while (accountFrom == null) {
            String choice = scanner.next().trim();
            accountFrom = RunBank.getAccountByChoice(customer, choice);
        
            if (accountFrom == null) {
                System.out.println("Invalid choice. Enter 1, 2, or 3:");
            }
        }
        
        // Choose account to transfer to:
        System.out.println("Choose account to transfer to:");
        RunBank.menuTypesAccount();
        Account accountTo = null;
        System.out.print("Enter your choice (1 to 3): ");
        
        // Input validation loop for accountTo
        while (accountTo == null) {
            String choice = scanner.next().trim();
            accountTo = RunBank.getAccountByChoice(customer, choice);
        
            if (accountTo == null) {
                System.out.println("Invalid choice. Enter 1, 2, or 3:");
            }
        }
        
        // Withdraw amount from accountFrom
        double amount = withdrawMoney(accountFrom, scanner);
        
        // Additional validation for credit account withdrawals and ensuring transfer doesn't exceed balance
        if (accountFrom == customer.getCreditAccount()) {
            double currentBalance = accountFrom.getBalance();  // current debt on the credit account
            double creditMax = Math.abs(customer.getCreditAccount().getCreditMax());  // credit limit (positive value)
            
            // Ensure the transfer amount does not exceed available credit
            if (Math.abs(currentBalance) + amount > creditMax) {
                System.out.println("Transfer would exceed the credit limit of $" + creditMax);
                System.out.println("Transfer failed. Please enter a valid amount.");
                return; // Exit the method if the transfer is not allowed
            }
        }
        
        // Perform the transfer
        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);
        
        // Log and confirm
        System.out.println("Transfer successful!");
        String name = customer.getFirstName() + " " + customer.getLastName();
        String message = name + " transferred $" + amount + " from " + accountFrom.getAccountType() + " to " + accountTo.getAccountType();
        Log.logEntries(message);
        Log.transactions.add(message);
        Log.logUserTransaction(name, message);
        
        // Display updated balances
        System.out.println("New balance for " + accountFrom.getAccountType() + " account: $" + accountFrom.getBalance());
    }
    
    
    public static void paySomeone(Customer customer, Scanner scanner, HashMap<String, Customer> customerMap) {
        System.out.println("Which account would you like to withdraw from?");
        RunBank.menuTypesAccount();
        Account accountFrom = null;
        System.out.print("Enter your choice (1 to 3): ");
        
        while (accountFrom == null) {
            String choice = scanner.next().trim();
            accountFrom = RunBank.getAccountByChoice(customer, choice);

            if (accountFrom == null) {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }

        // Step 2: Input the amount to withdraw, with checks for credit limits if necessary
        double amount = withdrawMoney(accountFrom, scanner);

        // Step 3: Input the recipient's name
        System.out.print("Enter the full name of the recipient: ");
        scanner.nextLine(); // Consume any leftover newline
        String recipientName = scanner.nextLine().trim();

        // Check if the recipient exists in the customer map
        if (!customerMap.containsKey(recipientName)) {
            System.out.println("Recipient not found. Payment canceled.");
            return;
        }

        Customer recipient = customerMap.get(recipientName);
        if (customer.getFirstName().equals(recipient.getFirstName()) && customer.getLastName().equals(recipient.getLastName())) {
            System.out.println("You cannot pay yourself. Payment canceled.");
            return;
        }

        // Choose the recipient's account to deposit into (Checking or Savings)
        System.out.println("Which account would you like to pay into?");
        RunBank.menuTypesAccount();
        Account accountTo = null;
        System.out.print("Enter your choice (1 or 2): ");

        while (accountTo == null) {
            String choice = scanner.next().trim();
            accountTo = RunBank.getAccountByChoice(recipient, choice);

            if (accountTo == null) {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }

        
        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);

        System.out.println("Payment successful!");
        String name = customer.getFirstName() + " " + customer.getLastName();
        
        String message = name + " paid $" + amount + " to " + recipientName + " from " + accountFrom.getAccountType() + " account to " + accountTo.getAccountType() + " account.";
        Log.logEntries(message);
        Log.transactions.add(message);
        Log.logUserTransaction(name, message);

    }

    /**
     * Facilitates a payment from one customer's account to another customer's account.
     * The method prompts the customer to select an account to withdraw from and to specify
     * the recipient and their account for depositing the payment. It validates the recipient's existence
     * and ensures that the customer cannot make a payment to themselves.
     *
     * @param customer     the Customer object performing the payment
     * @param scanner      the Scanner object used to capture user input
     * @param customerMap  a HashMap containing customer data, used to validate the recipient's existence
     */
    private static double withdrawMoney(Account account, Scanner scanner) {
        System.out.print("Input amount to withdraw: ");
        double amount = scanner.nextDouble();
        amount = validateAmount(amount, scanner, account);
    
        // Check for overdraft or balance limits
        if (account.getAccountType().equals("Credit")) { 
            Credit creditAccount = (Credit) account;
            double maxCreditAvailable = Math.abs(creditAccount.getCreditMax()) - creditAccount.getBalance();
    
            while (amount > maxCreditAvailable) {
                System.out.println("Amount exceeds available credit: $" + maxCreditAvailable);
                System.out.print("Input a valid amount: ");
        
            }
        }
        return amount; 
    }
    /**
     * Validates the input amount to ensure it is a positive value and does not exceed the balance of the specified account.
     * Prompts the user to enter a new amount if the input is invalid, such as being negative or exceeding the account balance.
     *
     * @param amount   the initial amount to validate
     * @param scanner  the Scanner object used to capture user input
     * @param account  the Account object against which the balance validation is performed
     * @return the validated amount that is greater than zero and within the account's balance
     */
    private static double validateAmount(double amount, Scanner scanner, Account account){
        
        while (amount <= 0){
            System.out.print("Input valid amount, not negative amounts: ");
            amount = scanner.nextDouble();

        }

        while (amount > account.getBalance()) {
            System.out.println("Amount exceeds account balance: $" + account.getBalance());
            System.out.print("Input a valid amount: ");
            amount = scanner.nextDouble();
            amount = validateAmount(amount, scanner, account);
        }

        return amount;
    }

    /**
     * Facilitates a transaction where one user pays another user by transferring funds between specified accounts.
     * The method checks for the existence of both users, validates the amount to ensure it is positive
     * and does not exceed the payer's account balance, and then performs the transfer if all conditions are met.
     *
     * @param fromUser    the name of the user initiating the payment
     * @param toUser      the name of the recipient user
     * @param fromAccount the type of account from which the funds will be withdrawn (e.g., Checking, Savings)
     * @param toAccount   the type of account to which the funds will be deposited
     * @param amount      the amount of money to be transferred
     */
    public static void paySomeoneTransaction(String fromUser, String toUser, String fromAccount, String toAccount, double amount){
        Customer payer;
        Customer payee;

        if (!nameMap.containsKey(fromUser)){
            System.out.println("Failed transaction: user " + fromUser + " does not exist.");
            return;
        }else{
            payer = nameMap.get(fromUser);
        }

        if (!nameMap.containsKey(toUser)){
            System.out.println("Failed transaction: user " + toUser + " does not exist.");
            return;
        }else{
            payee = nameMap.get(toUser);
        }

        Account payerAccount = accountTypeTransaction(payer, fromAccount);
        Account payeeAccount = accountTypeTransaction(payee, toAccount);

        if(amount <= 0 || amount > payerAccount.getBalance()){
            System.out.println("Failed transaction: amount is less than 0 or more than the payer's account balance (" + payerAccount.getBalance() + ") ");
        }else{
            payerAccount.setBalance(payerAccount.getBalance() - amount);
            payeeAccount.setBalance(payeeAccount.getBalance() + amount);
            
            String message = "Successful transaction! " + fromUser + " paid $" + amount + " to " + toUser + " from " + payerAccount.getAccountType() + " account to " + payeeAccount.getAccountType() + " account.";
            Log.logEntries(message);
            Log.transactions.add(message);
        }

    }

    /**
     * Retrieves the specified account of the given customer based on the account type.
     *
     * @param user        the Customer whose account is to be retrieved
     * @param accountType the type of the account (e.g., "Credit", "Checking", "Savings")
     * @return the Account object corresponding to the specified account type
     * @throws AssertionError if the account type is not recognized
     */
    private static Account accountTypeTransaction(Customer user, String accountType){
        switch (accountType) {
            case "Credit":
                return user.getCreditAccount();
            case "Checking":
                return user.getCheckingAccount();
            case "Savings":
                return user.getSavingAccount();
            default:
                throw new AssertionError();
        }
    }

    /**
     * Facilitates a transfer transaction between two customers' accounts.
     * Validates user existence and ensures that the amount is within the payer's balance.
     *
     * @param fromUser    the name of the user initiating the transfer
     * @param toUser      the name of the user receiving the transfer
     * @param fromAccount the type of account from which the funds will be transferred
     * @param toAccount   the type of account to which the funds will be transferred
     * @param amount      the amount to be transferred
     */
    public static void makeTransferTransaction(String fromUser, String toUser, String fromAccount, String toAccount, double amount){
        //Check if the username is the same o
        if(fromAccount.equals(toAccount)){
            System.out.println("Transaction failed: user cannot transfer within the same account type.");
            return;
        }

        Customer payer;
        Customer payee;

        if (!nameMap.containsKey(fromUser)){
            Log.logEntries("Failed transaction: user " + fromUser + " does not exist.");
            return;
        }else{
            payer = nameMap.get(fromUser);
        }

        if (!nameMap.containsKey(toUser)){
            Log.logEntries("Failed transaction: user " + toUser + " does not exist.");
            return;
        }else{
            payee = nameMap.get(toUser);
        }

        Account payerAccount = accountTypeTransaction(payer, fromAccount);
        Account payeeAccount = accountTypeTransaction(payee, toAccount);

        if(amount <= 0 || amount > payerAccount.getBalance()){
            Log.logEntries("Failed transaction: amount is less than 0 or more than the payer's account balance (" + payerAccount.getBalance() + ") ");
            return;
        }else{
            payerAccount.setBalance(payerAccount.getBalance() - amount);
            payeeAccount.setBalance(payerAccount.getBalance() + amount);
            
            String message = "Successful Transaction! " + fromUser + " transferred: $" + amount + " from " + payerAccount.getAccountType() + " account to " + payeeAccount.getAccountType() + " account";
            Log.logEntries(message);
            Log.transactions.add(message);

            
        }
    }

    /**
     * Facilitates a deposit transaction into a customer's account.
     *
     * @param toUser    the name of the user receiving the deposit
     * @param toAccount the type of account to deposit into (e.g., "Checking", "Savings")
     * @param amount    the amount to be deposited
     */
    public static void depositsTransaction(String toUser, String toAccount, double amount){
        if(!nameMap.containsKey(toUser)){
            Log.logEntries("Transaction failed: no user with that name.");
        }

        Customer user = nameMap.get(toUser);
        Account userAccount = accountTypeTransaction(user, toAccount);
        userAccount.setBalance(userAccount.getBalance() + amount);

        String message = "Successful Transaction! $" + amount + " has been deposited into " + toUser + " 's " + toAccount;
        Log.logEntries(message);
        Log.transactions.add(message);

    }

    /**
     * Facilitates a withdrawal transaction from a customer's account.
     * Ensures that the amount is within the user's account balance.
     *
     * @param fromUser    the name of the user making the withdrawal
     * @param fromAccount the type of account to withdraw from (e.g., "Checking", "Savings")
     * @param amount      the amount to be withdrawn
     */
    public static void withdrawTransaction(String fromUser, String fromAccount, double amount){
        if(!nameMap.containsKey(fromUser)){
            Log.logEntries("Transaction failed: no user with that name.");
        }

        Customer user = nameMap.get(fromUser);
        Account userAccount = accountTypeTransaction(user, fromAccount);

        if(amount <= 0 || amount > userAccount.getBalance()){
            Log.logEntries("Failed transaction: amount is less than 0 or more than the payer's account balance (" + userAccount.getBalance() + ") ");
            return;
        }

        userAccount.setBalance(userAccount.getBalance() + amount);
        
        String message = "Successful Transaction! $" + amount + " has been deposited into " + fromUser + " 's " + fromAccount;
        Log.logEntries(message);
        Log.transactions.add(message);

    }

    /**
     * Performs a balance inquiry for a customer's specified account and logs the transaction.
     *
     * @param fromUser    the name of the user making the inquiry
     * @param fromAccount the type of account to inquire about (e.g., "Checking", "Savings")
     */
    public static void inquireBalancaTransaction(String fromUser, String fromAccount){
        if(!nameMap.containsKey(fromUser)){
            Log.logEntries("Transaction failed: no user with that name.");
        }

        Customer user = nameMap.get(fromUser);
        Account userAccount = accountTypeTransaction(user, fromAccount);

        String message = "Successful transaction! " + fromUser + " has inquired about" + fromAccount +" 's balance: " + userAccount.getBalance();
        Log.logEntries(message);
        Log.transactions.add(message);
    }
}
