package banking.controller;

import banking.view.LoginView;

public class LoginController {
    public LoginController(LoginView view, Runnable onSuccess) {
        view.getLoginButton().setOnAction(event -> {
            String username = view.getUsername().trim();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.showMessage("Enter your username and password.");
                return;
            }

            if ("demo".equals(username) && "demo123".equals(password)) {
                onSuccess.run();
            } else {
                view.showMessage("Incorrect username or password.");
            }
        });
    }
}  