package banking.model;

public abstract class Account {
    protected final String accountNumber;
    protected double balance;
    protected final String branch;
    protected final Customer owner;

    protected Account(String accountNumber, double openingBalance,
                      String branch, Customer owner) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException(
                "Account number is required."
            );
        }

        if (!Double.isFinite(openingBalance) || openingBalance < 0) {
            throw new IllegalArgumentException(
                "Opening balance must be a finite, non-negative amount."
            );
        }

        if (branch == null || branch.isBlank()) {
            throw new IllegalArgumentException(
                "Branch is required."
            );
        }

        if (owner == null) {
            throw new IllegalArgumentException(
                "An account must have a customer."
            );
        }

        this.accountNumber = accountNumber;
        this.balance = openingBalance;
        this.branch = branch;
        this.owner = owner;
    }

    public void deposit(double amount) {
        validateAmount(amount);

        double newBalance = balance + amount;

        if (!Double.isFinite(newBalance)) {
            throw new IllegalArgumentException(
                "The resulting balance is too large."
            );
        }

        balance = newBalance;
    }

    protected void validateAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                "Amount must be a finite number greater than zero."
            );
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBranch() {
        return branch;
    }

    public Customer getOwner() {
        return owner;
    }

    public abstract String getAccountDetails();
}