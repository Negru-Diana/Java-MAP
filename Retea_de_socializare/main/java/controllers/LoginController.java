package com.example.retea_de_socializare.controllers;

import com.example.retea_de_socializare.service.ObservableService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField; //TextField pentru username

    @FXML
    private PasswordField passwordField; //PasswordField pentru parola

    private ObservableService service;
    private Stage stage;



    @FXML
    private void handleLoginButton(){
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username.isEmpty() || password.isEmpty()){
            MessageAlert.showErrorMessage(stage,"Toate campurile sunt obligatorii!");
            return;
        }
        else if(isValid(username, password) == true){
            service.addActivUser(username);
            openReteaDeSocializareController(username);
        }
        else{
            MessageAlert.showErrorMessage(stage, "Datele de logare sunt invalide!");
        }

    }

    private void openReteaDeSocializareController(String username){
        try{
            // Incarc fisierul FXML pentru SignUpController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/retea_de_socializare/views/reteaDeSocializare.fxml"));
            Parent root = loader.load();

            // Adaug fisierul CSS
            root.getStylesheets().add(getClass().getResource("/com/example/retea_de_socializare/css/viewStyle.css").toExternalForm());

            // Obtin instanta controller-ului
            ReteaDeSocializareController controller = loader.getController();
            Stage reteaDeSocializareStage = new Stage();
            reteaDeSocializareStage.setTitle("Retea de socializare");
            reteaDeSocializareStage.setScene(new Scene(root));
            controller.setStage(reteaDeSocializareStage);
            controller.setUsername(username);
            controller.setService(service);

            // Afisez fereastra Retea de socializare
            reteaDeSocializareStage.show();

            // Ascund fereastra curenta (Login)
            //stage.hide();

            clearFields();
        }
        catch(IOException e){
            e.printStackTrace();
            MessageAlert.showErrorMessage(stage,"A aparut o eroare la deschiderea ferestrei Retea de socializare!");
        }
    }


    private boolean isValid(String username, String password){
        try{
            return service.autentificare(username, password);
        }
        catch(Exception e){
            MessageAlert.showErrorMessage(stage, e.getMessage());
        }

        return false;
    }



    @FXML
    private void handleSignUpButton(){
        try{
            // Incarc fisierul FXML pentru SignUpController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/retea_de_socializare/views/signUp.fxml"));
            Parent root = loader.load();

            // Adaug fisierul CSS
            root.getStylesheets().add(getClass().getResource("/com/example/retea_de_socializare/css/viewStyle.css").toExternalForm());

            // Obtin instanta controller-ului
            SignUpController controller = loader.getController();
            controller.setService(service);
            Stage signUpStage = new Stage();
            signUpStage.setTitle("Sign Up");
            signUpStage.setScene(new Scene(root));
            controller.setStage(signUpStage);

            // Afisez fereastra Sign Up
            signUpStage.show();

            // Ascund fereastra curenta (Login)
            //stage.hide();

            // Golesc Field-urile din fereastra Login
            clearFields();

            // Afisez din nou fereastra de Login dupa inchiderea ferestrei Sign Up
            //signUpStage.setOnHidden(event -> stage.show());
        }
        catch(IOException e){
            e.printStackTrace();
            MessageAlert.showErrorMessage(stage,"A aparut o eroare la deschiderea ferestrei de sign up!");
        }
    }

    private void clearFields(){
        passwordField.clear();
        usernameField.clear();
    }


    public void setService(ObservableService service) {
        this.service = service;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


}
