package banking.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
    private static final String URL = "jdbc:h2:./data/banking";

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, "sa", "");
    }

    public static void initialize() throws SQLException {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS customers (
                    customer_id VARCHAR(100) PRIMARY KEY,
                    customer_type VARCHAR(20) NOT NULL,
                    address VARCHAR(500),
                    phone_number VARCHAR(50),
                    first_name VARCHAR(100),
                    surname VARCHAR(100),
                    employment_status VARCHAR(30),
                    employer_name VARCHAR(200),
                    employer_address VARCHAR(500),
                    company_name VARCHAR(200),
                    registration_number VARCHAR(100),
                    CHECK (customer_type IN ('Individual', 'Company'))
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS accounts (
                    account_number VARCHAR(100) PRIMARY KEY,
                    account_type VARCHAR(20) NOT NULL,
                    balance DOUBLE PRECISION NOT NULL,
                    branch VARCHAR(200) NOT NULL,
                    customer_id VARCHAR(100) NOT NULL,
                    employer_name VARCHAR(200),
                    employer_address VARCHAR(500),
                    FOREIGN KEY (customer_id)
                        REFERENCES customers(customer_id),
                    CHECK (balance >= 0),
                    CHECK (account_type IN (
                        'Savings', 'Investment', 'Cheque'
                    ))
                )
                """);

            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS transactions (
                    transaction_id VARCHAR(100) PRIMARY KEY,
                    amount DOUBLE PRECISION NOT NULL,
                    transaction_date TIMESTAMP NOT NULL,
                    transaction_type VARCHAR(500) NOT NULL,
                    account_number VARCHAR(100) NOT NULL,
                    FOREIGN KEY (account_number)
                        REFERENCES accounts(account_number),
                    CHECK (amount > 0)
                )
                """);
        }
    }

    public static void main(String[] args) throws SQLException {
        initialize();
        System.out.println("Database tables created successfully.");
    }
}