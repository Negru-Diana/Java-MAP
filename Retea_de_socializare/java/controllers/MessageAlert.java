package com.example.retea_de_socializare.controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class MessageAlert {

    public static void showMessage(Stage owner, Alert.AlertType type, String title, String text) {
        Alert message = new Alert(type);
        message.setHeaderText(title);
        message.setContentText(text);
        message.initOwner(owner);
        message.showAndWait();
    }

    public static void showErrorMessage(Stage owner, String text){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setTitle("Error");
        message.setContentText(text);
        message.showAndWait();
    }

    public static boolean showYesNoConfirmationMessage(Stage owner, String title, String text){
        // Creez o alerta de tip CONFIRMATION
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.initOwner(owner);
        confirmation.setTitle(title);
        confirmation.setContentText(text);

        // Schimb butoanele implicite "Ok" si "Cancel" cu butoanele "Yes" si "No"
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        confirmation.getButtonTypes().setAll(yesButton, noButton);

        // Astept alegerea utilizatorului si returnez:
        // true - daca utilizatorul a ales "Yes"
        // false - daca utilizatorul a ales "No"
        return confirmation.showAndWait().filter(response -> response == yesButton).isPresent();
    }
}
