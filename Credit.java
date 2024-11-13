/**
 * The Credit class represents a credit account, which inherits from the Account class.
 * It includes functionalities specific to credit accounts, such as managing the
 * maximum credit limit,  tracking the account balance, and status of withdrawals.
 *
 * @author Daniela Castro Enriquez
 * @author Carlos Gonzalez
 * @author Aylin Rodriguez
 *
 */
public class Credit extends Account implements Withdrawable {

    /** The credit limit for this account. */
    private double creditMax;

    /**
     * This constructor intitializes a credit account with the specified account number, starting balance,
     * maximum credit limit, and account owner's information.
     *
     * @param accountNumber    the account number for the credit account
     * @param startingBalance  the initial balance of the credit account
     * @param creditMax        the credit limit for this account
     * @param accountHolder    the Person who holds this credit account
     */
    public Credit(int accountNumber, double startingBalance, double creditMax, Person accountHolder) {
        super(accountNumber, startingBalance, accountHolder, "Credit");
        this.creditMax = creditMax;
    }

    /**
     * This method establishes the maximum credit limit for this account.
     *
     * @param creditMax the new maximum credit limit
     */
    public void setCreditMax(double creditMax) {
        this.creditMax = creditMax;
    }

    /**
     * This mmethod returns the maximum credit limit for this account.
     *
     * @return the maximum credit limit
     */
    public double getCreditMax() {
        return this.creditMax;
    }

    /**
     * This method withdraws the specified amount from the credit account.
     * The withdrawal of money is allowed if the current balance does not exceed the credit limit.
     * It implements the Withdrawable interface to provide withdrawal functionality specific to credit accounts.
     *
     * @param amount the amount to be withdrawn
     * @return true if the withdrawal is successful; false if it exceeds the credit limit
     */
    @Override
    public boolean withdraw(double amount) {
        double newBalance = getBalance() - amount;
        if (allowedToWithdraw(amount)) {
            setBalance(newBalance);
            return true;
        }
        System.out.println("Withdrawal denied. Exceeds credit limit.");
        System.out.println("Current balance: $" + getBalance() + "\n" +
                "Credit limit: $" + getCreditMax() + "\n" +
                "Available credit: $" + (creditMax + getBalance()));
        return false;
    }

    /**
     * This method checks if the specified amount can be withdrawn without exceeding the credit limit.
     *
     * @param amount the amount to be checked for withdrawal
     * @return true if the balance after withdrawal remains within the credit limit; otherwise, it would be false
     */
    @Override
    public boolean allowedToWithdraw(double amount) {
        return (getBalance() - amount) >= -creditMax;
    }

    /**
     * This method returns a string were the account information is shown, this method is inherited from the Account class.
     * This method also includes displaying the maximum credit.
     *
     * @return a string representing the Credit account information
     */
    @Override
    public String toString() {
        return super.toString() + "\n" +
                "Maximum credit: " + getCreditMax();
    }

}
