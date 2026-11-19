package banking;

import banking.controller.AccountController;
import banking.controller.LoginController;
import banking.model.ChequeAccount;
import banking.model.IndividualCustomer;
import banking.model.InvestmentAccount;
import banking.model.SavingsAccount;
import banking.view.AccountView;
import banking.view.LoginView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankingApp extends Application {
    private Stage stage;
    private IndividualCustomer customer;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        customer = new IndividualCustomer(
                "C001", "Gaborone", "71234567",
                "Alex", "Dube", "Employed",
                "Example Company", "Main Road, Gaborone"
        );

        new SavingsAccount("S001", 1000.00, "Main Branch", customer);
        new InvestmentAccount("I001", 500.00, "Main Branch", customer);
        new ChequeAccount("CH001", 1000.00, "Main Branch", customer);

        stage.setTitle("Banking Management System");
        stage.setMinWidth(700);
        stage.setMinHeight(600);

        showLogin();
        stage.show();
    }

    private void showLogin() {
        LoginView view = new LoginView();
        new LoginController(view, this::showAccounts);
        stage.setScene(new Scene(view, 900, 650));
    }

    private void showAccounts() {
        AccountView view = new AccountView();
        new AccountController(view, customer, this::showLogin);
        stage.setScene(new Scene(view, 900, 650));
    }

    public static void main(String[] args) {
        launch(args);
    }
}