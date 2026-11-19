package banking.model;

public class InvestmentAccount extends Account
        implements InterestBearing, Withdrawable {

    private final double monthlyInterestRate = 0.05;
    private static final double MINIMUM_OPENING_DEPOSIT = 500.00;

    public InvestmentAccount(String accountNumber, double openingBalance,
                             String branch, Customer owner) {
        super(accountNumber, openingBalance, branch, owner);

        if (openingBalance < MINIMUM_OPENING_DEPOSIT) {
            throw new IllegalArgumentException(
                    "Investment accounts require at least BWP 500 to open."
            );
        }

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
    public void withdraw(double amount) {
        validateAmount(amount);

        if (amount > balance) {
            throw new IllegalArgumentException(
                    "Insufficient funds."
            );
        }

        balance -= amount;
    }

    @Override
    public String getAccountDetails() {
        return "Investment Account: " + accountNumber
                + " | Branch: " + branch
                + " | Balance: BWP " + balance;
    }
}