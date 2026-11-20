package banking.dao;

import banking.model.*;
import banking.service.BankingService;

import java.sql.*;
import java.util.UUID;

public class PersistenceTest {
    public static void main(String[] args) throws Exception {
        Database.initialize();

        String id = "TEST-" + UUID.randomUUID();
        String number = "ACCOUNT-" + UUID.randomUUID();

        CustomerDAO customers = new CustomerDAO();
        AccountDAO accounts = new AccountDAO();
        BankingService service = new BankingService();

        try {
            Customer customer = new CompanyCustomer(
                    id, "Gaborone", "71234567",
                    "Integration Test Company", id
            );

            customers.create(customer);

            InvestmentAccount account = new InvestmentAccount(
                    number, 500.00, "Test Branch", customer
            );

            accounts.create(account);
            service.save(account);

            account.deposit(100.00);
            account.withdraw(200.00);
            account.creditMonthlyInterest();
            service.save(account);

            // A repeated save must not duplicate transactions.
            service.save(account);

            Customer restored = service.loadCustomer(id);
            Account loaded = restored.getAccounts().get(0);

            check(loaded instanceof InvestmentAccount,
                    "Account type was not restored.");

            check(Math.abs(loaded.getBalance() - 420.00) < 0.000001,
                    "Saved balance was not restored.");

            check(loaded.getTransactions().size() == 4,
                    "History is missing or duplicated.");

            check(loaded.getTransactions().get(0)
                            .getTransactionId().equals(
                                    account.getTransactions().get(0)
                                            .getTransactionId()),
                    "Original transaction ID was not preserved.");

            System.out.println("Balance below BWP 500 restored: PASS");
            System.out.println("Transaction history restored: PASS");
            System.out.println("Repeated save avoids duplicates: PASS");

            try {
                ((Withdrawable) loaded).withdraw(99999);
                throw new AssertionError("Overdraft was allowed.");
            } catch (IllegalArgumentException expected) {
                // Expected rejection.
            }

            service.save(loaded);

            Account reloaded = service.loadCustomer(id)
                    .getAccounts().get(0);

            check(Math.abs(reloaded.getBalance() - 420.00) < 0.000001,
                    "Rejected withdrawal changed saved balance.");

            check(reloaded.getTransactions().size() == 4,
                    "Rejected withdrawal changed saved history.");

            System.out.println("Rejected withdrawal leaves data unchanged: PASS");
            System.out.println("Persistence integration test: PASS");
        } finally {
            try (Connection connection = Database.getConnection()) {
                connection.setAutoCommit(false);

                try {
                    delete(connection,
                            "DELETE FROM transactions WHERE account_number = ?",
                            number);
                    delete(connection,
                            "DELETE FROM accounts WHERE account_number = ?",
                            number);
                    delete(connection,
                            "DELETE FROM customers WHERE customer_id = ?",
                            id);
                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                    throw e;
                }
            }
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void delete(Connection connection, String sql,
                               String value) throws SQLException {
        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            statement.setString(1, value);
            statement.executeUpdate();
        }
    }
}