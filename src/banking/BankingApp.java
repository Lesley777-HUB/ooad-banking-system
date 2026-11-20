package banking;

import banking.controller.AccountController;
import banking.controller.LoginController;
import banking.model.Customer;
import banking.service.BankingService;
import banking.view.AccountView;
import banking.view.LoginView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankingApp extends Application {
    private Stage stage;
    private Customer customer;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        BankingService service = new BankingService();
        service.initialize();
        customer = service.loadCustomer("C001");

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
        try {
            customer = new BankingService().loadCustomer("C001");

            AccountView view = new AccountView();
            new AccountController(view, customer, this::showLogin);
            stage.setScene(new Scene(view, 900, 650));
        } catch (java.sql.SQLException | IllegalArgumentException e) {
            e.printStackTrace();

            javafx.scene.control.Alert alert =
                    new javafx.scene.control.Alert(
                            javafx.scene.control.Alert.AlertType.ERROR
                    );

            alert.initOwner(stage);
            alert.setTitle("Account Loading Error");
            alert.setHeaderText("Could not load saved accounts");
            alert.setContentText(
                    "Check the terminal for details, then try again."
            );
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}