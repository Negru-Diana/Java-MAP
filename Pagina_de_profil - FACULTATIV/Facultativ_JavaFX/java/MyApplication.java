package com.example.facultativ_javafx;

import com.example.facultativ_javafx.controllers.LoginController;
import com.example.facultativ_javafx.controllers.utils.MessageAlert;
import com.example.facultativ_javafx.domain.Utilizator;
import com.example.facultativ_javafx.repository.Repository;
import com.example.facultativ_javafx.repository.UtilizatorRepository;
import com.example.facultativ_javafx.service.Service;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private Service service;

    @Override
    public void start(Stage stage) throws IOException {
        Repository<Utilizator> userRepo = new UtilizatorRepository();
        service = new Service(userRepo);

        initView(stage);
        stage.show();
        stage.toFront();
    }

    private void initView(Stage primaryStage) throws IOException {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/facultativ_javafx/views/login.fxml"));
            Parent root = loader.load();

            // Adaug fisierul CSS
            root.getStylesheets().add(getClass().getResource("/com/example/facultativ_javafx/css/style.css").toExternalForm());

            LoginController controller = loader.getController();
            controller.setStage(primaryStage);
            controller.setService(service);

            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root));
        }
        catch(IOException e){
            MessageAlert.showErrorMessage(primaryStage, e.getMessage());
        }
    }
}
