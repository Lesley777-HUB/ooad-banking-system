package banking.service;

import banking.dao.AccountDAO;
import banking.dao.CustomerDAO;
import banking.dao.Database;
import banking.dao.TransactionDAO;
import banking.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankingService {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public void initialize() throws SQLException {
        Database.initialize();

        if (customerDAO.findById("C001") == null) {
            customerDAO.create(new IndividualCustomer(
                    "C001", "Gaborone", "71234567",
                    "Alex", "Dube", "Employed",
                    "Example Company", "Main Road, Gaborone"
            ));
        }

        Customer demo = customerDAO.findById("C001");

        if (accountDAO.findById("S001") == null) {
            createAccount(new SavingsAccount(
                    "S001", 1000.00, "Main Branch", demo));
        }

        if (accountDAO.findById("I001") == null) {
            createAccount(new InvestmentAccount(
                    "I001", 500.00, "Main Branch", demo));
        }

        if (accountDAO.findById("CH001") == null) {
            createAccount(new ChequeAccount(
                    "CH001", 1000.00, "Main Branch", demo));
        }

        for (int i = 2; i <= 10; i++) {
            String customerId = String.format("C%03d", i);
            String accountNumber = String.format("S%03d", i);

            if (customerDAO.findById(customerId) == null) {
                customerDAO.create(new CompanyCustomer(
                        customerId, "Gaborone", "70000000",
                        "Sample Company " + i, "REG-" + i
                ));
            }

            if (accountDAO.findById(accountNumber) == null) {
                Customer owner = customerDAO.findById(customerId);
                createAccount(new SavingsAccount(
                        accountNumber, 1000.00,
                        "Main Branch", owner
                ));
            }
        }
    }

    private void createAccount(Account account) throws SQLException {
        accountDAO.create(account);
        transactionDAO.saveAccountState(account);
    }

    public Customer loadCustomer(String customerId) throws SQLException {
        Customer customer = customerDAO.findById(customerId);

        if (customer == null) {
            throw new SQLException("Customer not found.");
        }

        for (AccountDAO.AccountRecord row :
                accountDAO.findByCustomer(customerId)) {

            Account account = switch (row.accountType()) {
                case "Savings" -> new SavingsAccount(
                        row.accountNumber(), 0, row.branch(), customer);
                case "Investment" -> new InvestmentAccount(
                        row.accountNumber(), 500, row.branch(), customer);
                case "Cheque" -> new ChequeAccount(
                        row.accountNumber(), 0, row.branch(), customer);
                default -> throw new SQLException("Unknown account type.");
            };

            List<Transaction> history = new ArrayList<>();

            for (TransactionDAO.TransactionRecord saved :
                    transactionDAO.findByAccount(row.accountNumber())) {
                history.add(new Transaction(
                        saved.transactionId(),
                        saved.amount(),
                        saved.transactionDate(),
                        saved.transactionType(),
                        account
                ));
            }

            account.restoreState(row.balance(), history);
        }

        return customer;
    }

    public void save(Account account) throws SQLException {
        transactionDAO.saveAccountState(account);
    }
}