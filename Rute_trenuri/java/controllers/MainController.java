package com.example.examen_db.controllers;

import com.example.examen_db.service.ObservableService;
import com.example.examen_db.utils.MessageAlert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    private ObservableService service;
    private Stage stage;

    public void handleButton(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/examen_db/views/clientWindowView.fxml"));
            Parent root = loader.load();

            ClientWindowViewController controller = loader.getController();
            Stage mainStage = new Stage();
            controller.setStage(mainStage);
            controller.setService(service);

            mainStage.setTitle("Main");
            mainStage.setScene(new Scene(root));
            mainStage.show();
            mainStage.toFront();
        }
        catch (IOException e) {
            MessageAlert.showErrorMessage(stage, e.getMessage());
        }
    }


    public void setService(ObservableService service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
