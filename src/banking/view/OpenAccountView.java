package banking.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class OpenAccountView extends Dialog<OpenAccountView.Request> {
    public record Request(
            String accountType,
            String accountNumber,
            String branch,
            String openingDeposit
    ) {
    }

    public OpenAccountView() {
        setTitle("Open Account");
        setHeaderText("Create an account for the current customer");

        ComboBox<String> type = new ComboBox<>();
        type.getItems().addAll("Savings", "Investment", "Cheque");
        type.getSelectionModel().selectFirst();

        TextField number = new TextField();
        number.setPromptText("Unique account number");

        TextField branch = new TextField("Main Branch");

        TextField deposit = new TextField();
        deposit.setPromptText("Opening deposit in BWP");

        Label rules = new Label(
                "Investment: minimum opening deposit BWP 500.\n"
                + "Cheque: employed individuals only."
        );
        rules.setWrapText(true);

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(14);
        form.setPadding(new Insets(20));

        form.addRow(0, new Label("Account type"), type);
        form.addRow(1, new Label("Account number"), number);
        form.addRow(2, new Label("Branch"), branch);
        form.addRow(3, new Label("Opening deposit"), deposit);
        form.add(rules, 0, 4, 2, 1);

        getDialogPane().setContent(form);

        ButtonType create = new ButtonType(
                "Create Account", ButtonBar.ButtonData.OK_DONE
        );

        getDialogPane().getButtonTypes().addAll(
                create, ButtonType.CANCEL
        );

        setResultConverter(button -> {
            if (button != create) {
                return null;
            }

            return new Request(
                    type.getValue(),
                    number.getText().trim(),
                    branch.getText().trim(),
                    deposit.getText().trim()
            );
        });
    }
}