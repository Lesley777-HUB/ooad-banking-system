package banking.model;

public class TransactionTest {
    public static void main(String[] args) {
        Customer customer = new CompanyCustomer(
                "C010", "Gaborone", "71234567",
                "Test Company", "REG010"
        );

        InvestmentAccount account = new InvestmentAccount(
                "I010", 500.00, "Main Branch", customer
        );

        account.deposit(100.00, "Extra savings");
        account.withdraw(50.00);
        account.creditMonthlyInterest();

        if (Math.abs(account.getBalance() - 577.50) > 0.000001) {
            throw new AssertionError("Incorrect balance.");
        }

        if (account.getTransactions().size() != 4) {
            throw new AssertionError("Expected four transactions.");
        }

        try {
            account.withdraw(1000.00);
            throw new AssertionError("Overdraft was allowed.");
        } catch (IllegalArgumentException expected) {
            // Rejected withdrawals must not change the account.
        }

        if (account.getTransactions().size() != 4
                || Math.abs(account.getBalance() - 577.50) > 0.000001) {
            throw new AssertionError("Rejected withdrawal changed account.");
        }

        for (Transaction transaction : account.getTransactions()) {
            System.out.println(
                    transaction.getTransactionType()
                    + ": " + transaction.getAmount()
            );
        }

        System.out.println("Transaction test passed.");
    }
}