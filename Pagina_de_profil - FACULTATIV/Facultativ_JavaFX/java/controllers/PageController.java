package com.example.facultativ_javafx.controllers;

import com.example.facultativ_javafx.controllers.utils.MessageAlert;
import com.example.facultativ_javafx.domain.Utilizator;
import com.example.facultativ_javafx.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class PageController {

    @FXML
    private ImageView profilePictureImageView;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label postsNumberLabel;

    @FXML
    private Label followersNumberLabel;

    @FXML
    private Label followingNumberLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label ocupationLabel;

    @FXML
    private Label profileDescriptionLabel;

    private Stage stage;
    private Service service;
    private String username;

    public void load(){
        Utilizator user = service.getUser(username);
        System.out.println(user);

        //Setez imaginea de profil
        if(user.getPicture() == null){
            Image defaultImg = new Image("com/example/facultativ_javafx/images/nopic.png");
            profilePictureImageView.setImage(defaultImg);
        }
        else{
            try{
                Image img = new Image(user.getPicture());
                profilePictureImageView.setImage(img);
            } catch (Exception e) {
                Image defaultImg = new Image("com/example/facultativ_javafx/images/nopic.png");
                profilePictureImageView.setImage(defaultImg);
            }
        }

        profilePictureImageView.setFitWidth(167); //Latimea pozei
        profilePictureImageView.setFitHeight(150); //Inaltimea pozei
        profilePictureImageView.setPreserveRatio(false);  // Nu pastreaza raportul de aspect

        //Setez username-ul
        usernameLabel.setText(username);

        //Setez numele
        nameLabel.setText(user.getName());

        //Setez numarul de postari, followers si following
        postsNumberLabel.setText(String.valueOf(user.getNumberOfPosts()));
        followersNumberLabel.setText(String.valueOf(user.getNumberOfFollowers()));
        followingNumberLabel.setText(String.valueOf(user.getNumberOfFollowing()));

        //Setez ocupatia
        if(user.getOcupation() == null){
            ocupationLabel.setText("");
        }
        else{
            ocupationLabel.setText(user.getOcupation());
        }

        //Setez descrierea porfilului
        if(user.getProfileDescription() == null){
            profileDescriptionLabel.setText("");
        }
        else{
            profileDescriptionLabel.setText(user.getProfileDescription());
        }
    }


    @FXML
    private void handleEditProfileButton(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/facultativ_javafx/views/editPage.fxml"));
            Parent root = loader.load();

            // Adaug fisierul CSS
            root.getStylesheets().add(getClass().getResource("/com/example/facultativ_javafx/css/style.css").toExternalForm());

            EditPageController controller = loader.getController();
            controller.setService(service);
            Stage editPageStage = new Stage();
            editPageStage.setTitle("Edit profile");
            editPageStage.setScene(new Scene(root));
            controller.setStage(editPageStage);
            controller.setUsername(username);

            // Setez dimensiunea ferestrei EditPage
            editPageStage.setWidth(600); // Latimea
            editPageStage.setHeight(380); // Inaltimea

            //Afisez fereastra EditPage
            editPageStage.show();

            //Inchid fereastra Page
            stage.close();
        }
        catch(IOException e){
            MessageAlert.showErrorMessage(stage, "A aparut o eroare la deschiderea paginii de editare!");
        }
    }

    private void openLogin(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/facultativ_javafx/views/login.fxml"));
            Parent root = loader.load();

            // Adaug fisierul CSS
            root.getStylesheets().add(getClass().getResource("/com/example/facultativ_javafx/css/style.css").toExternalForm());

            LoginController controller = loader.getController();
            controller.setService(service);
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root));
            controller.setStage(loginStage);

            //Afisez fereastra EditPage
            loginStage.show();

            //Inchid fereastra Page
            stage.close();
        }
        catch(IOException e){
            MessageAlert.showErrorMessage(stage, "A aparut o eroare la deschiderea paginii login!");
            stage.close();
        }
    }

    private void closeRequest(){
        //Gestionarea evenimentului de inchidere a ferestrei EditPage
        stage.setOnCloseRequest(event -> {
            openLogin();
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
