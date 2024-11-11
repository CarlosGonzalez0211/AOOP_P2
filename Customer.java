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

        System.out.print("Input amount to deposit: ");
        double amount = scanner.nextDouble();
        amount = validateAmount(amount, scanner);
       
        if (account == customer.getCreditAccount()) {
            double creditMax = Math.abs(customer.getCreditAccount().getCreditMax());
            double currentBalance = account.getBalance();

            while (currentBalance + amount > creditMax) {
                System.out.println("You cannot deposit more money than the credit max: $" + creditMax);
                System.out.print("Input a valid amount: ");
                amount = scanner.nextDouble();
                amount = validateAmount(amount, scanner);
            }
        }
        
        account.setBalance(account.getBalance() + amount);
        System.out.println("New " + account.getAccountType() + " account balance: $" + account.getBalance());

        String name = customer.getFirstName() + " " + customer.getLastName();
        String accountTitle = account.getAccountType() + "-" + account.getAccountNum();
        Log.logEntries(name + " made a deposit on " + accountTitle + ". " + name + "'s new balance for " + accountTitle + " is " + account.getBalance());
    }

    public static void makeWithdrawal(Customer customer, Scanner scanner) {
        System.out.println("Which account would you like to make a withdrawal from?");
        RunBank.menuTypesAccount();
        Account account = null;
        System.out.print("Enter your choice (1 to 2): ");

        while (account == null) {
            String choice = scanner.next().trim(); // Read and trim input
            account = RunBank.getAccountByChoice(customer, choice);

            if (choice.equals("3")) {
                System.out.println("You cannot withdraw from credit.");
                System.out.print("Input valid choice 1 to 2: ");
                account = null;
                continue;
            }

            if (account == null) {
                System.out.println("Input valid choice. 1 to 2");
            }
        }

        double amount = withdrawMoney(account, scanner);
        account.setBalance(account.getBalance() - amount);  // Subtract the amount from the account balance
        Log.logEntries("Withdrawal successful of $" + amount + " from " + account.getAccountType() + " account. New " + account.getAccountType() + " account balance: $" + account.getBalance());

        // Add this line to print the updated balance for validation
        System.out.println("New " + account.getAccountType() + " account balance: $" + account.getBalance());
    }

    public static void makeTransfer(Customer customer, Scanner scanner){
        //Which account to withdraw:
        System.out.println("Choose account to withdraw from?");
        RunBank.menuTypesAccount();
        Account accountFrom = null;
        System.out.print("Enter your choice (1 to 2): ");

        while (accountFrom == null) {
            String choice = scanner.next().trim(); // Read and trim input
            accountFrom = RunBank.getAccountByChoice(customer, choice); 

            if (choice.equals("3")){
                System.out.println("You cannot withdraw from credit.");
                System.out.print("Input valid choice 1 to 2: ");
                accountFrom = null;
                continue;
            }

            if (accountFrom == null) {
                System.out.println("Input valid choice. 1 to 2");
            }
        }

        System.out.println("Choose account to transfer to?");
        RunBank.menuTypesAccount();
        Account accountTo = null;
        System.out.print("Enter your choice (1 to 3): ");

        while (accountTo == null) {
            String choice = scanner.next().trim(); // Read and trim input
            accountTo = RunBank.getAccountByChoice(customer, choice); 
            if (accountTo == null) {
                System.out.println("Input valid choice. 1 to 3");
            }
        }

        //Deposit money
        double amount = withdrawMoney(accountFrom, scanner);
        if (accountTo == customer.getCreditAccount()) {
            double creditMax = Math.abs(customer.getCreditAccount().getCreditMax());
            double currentBalance = accountTo.getBalance();
            

            while (currentBalance + amount > creditMax) {
                System.out.println("You cannot deposit more money than the credit max: $" + creditMax);
                System.out.print("Input a valid amount: ");
                amount = scanner.nextDouble();
                amount = validateAmount(amount, scanner);
            }
        }
        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);

        System.out.println("Transfer successful!");
        String name = customer.getFirstName() + " " + customer.getLastName();
        Log.logEntries(name + " transferred: $" + amount + " from " + accountFrom.getAccountType() + " account to " + accountTo.getAccountType() + " account");

    }

    public static void paySomeone(Customer customer, Scanner scanner, HashMap<String, Customer> customerMap) {
    // Step 1: Choose account to withdraw from (Checking or Savings)
        System.out.println("Which account would you like to withdraw from?");
        RunBank.menuTypesAccount();
        Account accountFrom = null;
        System.out.print("Enter your choice (1 to 2): ");
        while (accountFrom == null) {
            
            String choice = scanner.next().trim();
            accountFrom = RunBank.getAccountByChoice(customer, choice);

            if (choice.equals("3")) {
                System.out.println("You cannot withdraw from a credit account.");
                System.out.print("Input valid choice 1 to 2: ");
                accountFrom = null;
                continue;
            }

            if (accountFrom == null) {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }

        // Step 2: Input the amount to withdraw
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

        //Choose the recipient's account to deposit into (Checking or Savings)
        System.out.println("Which account would you like to pay into?");
        RunBank.menuTypesAccount();
        Account accountTo = null;
        System.out.print("Enter choice: ");

        while (accountTo == null) {
            
            String choice = scanner.next().trim();
            accountTo = RunBank.getAccountByChoice(recipient, choice);

            if (choice.equals("3")) {
                System.out.println("You cannot deposit to a credit account.");
                System.out.print("Input valid choice 1 to 2: ");
                accountTo = null;
                continue;
            }

            if (accountTo == null) {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }

        //Make the payment
        accountFrom.setBalance(accountFrom.getBalance() - amount);
        accountTo.setBalance(accountTo.getBalance() + amount);

        System.out.println("Payment successful!");
        String name = customer.getFirstName() + " " + customer.getLastName();
        Log.logEntries(name + " paid $" + amount + " to " + recipientName + " from " + accountFrom.getAccountType() + " account to " + accountTo.getAccountType() + " account.");
    }

    private static double withdrawMoney (Account account, Scanner scanner){
        System.out.print("Input amount to withdraw: ");
        double amount = scanner.nextDouble();
        amount = validateAmount(amount, scanner);

        while(amount > account.getBalance()){
            System.out.println("Amount exceeds account's balance: " + account.getBalance());
            System.out.print("Input valid amount: ");
            amount = scanner.nextDouble();
            amount = validateAmount(amount, scanner);
        }
        return amount;
    }
    
    //Works to validate amount (negative amounts)
    private static double validateAmount(double amount, Scanner scanner){
        while (amount <= 0){
            System.out.print("Input valid amount, not negative amounts: ");
            amount = scanner.nextDouble();

        }
        return amount;
    }
}
