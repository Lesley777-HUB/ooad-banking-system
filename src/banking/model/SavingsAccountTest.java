package banking.model;

public class SavingsAccountTest {
    public static void main(String[] args) {
        Customer customer = new CompanyCustomer(
                "C001",
                "Gaborone",
                "71234567",
                "Example Company",
                "REG001"
        );

        SavingsAccount account = new SavingsAccount(
                "S001", 1000.00, "Main Branch", customer
        );

        System.out.println("Opening balance: " + account.getBalance());
        System.out.println("Monthly interest: "
                + account.calculateMonthlyInterest());

        account.creditMonthlyInterest();

        System.out.println("Balance after interest: "
                + account.getBalance());
        System.out.println("Customer accounts: "
                + customer.getAccounts().size());
    }
}