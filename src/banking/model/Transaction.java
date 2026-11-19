package banking.model;

import java.util.Date;
import java.util.UUID;

public class Transaction {
    private final String transactionId;
    private final double amount;
    private final Date transactionDate;
    private final String transactionType;
    private final Account account;

    public Transaction(double amount, String transactionType,
                       Account account) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException(
                    "Transaction amount must be positive and finite."
            );
        }

        if (transactionType == null || transactionType.isBlank()) {
            throw new IllegalArgumentException(
                    "Transaction type is required."
            );
        }

        if (account == null) {
            throw new IllegalArgumentException(
                    "Transaction must belong to an account."
            );
        }

        this.transactionId = UUID.randomUUID().toString();
        this.amount = amount;
        this.transactionDate = new Date();
        this.transactionType = transactionType;
        this.account = account;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public Date getTransactionDate() {
        return new Date(transactionDate.getTime());
    }

    public String getTransactionType() {
        return transactionType;
    }

    public Account getAccount() {
        return account;
    }

    public String getTransactionDetails() {
        return transactionId
                + " | " + transactionType
                + " | BWP " + amount
                + " | Account: " + account.getAccountNumber()
                + " | " + transactionDate;
    }
}