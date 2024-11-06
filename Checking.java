/**
 * The Checking class represents a checking account and inherits the Account class.
 * It provides the specific implementation of withdrawal functionalities and receives 
 * the specific attributes of a checking account.
 * 
 * @author Daniela Castro Enriquez
 * @author Carlos Gonzalez
 * @author Aylin Rodriguez
 * 
 */
public class Checking extends Account {

    /**
     * This constructor initializes a checking account with the specified account number, starting balance,
     * and account holder's information.
     *
     * @param accountNumber    the account number for each account
     * @param startingBalance  the initial balance of the credit account
     * @param accountHolder    the owner of type Person of the account
     */
    public Checking(int accountNumber, double startingBalance, Person accountHolder) {
        super(accountNumber, startingBalance, accountHolder, "Checking");
    }

    /**
     * This method withdrawS the specified amount from the checking account.
     * and checks if the withdrawal, using the method allowedToWithdraw, is allowed based on the current balance.
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
        System.out.println("Insufficient funds. " +
                "Checking account balance: $" + getBalance());
        return false;
    }

    /**
     * This method checks if its possible to withdraw the specified amount from an account.
     *
     * @param amount the amount to be checked for withdrawal
     * @return true if the current balance is greater than or equal to the amount; otherwise, returns false
     */
    @Override
    public boolean allowedToWithdraw(double amount) {
        return getBalance() >= amount;
    }

    /**
     * This method returns a string were the account information is shown, this method is inherited from the Account class.
     *
     * @return a string representing the Checking account information
     */
    @Override
    public String toString() {
        return super.toString();
    }

}
