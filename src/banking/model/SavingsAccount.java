package banking.model;

public class SavingsAccount extends Account
        implements InterestBearing {

    private final double monthlyInterestRate = 0.0005;

    public SavingsAccount(String accountNumber, double openingBalance,
                          String branch, Customer owner) {
        super(accountNumber, openingBalance, branch, owner);
        owner.addAccount(this);
    }

    @Override
    public double calculateMonthlyInterest() {
        return balance * monthlyInterestRate;
    }

    @Override
    public void creditMonthlyInterest() {
        double interest = calculateMonthlyInterest();

        if (interest > 0) {
            deposit(interest);
        }
    }

    @Override
    public String getAccountDetails() {
        return "Savings Account: " + accountNumber
                + " | Branch: " + branch
                + " | Balance: BWP " + balance;
    }
}