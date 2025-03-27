package com.example.retea_de_socializare;

import com.example.retea_de_socializare.controllers.LoginController;
import com.example.retea_de_socializare.controllers.MessageAlert;
import com.example.retea_de_socializare.service.ObservableService;
import com.example.retea_de_socializare.service.ServiceFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class HelloApplication extends Application {
    private ObservableService service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Creez service-ul pe care il voi utiliza
        service = ServiceFactory.getObservableService();
        if(service == null) {
            MessageAlert.showErrorMessage(stage, "A aparut o eroare! Verificati conexiunea la baza de date.");
            return;
        }

        initView(stage);
        stage.show();
        stage.toFront();

    }

    private void initView(Stage primaryStage) throws IOException{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/retea_de_socializare/views/login.fxml"));
            Parent root = loader.load();

            // Adaug fisierul CSS
            root.getStylesheets().add(getClass().getResource("/com/example/retea_de_socializare/css/viewStyle.css").toExternalForm());

            LoginController controller = loader.getController();
            controller.setStage(primaryStage);
            controller.setService(service);

            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root));
        }
        catch (IOException e) {
            MessageAlert.showErrorMessage(primaryStage, e.getMessage());
        }
    }
}