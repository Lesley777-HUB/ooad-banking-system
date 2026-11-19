package banking.model;

public class CompanyCustomer extends Customer {
    private final String companyName;
    private final String registrationNumber;

    public CompanyCustomer(String customerId, String address,
                           String phoneNumber, String companyName,
                           String registrationNumber) {
        super(customerId, address, phoneNumber);

        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException(
                "Company name is required."
            );
        }

        if (registrationNumber == null || registrationNumber.isBlank()) {
            throw new IllegalArgumentException(
                "Registration number is required."
            );
        }

        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
    }

    @Override
    public String getCustomerType() {
        return "Company";
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }
}