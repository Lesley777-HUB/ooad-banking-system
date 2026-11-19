package banking.model;

public class ChequeAccountTest {
    public static void main(String[] args) {
        IndividualCustomer customer = new IndividualCustomer(
                "C003", "Gaborone", "71234567",
                "Alex", "Dube", "Employed",
                "Example Company", "Main Road, Gaborone"
        );

        ChequeAccount account = new ChequeAccount(
                "CH001", 1000.00, "Main Branch", customer
        );

        account.deposit(500.00);
        System.out.println("After deposit: " + account.getBalance());

        account.withdraw(200.00);
        System.out.println("After withdrawal: " + account.getBalance());

        System.out.println("Employer: " + account.getEmployerName());

        try {
            account.withdraw(2000.00);
            throw new AssertionError("Overdraft was allowed.");
        } catch (IllegalArgumentException e) {
            System.out.println("Overdraft rejected.");
        }

        IndividualCustomer unemployed = new IndividualCustomer(
                "C004", "Francistown", "72345678",
                "Sam", "Molefe", "Unemployed", "", ""
        );

        try {
            new ChequeAccount(
                    "CH002", 0.00, "Main Branch", unemployed
            );
            throw new AssertionError("Unemployed customer was accepted.");
        } catch (IllegalArgumentException e) {
            System.out.println("Unemployed customer rejected.");
        }

        System.out.println("Customer accounts: "
                + customer.getAccounts().size());
        System.out.println("Unemployed customer accounts: "
                + unemployed.getAccounts().size());
    }
}