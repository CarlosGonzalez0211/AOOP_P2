import java.util.HashSet;
import java.util.Set;

/**
 * This class shows the different attributes and functionalities that an account has.
 * Thie class is inherited by Checking, Credit, and Saving, since they are all 
 * different types of accounts that each user has.
 * 
 * @author Daniela Castro Enriquez
 * @author Carlos Gonzalez
 * @author Aylin Rodriguez
 * 
 */
public abstract class Account {

    /** The account number for each account. */
    private int accountNumber;

    /** The current balance of each account type. */
    private double currentBalance;

    /** The starting balance when the account was first intitialized. */
    private double startingBalance;

    /** The owner of the account. */
    private Person accountHolder;

    /** The type of the account, either a Checking, Savings, or Credit account. */
    private String accountType;

   /** 
     * A set containing unique account numbers.
     */
    private static Set<Integer> accountNumbers = new HashSet<>();

    /**
     * Adds savings, checking, and credit account numbers to the set of unique account numbers.
     *
     * @param savingsNum the savings account number to be added
     * @param checkingNum the checking account number to be added
     * @param creditNum the credit account number to be added
     */
    public static void addToAccountNumbers(int savingsNum, int checkingNum, int creditNum) {
        accountNumbers.add(savingsNum);
        accountNumbers.add(checkingNum);
        accountNumbers.add(creditNum);
    }

    /**
     * Adds an individual account identifier (ID number) to the set of unique account numbers.
     *
     * @param idNumber the ID number to be added to the set
     */
    public static void addIdToAccountSet(int idNumber) {
        accountNumbers.add(idNumber);
    }

    /**
     * Retrieves the set of unique account numbers.
     *
     * @return a set of unique account numbers
     */
    public static Set<Integer> getSetAccountsNumbers() {
        return accountNumbers;
    }
    /**
     * This constructor initializes the account with th enumber of the account
     * the balance that has in the beginning, the account holder, and the type
     * of the account.
     *
     * @param accountNumber   the account number for each account
     * @param startingBalance the starting balance of each account type.
     * @param accountHolder   the owner of type Person of the account.
     * @param accountType     the type of the account, either a Checking, Savings, or Credit account.
     */
    
    public Account(int accountNumber, double startingBalance, Person accountHolder, String accountType) {
        this.accountNumber = accountNumber;
        this.startingBalance = startingBalance;
        this.currentBalance = startingBalance;
        this.accountHolder = accountHolder;
        this.accountType = accountType;
    }

    /**
     * This method makes a withdrawal of a specified amount from an account.
     * It acts differently depending on the account (abstract method).
     *      
     * @param amount the amount to withdraw
     * @return true if the withdrawal was successful, false if it was not processed correctly
     */
    public abstract boolean withdraw(double amount);

    /**
     * This method checks if it's possible to withdraw a certain amount from an account.
     *
     * @param amount the amount to check
     * @return true if it's a valid amount, false otherwise
     */
    public abstract boolean allowedToWithdraw(double amount);

    // Setters

    /**
     * This method assigns the account number to its respective account.
     *
     * @param accountNumber the number associated with the account.
     */
    public void setAccountNum(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * This method assigns the current balance of the account.
     *
     * @param currentBalance the current balance that an account has.
     */
    public void setBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    /**
     * This method assigns the account owner.
     *
     * @param accountHolder the Person who owns this account.
     */
    public void setAccountHolder(Person accountHolder) {
        this.accountHolder = accountHolder;
    }

    /**
     * This method establishes the starting balance of the account.
     *
     * @param startingBalance the starting balance of the account.
     */
    public void setStartingBalance(double startingBalance) {
        this.startingBalance = startingBalance;
    }

    /**
     * This method sets the type of the account.
     *
     * @param accountType the type of the accoount, which is either checking, savings or credit)
     */
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * This method retrieves the account number.
     *
     * @return the account number
     */
    public int getAccountNum() {
        return this.accountNumber;
    }

    /**
     * This method retrieves the current balance of the account.
     *
     * @return the current balance
     */
    public double getBalance() {
        return this.currentBalance;
    }

    /**
     * This method retrieves the account owner.
     *
     * @return the Person who holds the account
     */
    protected Person getAccountHolder() {
        return this.accountHolder;
    }

    /**
     * This methods gets the starting balance of an account.
     *
     * @return the starting balance
     */
    public double getStartingBalance() {
        return this.startingBalance;
    }

    /**
     * This method retrieves the type of an account.
     *
     * @return the type of the account, which is either checking, savings, or credit)
     */
    public String getAccountType() {
        return this.accountType;
    }

    
    /**
     * This method implements a string representation of the account, which shows the account number, type, and the account balance.
     *
     * @return a string representing the account information
     */
    @Override
    public String toString() {
        return "Account number: " + getAccountNum() + "\n" +
               "Account type: " + getAccountType() + "\n" +
               "Account current balance: " + getBalance();
    }
}
