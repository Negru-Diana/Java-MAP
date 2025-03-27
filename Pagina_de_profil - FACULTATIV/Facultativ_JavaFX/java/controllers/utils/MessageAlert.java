package com.example.facultativ_javafx.controllers.utils;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MessageAlert {
    //Clasa pentru afisarea mesajelor de tip Alert

    //Metoda pentru a afisa o alerta personalizata
    public static void showMessage(Stage owner, Alert.AlertType type, String title, String text) {
        Alert message = new Alert(type);
        message.setHeaderText(title);
        message.setContentText(text);
        message.initOwner(owner);
        message.showAndWait();
    }


    //Metoda pentru a afisa o alerta de eroare
    public static void showErrorMessage(Stage owner, String text){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.initOwner(owner);
        message.setTitle("Error");
        message.setContentText(text);
        message.showAndWait();
    }
}
