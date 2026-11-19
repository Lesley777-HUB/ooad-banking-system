package banking.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginView extends VBox {
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Button loginButton = new Button("Log in");
    private final Label messageLabel = new Label();

    public LoginView() {
        setSpacing(16);
        setPadding(new Insets(40));
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #eef2f7;");

        Label title = new Label("Banking Management System");
        title.setStyle(
                "-fx-font-size: 28px; -fx-font-weight: bold;"
                + "-fx-text-fill: #17365d;"
        );

        Label subtitle = new Label("Sign in to manage customer accounts");

        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(320);

        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(320);

        loginButton.setDefaultButton(true);
        loginButton.setMaxWidth(320);
        loginButton.setStyle(
                "-fx-background-color: #17365d;"
                + "-fx-text-fill: white; -fx-padding: 12;"
        );

        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(320);
        messageLabel.setStyle("-fx-text-fill: #b42318;");

        getChildren().addAll(
                title, subtitle,
                new Label("Username"), usernameField,
                new Label("Password"), passwordField,
                loginButton, messageLabel
        );
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public void showMessage(String message) {
        messageLabel.setText(message);
    }
}
