package banking.dao;

import banking.model.CompanyCustomer;
import banking.model.Customer;
import banking.model.IndividualCustomer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    public void create(Customer customer) throws SQLException {
        String sql = """
            INSERT INTO customers (
                customer_id, customer_type, address, phone_number,
                first_name, surname, employment_status,
                employer_name, employer_address,
                company_name, registration_number
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            bindCustomer(statement, customer);
            statement.executeUpdate();
        }
    }

    public Customer findById(String customerId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customerId);

            try (ResultSet result = statement.executeQuery()) {
                return result.next() ? readCustomer(result) : null;
            }
        }
    }

    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM customers ORDER BY customer_id");
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                customers.add(readCustomer(result));
            }
        }

        return customers;
    }

    public void update(Customer customer) throws SQLException {
        String sql = """
            UPDATE customers SET
                customer_type = ?, address = ?, phone_number = ?,
                first_name = ?, surname = ?, employment_status = ?,
                employer_name = ?, employer_address = ?,
                company_name = ?, registration_number = ?
            WHERE customer_id = ?
            """;

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            setDetails(statement, customer, 1);
            statement.setString(11, customer.getCustomerId());

            if (statement.executeUpdate() != 1) {
                throw new SQLException("Customer not found.");
            }
        }
    }

    public void delete(String customerId) throws SQLException {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM customers WHERE customer_id = ?")) {
            statement.setString(1, customerId);

            if (statement.executeUpdate() != 1) {
                throw new SQLException("Customer not found.");
            }
        }
    }

    private void bindCustomer(PreparedStatement statement,
                              Customer customer) throws SQLException {
        statement.setString(1, customer.getCustomerId());
        setDetails(statement, customer, 2);
    }

    private void setDetails(PreparedStatement statement,
                            Customer customer, int start)
            throws SQLException {
        statement.setString(start, customer.getCustomerType());
        statement.setString(start + 1, customer.getAddress());
        statement.setString(start + 2, customer.getPhoneNumber());

        for (int i = start + 3; i <= start + 9; i++) {
            statement.setNull(i, Types.VARCHAR);
        }

        if (customer instanceof IndividualCustomer individual) {
            statement.setString(start + 3, individual.getFirstName());
            statement.setString(start + 4, individual.getSurname());
            statement.setString(start + 5, individual.getEmploymentStatus());
            statement.setString(start + 6, individual.getEmployerName());
            statement.setString(start + 7, individual.getEmployerAddress());
        } else if (customer instanceof CompanyCustomer company) {
            statement.setString(start + 8, company.getCompanyName());
            statement.setString(start + 9, company.getRegistrationNumber());
        } else {
            throw new SQLException("Unsupported customer type.");
        }
    }

    private Customer readCustomer(ResultSet result) throws SQLException {
        String id = result.getString("customer_id");
        String address = result.getString("address");
        String phone = result.getString("phone_number");

        return switch (result.getString("customer_type")) {
            case "Individual" -> new IndividualCustomer(
                    id, address, phone,
                    result.getString("first_name"),
                    result.getString("surname"),
                    result.getString("employment_status"),
                    result.getString("employer_name"),
                    result.getString("employer_address")
            );
            case "Company" -> new CompanyCustomer(
                    id, address, phone,
                    result.getString("company_name"),
                    result.getString("registration_number")
            );
            default -> throw new SQLException("Unknown customer type.");
        };
    }
}