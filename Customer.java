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
            String choice = scanner.next().trim(); // Read and trim input
            account = RunBank.getAccountByChoice(customer, choice); 
            if (account == null) {
                System.out.println("Input valid choice. 1 to 3:");
            }
        }
    }

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
        Log.logUserTransaction(customer.getFirstName() + " " + customer.getLastName(), message);
    }

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

        // Make the payment
        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);

        System.out.println("Payment successful!");
        String name = customer.getFirstName() + " " + customer.getLastName();
        
        String message = name + " paid $" + amount + " to " + recipientName + " from " + accountFrom.getAccountType() + " account to " + accountTo.getAccountType() + " account.";
        Log.logEntries(message);
        Log.logUserTransaction(name, message);

    }

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

    public static void paySomeoneTransaction(String fromUser, String toUser, String fromAccount, String toAccount, double amount){
        /*
            * 1. Check if its a valid account (from and to)
            * 2. Check if amount is valid
            * 3. Perform the action.
            * 4. For each problem, sout what went wrong and why
            * 
            */
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
            Log.logUserTransaction(fromUser, message);
        }

        }

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
            Log.logUserTransaction(fromUser, message);

            
        }
    }

    public static void depositsTransaction(String toUser, String toAccount, double amount){
        if(!nameMap.containsKey(toUser)){
            Log.logEntries("Transaction failed: no user with that name.");
        }

        Customer user = nameMap.get(toUser);
        Account userAccount = accountTypeTransaction(user, toAccount);
        userAccount.setBalance(userAccount.getBalance() + amount);

        String message = "Successful Transaction! $" + amount + " has been deposited into " + toUser + " 's " + toAccount;
        Log.logEntries(message);
        Log.logUserTransaction(toUser, message);

    }

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
        Log.logUserTransaction(fromUser, message);

    }

    public static void inquireBalancaTransaction(String fromUser, String fromAccount){
        if(!nameMap.containsKey(fromUser)){
            Log.logEntries("Transaction failed: no user with that name.");
        }

        Customer user = nameMap.get(fromUser);
        Account userAccount = accountTypeTransaction(user, fromAccount);

        String message = "Successful transaction! " + fromUser + " has inquired about" + fromAccount +" 's balance: " + userAccount.getBalance();
        Log.logEntries(message);
        Log.logUserTransaction(fromUser, message);
    }
}
