package banking.model;

public class IndividualCustomer extends Customer {
    private final String firstName;
    private final String surname;
    private final String employmentStatus;
    private final String employerName;
    private final String employerAddress;

    public IndividualCustomer(String customerId, String address,
                              String phoneNumber, String firstName,
                              String surname, String employmentStatus,
                              String employerName, String employerAddress) {
        super(customerId, address, phoneNumber);

        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException(
                "First name is required."
            );
        }

        if (surname == null || surname.isBlank()) {
            throw new IllegalArgumentException(
                "Surname is required."
            );
        }

        if (employmentStatus == null ||
            !(employmentStatus.equalsIgnoreCase("Employed") ||
              employmentStatus.equalsIgnoreCase("Unemployed"))) {
            throw new IllegalArgumentException(
                "Employment status must be Employed or Unemployed."
            );
        }

        if (employmentStatus.equalsIgnoreCase("Employed")) {
            if (employerName == null || employerName.isBlank() ||
                employerAddress == null || employerAddress.isBlank()) {
                throw new IllegalArgumentException(
                    "Employed customers must provide employer details."
                );
            }
        }

        this.firstName = firstName;
        this.surname = surname;
        this.employmentStatus = employmentStatus;
        this.employerName = isEmployed() ? employerName : "";
        this.employerAddress = isEmployed() ? employerAddress : "";
    }

    @Override
    public String getCustomerType() {
        return "Individual";
    }

    public boolean isEmployed() {
        return employmentStatus.equalsIgnoreCase("Employed");
    }

    public String getEmployerDetails() {
        if (!isEmployed()) {
            return "Not employed";
        }

        return employerName + ", " + employerAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public String getEmployerName() {
        return employerName;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }
}