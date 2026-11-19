package banking.model;

public class ChequeAccount extends Account implements Withdrawable {
    private final String employerName;
    private final String employerAddress;

    public ChequeAccount(String accountNumber, double openingBalance,
                         String branch, Customer owner) {
        super(accountNumber, openingBalance, branch, owner);

        if (!(owner instanceof IndividualCustomer)) {
            throw new IllegalArgumentException(
                    "Cheque accounts require an individual customer."
            );
        }

        IndividualCustomer individual = (IndividualCustomer) owner;

        if (!individual.isEmployed()) {
            throw new IllegalArgumentException(
                    "Cheque accounts require an employed customer."
            );
        }

        employerName = individual.getEmployerName();
        employerAddress = individual.getEmployerAddress();

        owner.addAccount(this);
    }

    @Override
    public void withdraw(double amount) {
        validateAmount(amount);

        if (amount > balance) {
            throw new IllegalArgumentException(
                    "Insufficient funds."
            );
        }

        balance -= amount;
    }

    @Override
    public String getAccountDetails() {
        return "Cheque Account: " + accountNumber
                + " | Branch: " + branch
                + " | Balance: BWP " + balance
                + " | Employer: " + employerName
                + " | Employer address: " + employerAddress;
    }

    public String getEmployerName() {
        return employerName;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }
}