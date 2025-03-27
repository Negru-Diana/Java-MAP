package com.example.facultativ_javafx.controllers;

import com.example.facultativ_javafx.controllers.utils.MessageAlert;
import com.example.facultativ_javafx.service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;


    private Stage stage;
    private Service service;


    @FXML
    private void handleCreateButton(){
        String name = nameTextField.getText();
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if(name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION, "Date incomplete","Toate campurile sunt obligatorii!");
            return;
        }

        if(!password.equals(confirmPassword)){
            MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION, "Date invalide", "Parolele nu coincid!");
            return;
        }

        try{
            service.add(name, username, password);
            MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION, "Create account", "Contul a fost creat cu succes!");
            cleardFields();

            stage.close();
        }
        catch(Exception e){
            MessageAlert.showErrorMessage(stage, e.getMessage());
        }
    }

    private void cleardFields(){
        nameTextField.clear();
        usernameTextField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
