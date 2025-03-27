package com.example.facultativ_javafx.controllers;

import com.example.facultativ_javafx.controllers.utils.MessageAlert;
import com.example.facultativ_javafx.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    private Stage stage;
    private Service service;

    @FXML
    private void handleSignUpButton(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/facultativ_javafx/views/signUp.fxml"));
            Parent root = loader.load();

            // Adaug fisierul CSS
            root.getStylesheets().add(getClass().getResource("/com/example/facultativ_javafx/css/style.css").toExternalForm());

            SignUpController controller = loader.getController();
            controller.setService(service);
            Stage signUpStage = new Stage();
            signUpStage.setTitle("SignUp");
            signUpStage.setScene(new Scene(root));
            controller.setStage(signUpStage);

            //Afisez fereastra SignUp
            signUpStage.show();

            //Golesc Field-urile din fereastra Login
            clearFields();
        }
        catch(IOException e){
            MessageAlert.showErrorMessage(stage, "A aparut o eroare la deschiderea ferestrei de SignUp!");
        }
    }


    @FXML
    private void handleLoginButton(){
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        if(username.isEmpty() || password.isEmpty()){
            MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION, "Login failed", "Toate campurile sunt obligatorii!");
            return;
        }

        try{
            service.isLoginDataValid(username, password);
            //MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION, "Login", "Login successful!");
            openPage();
            clearFields();
        }
        catch(Exception e){
            MessageAlert.showErrorMessage(stage, e.getMessage());
        }
    }


    private void openPage(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/facultativ_javafx/views/page.fxml"));
            Parent root = loader.load();

            // Adaug fisierul CSS
            root.getStylesheets().add(getClass().getResource("/com/example/facultativ_javafx/css/style.css").toExternalForm());

            PageController controller = loader.getController();
            controller.setService(service);
            Stage pageStage = new Stage();
            pageStage.setTitle("Page");
            pageStage.setScene(new Scene(root));
            controller.setStage(pageStage);
            controller.setUsername(usernameTextField.getText());

            //Afisez fereastra Page
            pageStage.show();

            //Inchid fereastra de Login
            stage.close();
        }
        catch(IOException e){
            MessageAlert.showErrorMessage(stage, "A aparut o eroare la deschiderea profilului!");
        }
    }



    private void clearFields(){
        usernameTextField.clear();
        passwordField.clear();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
