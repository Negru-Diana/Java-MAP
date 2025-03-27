package com.example.retea_de_socializare.controllers;

import com.example.retea_de_socializare.service.ObservableService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private TextField prenumeField;

    @FXML
    private TextField numeField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;


    private ObservableService service;
    private Stage stage;



    @FXML
    private void handleCreateButton(){
        String nume = numeField.getText();
        String prenume = prenumeField.getText();
        String username = usernameField.getText();
        String parola = passwordField.getText();
        String confirmare = confirmPasswordField.getText();

        if(nume.isEmpty() || prenume.isEmpty() || username.isEmpty() || parola.isEmpty() || confirmare.isEmpty()){
            MessageAlert.showErrorMessage(stage, "Toate campurile sunt obligatorii!");
        }
        else if(!parola.equals(confirmare)){
            MessageAlert.showErrorMessage(stage, "Parola si confirmarea parolei nu coincid!");
        }
        else if(createAccount(nume, prenume, username, parola)){
            MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION, "Create Account", "Contul a fost creat cu succes!");
            stage.close();
        }
    }

    private boolean createAccount(String nume, String prenume, String username, String parola){
        try{
            service.addUtilizator(prenume, nume, username, parola);
            return true;
        } catch (Exception e) {
            MessageAlert.showErrorMessage(stage, e.getMessage());
        }
        return false;
    }






    public void setService(ObservableService service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
