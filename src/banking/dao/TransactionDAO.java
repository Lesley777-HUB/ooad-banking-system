package banking.dao;

import banking.model.Account;
import banking.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public record TransactionRecord(
            String transactionId,
            double amount,
            Timestamp transactionDate,
            String transactionType,
            String accountNumber
    ) {
    }

    public List<TransactionRecord> findByAccount(String accountNumber)
            throws SQLException {
        List<TransactionRecord> records = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     """
                     SELECT * FROM transactions
                     WHERE account_number = ?
                     ORDER BY transaction_date, transaction_id
                     """)) {
            statement.setString(1, accountNumber);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    records.add(new TransactionRecord(
                            result.getString("transaction_id"),
                            result.getDouble("amount"),
                            result.getTimestamp("transaction_date"),
                            result.getString("transaction_type"),
                            result.getString("account_number")
                    ));
                }
            }
        }

        return records;
    }

    public void saveAccountState(Account account) throws SQLException {
        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);

            try {
                updateBalance(connection, account);

                for (Transaction transaction : account.getTransactions()) {
                    insertIfMissing(connection, transaction);
                }

                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    private void updateBalance(Connection connection, Account account)
            throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                """
                UPDATE accounts SET balance = ?
                WHERE account_number = ? AND customer_id = ?
                """)) {
            statement.setDouble(1, account.getBalance());
            statement.setString(2, account.getAccountNumber());
            statement.setString(3, account.getOwner().getCustomerId());

            if (statement.executeUpdate() != 1) {
                throw new SQLException("Account not found for this customer.");
            }
        }
    }

    private void insertIfMissing(Connection connection,
                                 Transaction transaction)
            throws SQLException {
        String sql = """
            INSERT INTO transactions (
                transaction_id, amount, transaction_date,
                transaction_type, account_number
            )
            SELECT ?, ?, ?, ?, ?
            WHERE NOT EXISTS (
                SELECT 1 FROM transactions WHERE transaction_id = ?
            )
            """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.setString(1, transaction.getTransactionId());
            statement.setDouble(2, transaction.getAmount());
            statement.setTimestamp(3, new Timestamp(
                    transaction.getTransactionDate().getTime()));
            statement.setString(4, transaction.getTransactionType());
            statement.setString(5,
                    transaction.getAccount().getAccountNumber());
            statement.setString(6, transaction.getTransactionId());
            statement.executeUpdate();
        }
    }
}