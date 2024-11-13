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

     /**
     * An array of HashMaps that stores customer information, where each HashMap
     * holds customer data organized by different keys. Populated by reading from a file.
     */
    public static HashMap<String, Customer> [] userMaps = PopulationHashmap.readFile();

      /**
     * A HashMap that maps customer names to Customer objects for quick access by name.
     * This map is the second entry in the userMaps array, which stores customers by their names.
     */
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
    * Constructs a new Customer object with the specified details.
    * This constructor initializes a customer with personal information and a list of associated accounts.
    *
    * @param idNumber the unique identification number of the customer
    * @param firstName the first name of the customer
    * @param lastName the last name of the customer
    * @param dateOfBirth the date of birth of the customer (in string format)
    * @param address the address of the customer
    * @param city the city where the customer resides
    * @param state the state where the customer resides
    * @param zip the zip code of the customer's address
    * @param phoneNumber the phone number of the customer
    * @param accounts an array of Account objects representing the customer's accounts (credit, checking, savings)
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

    /**
    * Inquires the balance of all accounts associated with the given customer.
    * This method prints the account details (balance) for the customer's checking, 
    * savings, and credit accounts. It also logs the action made by the customer.
    *
    * @param customer the customer whose account balances are to be inquired
    */
    public static void inquireBalance(Customer customer) {
        //Inquire Balance will get all the customer's accounts balance.
        System.out.println(customer.getCheckingAccount().toString() + "\n");
        System.out.println(customer.getSavingAccount().toString() + "\n");
        System.out.println(customer.getCreditAccount().toString() + "\n");

        String name = customer.getFirstName() + " " + customer.getLastName();
        Log.logEntries(name + " made a balance inquiry on their accounts.");
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

    /**
    * This method allows a customer to make a withdrawal from one of their accounts.
    * 
     *
    * @param customer the customer from whose account the withdrawal will be made
    * @param scanner the Scanner object used to capture user input for account selection and withdrawal amount
    */
    public static void makeWithdrawal(Customer customer, Scanner scanner) {
        System.out.println("Which account would you like to make a withdrawal from?");
        RunBank.menuTypesAccount();
        Account account = null;
        System.out.print("Enter your choice (1 to 3): "); 
    
        while (account == null) {
            String choice = scanner.next().trim(); 
            account = RunBank.getAccountByChoice(customer, choice);
    
            if (account == null) {
                System.out.println("Input a valid choice. 1 to 3:");
            }
        }
    
    
   
        
        double amount = withdrawMoney(account, scanner); 
        account.setBalance(account.getBalance() - amount);
        // Display updated balance without further subtraction
        System.out.println("New " + account.getAccountType() + " account balance: $" + account.getBalance());
        Log.logEntries("Withdrawal of $" + amount + " from " + account.getAccountType() + " account. New balance: $" + account.getBalance());
        
    }
    /**
     * This method allows a customer to transfer money between two of their accounts.
    * The user is prompted to select the source and destination accounts.
    * The transfer amount is withdrawn from the source account and deposited into the destination account.
    * If the transfer involves a credit account, the method checks if the transfer would exceed the credit limit before proceeding.
    * A log entry is created for the transaction, and updated balances are displayed.
    *
    * @param customer the customer who is making the transfer
    * @param scanner the Scanner object used to capture user input for account selections and transfer amount
    */
    public static void makeTransfer(Customer customer, Scanner scanner) {
        System.out.println("Choose account to withdraw from:");
        RunBank.menuTypesAccount();
        Account accountFrom = null;
        System.out.print("Enter your choice (1 to 3): ");

        while (accountFrom == null) {
            String choice = scanner.next().trim();
            accountFrom = RunBank.getAccountByChoice(customer, choice);
        
            if (accountFrom == null) {
                System.out.println("Invalid choice. Enter 1, 2, or 3:");
            }
        }
        
       
        System.out.println("Choose account to transfer to:");
        RunBank.menuTypesAccount();
        Account accountTo = null;
        System.out.print("Enter your choice (1 to 3): ");
        
        while (accountTo == null) {
            String choice = scanner.next().trim();
            accountTo = RunBank.getAccountByChoice(customer, choice);
        
            if (accountTo == null) {
                System.out.println("Invalid choice. Enter 1, 2, or 3:");
            }
        }
        
  
        double amount = withdrawMoney(accountFrom, scanner);
        
        if (accountFrom == customer.getCreditAccount()) {
            double currentBalance = accountFrom.getBalance(); 
            double creditMax = Math.abs(customer.getCreditAccount().getCreditMax());  
            
            if (Math.abs(currentBalance) + amount > creditMax) {
                System.out.println("Transfer would exceed the credit limit of $" + creditMax);
                System.out.println("Transfer failed. Please enter a valid amount.");
                return; 
            }
        }
        
      
        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);
   
        System.out.println("Transfer successful!");
        String name = customer.getFirstName() + " " + customer.getLastName();
        Log.logEntries(name + " transferred $" + amount + " from " + accountFrom.getAccountType() + " to " + accountTo.getAccountType());
        
        System.out.println("New balance for " + accountFrom.getAccountType() + " account: $" + accountFrom.getBalance());
    }
    
    
    /**
     * This method allows a customer to make a payment to another customer.
    * The customer first selects an account to withdraw from, then enters the recipient's name.
    * If the recipient is found in the customer map, the customer chooses an account to transfer money into.
    * The method ensures that the customer cannot pay themselves and that the recipient exists in the system.
    * A successful payment results in the withdrawal from the payer's account and a deposit into the recipient's account.
    * A log entry is created for the transaction.
    *
    * @param customer the customer making the payment
    * @param scanner the Scanner object used to capture user input for account selections, recipient name, and payment amount
    * @param customerMap the map of customers used to look up the recipient's account information
    */
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

        double amount = withdrawMoney(accountFrom, scanner);

        System.out.print("Enter the full name of the recipient: ");
        scanner.nextLine(); 
        String recipientName = scanner.nextLine().trim();

        if (!customerMap.containsKey(recipientName)) {
            System.out.println("Recipient not found. Payment canceled.");
            return;
        }

        Customer recipient = customerMap.get(recipientName);
        if (customer.getFirstName().equals(recipient.getFirstName()) && customer.getLastName().equals(recipient.getLastName())) {
            System.out.println("You cannot pay yourself. Payment canceled.");
            return;
        }

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
        Log.logEntries(name + " paid $" + amount + " to " + recipientName + " from " + accountFrom.getAccountType() + " account to " + accountTo.getAccountType() + " account.");
}

    /**
    * This method prompts the user to enter an amount to withdraw from the specified account. 
    * It then validates the withdrawal amount to ensure it is within acceptable limits. 
    * If the account is a credit account, the method checks whether the requested withdrawal exceeds the available credit limit. 
    * If the amount exceeds the limit, the user is prompted to enter a valid amount.
    * 
    * @param account the account from which money will be withdrawn
    * @param scanner the Scanner object used to capture user input for the withdrawal amount
    * @return the validated withdrawal amount
    */ 
    private static double withdrawMoney(Account account, Scanner scanner) {
        System.out.print("Input amount to withdraw: ");
        double amount = scanner.nextDouble();
        amount = validateAmount(amount, scanner, account);
    
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
    * This method validates the withdrawal amount by ensuring it is positive and does not exceed the account balance.
    * If the amount is less than or equal to zero, the user will be prompted to input a valid amount.
    * If the amount exceeds the account balance, the user will be informed and asked to input a valid amount.
    * 
    * @param amount the initial withdrawal amount to be validated
    * @param scanner the Scanner object used to capture user input
    * @param account the account from which the withdrawal is to be made, used to check the balance
    * @return the validated withdrawal amount
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
    * This method handles a transaction where one user (payer) pays another user (payee) a specified amount 
    * from one account to another. The method performs the following actions:
    * <ul>
    *     <li>Verifies that both the payer and payee exist in the system</li>
    *     <li>Checks that the payer has sufficient funds in the specified account</li>
    *     <li>Transfers the specified amount from the payer's account to the payee's account</li>
    * </ul>
    * If any of the checks fail (e.g., users don't exist, insufficient funds), the transaction is canceled and a message is displayed.
    * 
    * @param fromUser the name of the payer (user making the payment)
    * @param toUser the name of the payee (user receiving the payment)
    * @param fromAccount the type of the account from which the payer is withdrawing money
    * @param toAccount the type of the account into which the payee is receiving money
    * @param amount the amount of money to be transferred from the payer's account to the payee's account
    */
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
            
            System.out.println("Successful transaction! " + fromUser + " paid $" + amount + " to " + toUser + " from " + payerAccount.getAccountType() + " account to " + payeeAccount.getAccountType() + " account.");
        }

        }

        /**
    * This method returns the account of the specified type for a given user (Customer).
    * It retrieves the appropriate account (Credit, Checking, or Savings) based on the input account type.
    * 
    * @param user the customer whose account is being retrieved
    * @param accountType the type of account to retrieve ("Credit", "Checking", or "Savings")
     * @return the account of the specified type
     * @throws AssertionError if an invalid account type is provided (not "Credit", "Checking", or "Savings")
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
    * Processes a transfer transaction between two users from one account to another.
    * 
    * This method performs a transfer of funds from one user's account to another user's
    * account. It ensures that the account types are different, both users exist, the amount
    * is valid, and the payer has enough funds to complete the transfer. If any conditions are
     * not met, the transaction is canceled and an appropriate error message is displayed.
    * 
    * @param fromUser The username of the payer (the user initiating the transfer).
    * @param toUser The username of the payee (the user receiving the transfer).
    * @param fromAccount The account type to withdraw from (e.g., "Credit", "Checking", or "Savings").
    * @param toAccount The account type to deposit into (e.g., "Credit", "Checking", or "Savings").
    * @param amount The amount to transfer.
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
            return;
        }else{
            payerAccount.setBalance(payerAccount.getBalance() - amount);
            payeeAccount.setBalance(payerAccount.getBalance() + amount);
            System.out.println("Successful Transaction! " + fromUser + " transferred: $" + amount + " from " + payerAccount.getAccountType() + " account to " + payeeAccount.getAccountType() + " account");

            
        }
    }

    /**
     * Processes a deposit transaction for a user's account.
     * 
    * This method handles the deposit of funds into a specific account of a user.
    * It ensures that the user exists, the account type is valid, and the amount 
    * is greater than 0 before proceeding with the deposit. If any conditions are
    * not met, the transaction is canceled and an appropriate error message is displayed.
    * 
    * @param toUser The username of the user receiving the deposit.
    * @param toAccount The account type to deposit into (e.g., "Credit", "Checking", or "Savings").
    * @param amount The amount to deposit into the account.
    */
    public static void depositsTransaction(String toUser, String toAccount, double amount){
        if(!nameMap.containsKey(toUser)){
            System.out.println("Transaction failed: no user with that name.");
        }

        Customer user = nameMap.get(toUser);
        Account userAccount = accountTypeTransaction(user, toAccount);

        if(amount <= 0 || amount > userAccount.getBalance()){
            System.out.println("Failed transaction: amount is less than 0 or more than the payer's account balance (" + userAccount.getBalance() + ") ");
            return;
        }

        userAccount.setBalance(userAccount.getBalance() + amount);
        System.out.println("Successful Transaction! $" + amount + " has been deposited into " + toUser + " 's " + toAccount);

    }

    /**
    * Processes a withdrawal transaction from a user's account.
    * 
    * This method handles the withdrawal of funds from a specific account of a user.
    * It checks that the user exists, the account type is valid, and the withdrawal 
    * amount is positive and less than or equal to the account's balance. If any 
    * conditions are not met, the transaction is canceled and an appropriate 
    * error message is displayed.
    * 
    * @param fromUser The username of the user making the withdrawal.
    * @param fromAccount The account type to withdraw from (e.g., "Credit", "Checking", or "Savings").
    * @param amount The amount to withdraw from the account.
    */
    public static void withdrawTransaction(String fromUser, String fromAccount, double amount){
        if(!nameMap.containsKey(fromUser)){
            System.out.println("Transaction failed: no user with that name.");
        }

        Customer user = nameMap.get(fromUser);
        Account userAccount = accountTypeTransaction(user, fromAccount);

        if(amount <= 0 || amount > userAccount.getBalance()){
            System.out.println("Failed transaction: amount is less than 0 or more than the payer's account balance (" + userAccount.getBalance() + ") ");
            return;
        }

        userAccount.setBalance(userAccount.getBalance() + amount);
        System.out.println("Successful Transaction! $" + amount + " has been deposited into " + fromUser + " 's " + fromAccount);

    }

    /**
    * Allows a user to inquire about the balance of a specific account.
    * 
    * This method handles the inquiry of a specific account balance for a given user.
    * It checks whether the user exists, and if the account type is valid. Once validated, 
    * it retrieves and displays the balance of the specified account.
    * 
    * @param fromUser The username of the user making the inquiry.
    * @param fromAccount The account type to inquire about (e.g., "Credit", "Checking", or "Savings").
    */
    public static void inquireBalancaTransaction(String fromUser, String fromAccount){
        if(!nameMap.containsKey(fromUser)){
            System.out.println("Transaction failed: no user with that name.");
        }

        Customer user = nameMap.get(fromUser);
        Account userAccount = accountTypeTransaction(user, fromAccount);

        System.out.println("Successful transaction! " + fromUser + " has inquired about" + fromAccount +" 's balance: " + userAccount.getBalance());

    }
}
