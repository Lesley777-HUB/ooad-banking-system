package banking.controller;

import banking.model.Account;
import banking.model.Customer;
import banking.model.InterestBearing;
import banking.model.Transaction;
import banking.model.Withdrawable;
import banking.view.AccountView;

import java.util.List;
import java.util.Locale;

public class AccountController {
    private final AccountView view;
    private final List<Account> accounts;

    public AccountController(AccountView view, Customer customer,
                             Runnable onLogout) {
        this.view = view;
        this.accounts = customer.getAccounts();

        for (Account account : accounts) {
            view.getAccountSelector().getItems().add(
                    account.getAccountNumber()
            );
        }

        view.getAccountSelector().setOnAction(event -> {
            view.showMessage("");
            refresh();
        });

        view.getDepositButton().setOnAction(event ->
                perform(() -> selectedAccount().deposit(readAmount()))
        );

        view.getWithdrawButton().setOnAction(event ->
                perform(() -> {
                    Account account = selectedAccount();

                    if (!(account instanceof Withdrawable)) {
                        throw new IllegalArgumentException(
                                "This account does not allow withdrawals."
                        );
                    }

                    ((Withdrawable) account).withdraw(readAmount());
                })
        );

        view.getInterestButton().setOnAction(event ->
                perform(() -> {
                    Account account = selectedAccount();

                    if (!(account instanceof InterestBearing)) {
                        throw new IllegalArgumentException(
                                "This account does not earn interest."
                        );
                    }

                    ((InterestBearing) account).creditMonthlyInterest();
                })
        );

        view.getLogoutButton().setOnAction(event -> onLogout.run());

        if (!accounts.isEmpty()) {
            view.getAccountSelector().getSelectionModel().selectFirst();
        }

        refresh();
    }

    private Account selectedAccount() {
        int index = view.getAccountSelector()
                .getSelectionModel().getSelectedIndex();

        if (index < 0 || index >= accounts.size()) {
            throw new IllegalArgumentException("Select an account first.");
        }

        return accounts.get(index);
    }

    private double readAmount() {
        try {
            double amount = Double.parseDouble(
                    view.getAmountText().trim()
            );

            if (!Double.isFinite(amount) || amount <= 0) {
                throw new IllegalArgumentException(
                        "Enter an amount greater than zero."
                );
            }

            return amount;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Enter a valid numeric amount."
            );
        }
    }

    private void perform(Runnable operation) {
        try {
            operation.run();
            refresh();
            view.clearAmount();
            view.showMessage("Operation completed successfully.");
        } catch (IllegalArgumentException e) {
            view.showMessage(e.getMessage());
        }
    }

    private void refresh() {
        boolean hasAccount = view.getAccountSelector()
                .getSelectionModel().getSelectedIndex() >= 0;

        view.getDepositButton().setDisable(!hasAccount);
        view.getWithdrawButton().setDisable(true);
        view.getInterestButton().setDisable(true);

        if (!hasAccount) {
            return;
        }

        Account account = selectedAccount();

        view.showBalance(String.format(
                Locale.ROOT, "%.2f", account.getBalance()
        ));

        view.getWithdrawButton().setDisable(
                !(account instanceof Withdrawable)
        );

        view.getInterestButton().setDisable(
                !(account instanceof InterestBearing)
        );

        StringBuilder history = new StringBuilder();

        for (Transaction transaction : account.getTransactions()) {
            history.append(transaction.getTransactionDetails())
                    .append(System.lineSeparator());
        }

        view.showHistory(history.toString());
    }
}