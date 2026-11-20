package banking.dao;

import banking.model.Account;
import banking.model.ChequeAccount;
import banking.model.InvestmentAccount;
import banking.model.SavingsAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public record AccountRecord(
            String accountNumber,
            String accountType,
            double balance,
            String branch,
            String customerId,
            String employerName,
            String employerAddress
    ) {
    }

    public void create(Account account) throws SQLException {
        String sql = """
            INSERT INTO accounts (
                account_number, account_type, balance, branch,
                customer_id, employer_name, employer_address
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.setString(1, account.getAccountNumber());
            statement.setString(2, accountType(account));
            statement.setDouble(3, account.getBalance());
            statement.setString(4, account.getBranch());
            statement.setString(5, account.getOwner().getCustomerId());

            if (account instanceof ChequeAccount cheque) {
                statement.setString(6, cheque.getEmployerName());
                statement.setString(7, cheque.getEmployerAddress());
            } else {
                statement.setNull(6, Types.VARCHAR);
                statement.setNull(7, Types.VARCHAR);
            }

            statement.executeUpdate();
        }
    }

    public AccountRecord findById(String accountNumber)
            throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM accounts WHERE account_number = ?")) {
            statement.setString(1, accountNumber);

            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? readRecord(result) : null;
            }
        }
    }

    public List<AccountRecord> findByCustomer(String customerId)
            throws SQLException {
        List<AccountRecord> records = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     """
                     SELECT * FROM accounts
                     WHERE customer_id = ?
                     ORDER BY account_number
                     """)) {
            statement.setString(1, customerId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    records.add(readRecord(result));
                }
            }
        }

        return records;
    }

    public void update(Account account) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     """
                     UPDATE accounts SET balance = ?
                     WHERE account_number = ?
                     """)) {
            statement.setDouble(1, account.getBalance());
            statement.setString(2, account.getAccountNumber());

            if (statement.executeUpdate() != 1) {
                throw new SQLException("Account not found.");
            }
        }
    }

    public void delete(String accountNumber) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM accounts WHERE account_number = ?")) {
            statement.setString(1, accountNumber);

            if (statement.executeUpdate() != 1) {
                throw new SQLException("Account not found.");
            }
        }
    }

    private String accountType(Account account) throws SQLException {
        if (account instanceof SavingsAccount) {
            return "Savings";
        }

        if (account instanceof InvestmentAccount) {
            return "Investment";
        }

        if (account instanceof ChequeAccount) {
            return "Cheque";
        }

        throw new SQLException("Unsupported account type.");
    }

    private AccountRecord readRecord(ResultSet result)
            throws SQLException {
        return new AccountRecord(
                result.getString("account_number"),
                result.getString("account_type"),
                result.getDouble("balance"),
                result.getString("branch"),
                result.getString("customer_id"),
                result.getString("employer_name"),
                result.getString("employer_address")
        );
    }
}