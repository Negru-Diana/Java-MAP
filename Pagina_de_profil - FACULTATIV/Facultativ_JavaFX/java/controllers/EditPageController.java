package com.example.facultativ_javafx.controllers;

import com.example.facultativ_javafx.controllers.utils.MessageAlert;
import com.example.facultativ_javafx.domain.Utilizator;
import com.example.facultativ_javafx.service.Service;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class EditPageController {

    @FXML
    private ImageView profilPictureImageView;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField OcupationTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Label profilePictureLabel;


    private Stage stage;
    private Service service;
    private String username;

    private void load() {
        Utilizator user = service.getUser(username);

        // Verific daca utilizatorul are o imagine de profil
        if (user.getPicture() == null) {
            Image defaultImg = new Image("com/example/facultativ_javafx/images/nopic.png");
            profilPictureImageView.setImage(defaultImg);
        }
        else {
            try{
                // Incarc imaginea de profil
                String imagePath = user.getPicture();
                Image img = new Image(imagePath);
                profilPictureImageView.setImage(img);
            }
            catch (Exception e) {
                Image defaultImg = new Image("com/example/facultativ_javafx/images/nopic.png");
                profilPictureImageView.setImage(defaultImg);
            }
        }

        profilPictureImageView.setFitWidth(167); //Latimea pozei
        profilPictureImageView.setFitHeight(150); //Inaltimea pozei

        profilPictureImageView.setPreserveRatio(false);  // Nu pastreaza raportul de aspect


        usernameTextField.setText(username);
        nameTextField.setText(user.getName());

        if (user.getOcupation() == null) {
            OcupationTextField.setText("");
        } else {
            OcupationTextField.setText(user.getOcupation());
        }

        if (user.getProfileDescription() == null) {
            descriptionTextArea.setText("");
        } else {
            descriptionTextArea.setText(user.getProfileDescription());
        }

        // Salvez calea completa a imaginii in label
        profilePictureLabel.setText(user.getPicture());
    }



    @FXML
    private void handleSaveButton(){
        String name = nameTextField.getText();
        String username2 = usernameTextField.getText();
        String password = passwordField.getText();
        String description = descriptionTextArea.getText();
        String ocupation = OcupationTextField.getText();
        String picture = profilePictureLabel.getText();

        //Verific daca username-ul si numele nu sunt goale
        if (username2.isEmpty() || name.isEmpty()) {
            MessageAlert.showErrorMessage(stage, "Campurile username si name nu pot fi goale!");
            return;
        }

        //Verific daca s-a introdus parola
        if(password.isEmpty()){
            MessageAlert.showErrorMessage(stage, "Trebuie introdusa parola pentru a face modificarile!");
            return;
        }

        //Verific daca parola introdusa este corecta
        try{
            service.isLoginDataValid(username, password);
        }
        catch (Exception e) {
            MessageAlert.showErrorMessage(stage, e.getMessage());
            return;
        }

        //Verific daca s-a modificat username-ul
        if((!username2.equals(username)) && service.getUser(username2) != null){
            MessageAlert.showErrorMessage(stage, "Username-ul este deja utilizat!");
        }

        //Creez noul Utilizator
        Utilizator user = new Utilizator();
        user.setName(name);
        user.setUsername(username2);
        user.setHashedPassword(password);
        user.setProfileDescription(description);
        user.setOcupation(ocupation);
        user.setPicture(picture);

        try{
            //Apelez metoda update din service
            service.update(user, username);

            MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION, "Update profile", "Modificarile au fost salvate cu succes!");

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            //Deschid fereastra Page
            openPage(username2);
        }
        catch (Exception e) {
            MessageAlert.showErrorMessage(stage, e.getMessage());
        }
    }

    private void openPage(String usernameOpen){
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
            controller.setUsername(usernameOpen);

            //Afisez fereastra Page
            pageStage.show();

            //Inchid fereastra Page
            stage.close();
        }
        catch(IOException e){
            MessageAlert.showErrorMessage(stage, "A aparut o eroare la deschiderea paginii utilizatorului!");
        }
    }

    @FXML
    private void handleChooseProfilePictureButton(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            saveProfilePicture(selectedFile);
        }
    }

    private void saveProfilePicture(File selectedFile) {
        String completeFilePath = selectedFile.toURI().toString(); // Calea completa a fișierului

        // Salvez calea completa in label pentru a fi folosita ulterior
        profilePictureLabel.setText(completeFilePath);

        // Incarc imaginea
        Image img = new Image(completeFilePath);
        profilPictureImageView.setImage(img);

        profilPictureImageView.setFitWidth(167); //Latimea pozei
        profilPictureImageView.setFitHeight(150); //Inaltimea pozei
        profilPictureImageView.setPreserveRatio(false);  // Nu pastreaza raportul de aspect
    }


    private void closeRequest(){
        //Gestionarea evenimentului de inchidere a ferestrei EditPage
        stage.setOnCloseRequest(event -> {
            openPage(username);
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;

        closeRequest();
    }

    public void setService(Service service) {
        this.service = service;
    }

    public void setUsername(String username) {
        this.username = username;
        load();
    }
}
