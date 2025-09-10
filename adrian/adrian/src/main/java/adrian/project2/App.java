package adrian.project2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;


public class App extends Application {

    @Override
    public void start(@SuppressWarnings("exports") Stage stage) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Visa", "MasterCard", "American Express", "Discover");

        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Enter credit card number");

        Button validateButton = new Button("Validate");
        Button clearButton = new Button("Clear");
        Button exitButton = new Button("Exit");

        Label resultLabel = new Label("");

        // Updating field length based on selected card type
        comboBox.setOnAction(e -> adjustFieldLength(comboBox, cardNumberField));

        // Validating card
        validateButton.setOnAction(e -> validateCard(comboBox, cardNumberField, resultLabel));

        // Clearing input fields
        clearButton.setOnAction(e -> {
            comboBox.getSelectionModel().clearSelection();
            cardNumberField.clear();
            resultLabel.setText("");
        });

        // Exit application
        exitButton.setOnAction(e -> stage.close());

        // Layout for input field and Clear button (horizontal)
        HBox inputRow = new HBox(10, cardNumberField, clearButton);
        inputRow.setAlignment(Pos.CENTER);

        // Layout for all elements
        VBox vbox = new VBox(10, comboBox, inputRow, validateButton, resultLabel, exitButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 20;");

        // Set Scene
        stage.setScene(new Scene(vbox, 400, 300));
        stage.setTitle("Credit Card Validator");
        stage.show();
    }

    // Handles the validation logic by calling the CardValidator
    private void validateCard(ComboBox<String> comboBox, TextField cardNumberField, Label resultLabel) {
        String cardType = comboBox.getValue();
        String cardNumber = cardNumberField.getText().trim();

        // Input error handling
        if (cardType == null || cardType.isEmpty()) {
            displayError(resultLabel, "Error: Please select a card type.");
            return;
        }

        if (cardNumber.isEmpty()) {
            displayError(resultLabel, "Error: Please enter a card number.");
            return;
        }

        CreditCard card = CreditCard.getCard(cardType, cardNumber);
        if (card == null) {
            displayError(resultLabel, "Error: Invalid card type selected.");
            return;
        }

        String validationMessage = CardValidator.validate(card);
        if (validationMessage.startsWith("Error")) {
            displayError(resultLabel, validationMessage);
        } else {
            displaySuccess(resultLabel, validationMessage);
        }
    }

    // Adjusts the maximum length of the text field based on the selected card type
    private void adjustFieldLength(ComboBox<String> comboBox, TextField cardNumberField) {
        String selectedType = comboBox.getValue();
        if (selectedType == null) return;

        int maxLength = switch (selectedType) {
            case "Visa" -> 16;
            case "MasterCard" -> 16;
            case "American Express" -> 15;
            case "Discover" -> 16;
            default -> 16;
        };

        // Restrict input to the selected card's length
        final int MAX = maxLength;
        cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > MAX) {
                cardNumberField.setText(newValue.substring(0, MAX));
            }
        });
    }

    // Displays an error message in red
    private void displayError(Label label, String message) {
        label.setText(message);
        label.setTextFill(Color.RED);
    }

    // Displays a success message in green
    private void displaySuccess(Label label, String message) {
        label.setText(message);
        label.setTextFill(Color.GREEN);
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setRoot(String string) {
        throw new UnsupportedOperationException("Unimplemented method 'setRoot'");
    }
}
