package banking.model;

public class InvestmentAccountTest {
    public static void main(String[] args) {
        Customer customer = new CompanyCustomer(
                "C002", "Gaborone", "71234567",
                "Example Company", "REG002"
        );

        InvestmentAccount account = new InvestmentAccount(
                "I001", 500.00, "Main Branch", customer
        );

        System.out.println("Opening balance: " + account.getBalance());
        System.out.println("Monthly interest: "
                + account.calculateMonthlyInterest());

        account.creditMonthlyInterest();
        System.out.println("After interest: " + account.getBalance());

        account.deposit(100.00);
        System.out.println("After deposit: " + account.getBalance());

        account.withdraw(200.00);
        System.out.println("After withdrawal: " + account.getBalance());

        try {
            account.withdraw(1000.00);
            throw new AssertionError("Overdraft was allowed.");
        } catch (IllegalArgumentException e) {
            System.out.println("Overdraft rejected.");
        }

        try {
            account.withdraw(-10.00);
            throw new AssertionError("Negative withdrawal was allowed.");
        } catch (IllegalArgumentException e) {
            System.out.println("Negative withdrawal rejected.");
        }

        try {
            new InvestmentAccount(
                    "I002", 499.00, "Main Branch", customer
            );
            throw new AssertionError("Opening below BWP 500 was allowed.");
        } catch (IllegalArgumentException e) {
            System.out.println("Opening below BWP 500 rejected.");
        }

        System.out.println("Final balance: " + account.getBalance());
        System.out.println("Customer accounts: "
                + customer.getAccounts().size());
    }
}