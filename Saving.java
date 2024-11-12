/**
 * The Saving class represents a savings account that extends the Account class.
 * It is designed to hold a balance and provide functionalities specific to savings accounts.
 * It implements the Withdrawable interface to provide withdrawal functionality
 * 
 * @author Daniela Castro Enriquez
 * @author Carlos Gonzalez
 * @author Aylin Rodriguez
 * 
 */
public class Saving extends Account implements Withdrawable {

    /**
     * Constructs a Saving account with the specified account number, starting balance,
     * and account holder's information.
     *
     * @param accountNumber   the unique identifier for the savings account
     * @param startingBalance the initial balance for the savings account
     * @param accountHolder   the Person object representing the account holder
     */
    public Saving(int accountNumber, double startingBalance, Person accountHolder) {
        super(accountNumber, startingBalance, accountHolder, "Savings");
    }
    
    /**
     * Attempts to withdraw the specified amount from the savings account.
     * Checks if the withdrawal is allowed based on the current balance.
     *
     * @param amount the amount to be withdrawn
     * @return true if the withdrawal was successful; false if there were insufficient funds
     */
    @Override
    public boolean withdraw(double amount) {
        if (allowedToWithdraw(amount)) {
            setBalance(getBalance() - amount);
            return true;
        }
        System.out.println("Insufficient funds. Savings account balance: $" + getBalance());
        return false;
    }

    /**
     * Checks if the specified amount can be withdrawn based on the current balance.
     *
     * @param amount the amount to be checked for withdrawal
     * @return true if the current balance is sufficient; false otherwise
     */
    @Override 
    public boolean allowedToWithdraw(double amount) {
        return getBalance() >= amount;
    }

    /**
     * Returns a string showing the account information. Inherited from the Account class.
     *
     * @return a string representing the Savings account information
     */
    @Override
    public String toString() {
        return super.toString();
    }
}