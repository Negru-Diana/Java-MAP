package com.example.examen_db.UI;

import com.example.examen_db.controllers.MainController;
import com.example.examen_db.service.ObservableService;
import com.example.examen_db.service.Service;
import com.example.examen_db.service.ServiceFactory;
import com.example.examen_db.utils.MessageAlert;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ExamenApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        ObservableService service = ServiceFactory.getService();

        initView(stage, service);
        stage.show();
        stage.toFront();
    }

    private void initView(Stage primaryStage, ObservableService service){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/examen_db/views/mainView.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.setStage(primaryStage);
            controller.setService(service);

            primaryStage.setTitle("Main");
            primaryStage.setScene(new Scene(root));
        }
        catch (IOException e) {
            MessageAlert.showErrorMessage(primaryStage, e.getMessage());
        }
    }
}
