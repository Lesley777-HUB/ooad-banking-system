package banking.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Account {
    protected final String accountNumber;
    protected double balance;
    protected final String branch;
    protected final Customer owner;

    private final List<Transaction> transactions = new ArrayList<>();

    protected Account(String accountNumber, double openingBalance,
                      String branch, Customer owner) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Account number is required.");
        }

        if (!Double.isFinite(openingBalance) || openingBalance < 0) {
            throw new IllegalArgumentException(
                    "Opening balance must be finite and non-negative.");
        }

        if (branch == null || branch.isBlank()) {
            throw new IllegalArgumentException("Branch is required.");
        }

        if (owner == null) {
            throw new IllegalArgumentException(
                    "An account must have a customer.");
        }

        this.accountNumber = accountNumber;
        this.balance = openingBalance;
        this.branch = branch;
        this.owner = owner;

        if (openingBalance > 0) {
            transactions.add(new Transaction(
                    openingBalance, "OPENING_DEPOSIT", this));
        }
    }

    public void deposit(double amount) {
        creditFunds(amount, "DEPOSIT");
    }

    public void deposit(double amount, String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException(
                    "Deposit description is required.");
        }

        creditFunds(amount, "DEPOSIT: " + description.trim());
    }

    private void creditFunds(double amount, String type) {
        validateAmount(amount);

        double newBalance = balance + amount;

        if (!Double.isFinite(newBalance)) {
            throw new IllegalArgumentException(
                    "The resulting balance is too large.");
        }

        Transaction transaction = new Transaction(amount, type, this);
        balance = newBalance;
        transactions.add(transaction);
    }

    protected void withdrawFunds(double amount) {
        validateAmount(amount);

        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        Transaction transaction = new Transaction(
                amount, "WITHDRAWAL", this);

        balance -= amount;
        transactions.add(transaction);
    }

    public void applyInterestRate(double rate) {
        if (!(this instanceof InterestBearing)) {
            throw new IllegalArgumentException(
                    "This account does not earn interest.");
        }

        if (!Double.isFinite(rate) || rate < 0) {
            throw new IllegalArgumentException(
                    "Interest rate must be finite and non-negative.");
        }

        double interest = balance * rate;

        if (interest > 0) {
            creditFunds(interest, "INTEREST");
        }
    }

    protected void validateAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be finite and greater than zero.");
        }
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
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