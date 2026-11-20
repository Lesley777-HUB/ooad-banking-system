package banking.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class AccountView extends BorderPane {
    private final ComboBox<String> accountSelector = new ComboBox<>();
    private final TextField amountField = new TextField();
    private final Button depositButton = new Button("Deposit");
    private final Button withdrawButton = new Button("Withdraw");
    private final Button interestButton = new Button("Credit monthly interest");
    private final Button logoutButton = new Button("Log out");
    private final Label balanceLabel = new Label("Balance: —");
    private final Label messageLabel = new Label();
    private final TextArea historyArea = new TextArea();
    private final Button openAccountButton = new Button("Open Account");
    
    public AccountView() {
        setPadding(new Insets(24));
        setStyle("-fx-background-color: #eef2f7;");

        Label title = new Label("Account Management");
        title.setStyle(
                "-fx-font-size: 26px; -fx-font-weight: bold;"
                + "-fx-text-fill: #17365d;"
        );

        HBox header = new HBox(20, title, openAccountButton, logoutButton);
        header.setPadding(new Insets(0, 0, 24, 0));
        setTop(header);

        accountSelector.setPromptText("Select an account");
        accountSelector.setMaxWidth(Double.MAX_VALUE);

        balanceLabel.setStyle("-fx-font-size: 22px;");
        amountField.setPromptText("Amount in BWP");

        HBox actions = new HBox(12, depositButton, withdrawButton);
    

        historyArea.setEditable(false);
        historyArea.setPromptText("Transaction history");
        historyArea.setPrefRowCount(10);
        VBox.setVgrow(historyArea, Priority.ALWAYS);

        messageLabel.setWrapText(true);

        VBox content = new VBox(
                16, new Label("Account"), accountSelector,
                balanceLabel, new Label("Transaction amount"),
                amountField, actions, interestButton,
                messageLabel, new Label("Transaction history"),
                historyArea
        );

        setCenter(content);
    }

    public ComboBox<String> getAccountSelector() {
        return accountSelector;
    }

    public String getAmountText() {
        return amountField.getText();
    }

    public void clearAmount() {
        amountField.clear();
    }

    public Button getDepositButton() {
        return depositButton;
    }

    public Button getWithdrawButton() {
        return withdrawButton;
    }

    public Button getInterestButton() {
        return interestButton;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }

    public void showBalance(String balance) {
        balanceLabel.setText("Balance: BWP " + balance);
    }

    public void showHistory(String history) {
        historyArea.setText(history);
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
    }
        public Button getOpenAccountButton() {
        return openAccountButton;
    }
}
 