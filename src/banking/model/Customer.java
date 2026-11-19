package banking.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Customer {
    protected final String customerId;
    protected String address;
    protected String phoneNumber;
    protected final List<Account> accounts;

    protected Customer(String customerId, String address,
                       String phoneNumber) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException(
                "Customer ID is required."
            );
        }

        this.customerId = customerId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.accounts = new ArrayList<>();
    }

    public abstract String getCustomerType();

    public void addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException(
                "Account is required."
            );
        }

        if (account.getOwner() != this) {
            throw new IllegalArgumentException(
                "Account must belong to this customer."
            );
        }

        if (!accounts.contains(account)) {
            accounts.add(account);
        }
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}