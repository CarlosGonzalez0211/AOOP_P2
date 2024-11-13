/**
 * Withdrawable interface defines withdrawal-related methods for accounts.
 * @author Daniela Castro Enriquez
 * @author Carlos Gonzalez
 * @author Aylin Rodriguez
 */
public interface Withdrawable {
/**
     * Attempts to withdraw the specified amount from the account.
     *
     * @param amount the amount to be withdrawn
     * @return true if the withdrawal was successful; false otherwise
     */
    boolean withdraw(double amount);

    /**
     * Checks if the specified amount can be withdrawn based on account rules.
     *
     * @param amount the amount to be checked for withdrawal
     * @return true if withdrawal is allowed; false otherwise
     */
    boolean allowedToWithdraw(double amount);
}
    

