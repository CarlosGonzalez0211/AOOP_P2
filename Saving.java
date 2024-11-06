/**
 * The Saving class represents a savings account that extends the Account class.
 * It is designed to hold a balance and provide functionalities specific to savings accounts.
 * 
 * @author Daniela Castro Enriquez
 * @author Carlos Gonzalez
 * @author Aylin Rodriguez
 * 
 */
public class Saving extends Account {

    /**
     * This cosntructor constructs a Saving account with the specified account number, starting balance,
     * and account holder's information.
     *
     * @param accountNumber     the unique identifier for the savings account
     * @param startingBalance   the initial balance for the savings account
     * @param accountHolder     the Person object representing the account holder
     */
    public Saving(int accountNumber, double startingBalance, Person accountHolder) {
        super(accountNumber, startingBalance, accountHolder, "Savings");
    }
    
    /**
     * This method withdraws the specified amount from the savings account.
     * and checks if the withdrawal, using the method allowedToWithdraw, is allowed based on the current balance.
     *
     * @param amount the amount to be withdrawn
     * @return true if the withdrawal was successful; false if there were insufficient funds
     */

    @Override
    public boolean withdraw(double amount){
        if(allowedToWithdraw(amount)){
            setBalance(getBalance() - amount);
            return true;
        }
        System.out.println("Insufficient funds. " +
                   "Savings account balance: $" + getBalance());
        return false;
    }

    
    @Override 
    public boolean allowedToWithdraw(double amount){
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
