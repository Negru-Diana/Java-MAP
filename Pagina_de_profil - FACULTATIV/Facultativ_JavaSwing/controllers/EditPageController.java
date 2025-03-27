package controllers;

import controllers.utils.MessageAlert;
import domain.Utilizator;
import service.Service;
import views.EditPageView;
import views.PageView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class EditPageController {

    private EditPageView editPageView;
    private Service service;
    private String username;
    private String selectedProfilePicturePath; //Calea imaginii selectate

    public EditPageController(EditPageView editPageView, Service service, String username) {
        this.editPageView = editPageView;
        this.service = service;
        this.username = username;

        loadUserData(); //Incarc datele utilizatorului
        initController();
    }

    private void initController() {
        editPageView.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                openPage(username);
                editPageView.getFrame().dispose();
            }
        });


        editPageView.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSaveButton();
            }
        });

        editPageView.getChoosePictureButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleChooseProfilePictureButton();
            }
        });
    }

    private void loadUserData() {
        Utilizator user = service.getUser(username);

        if(user == null) {
            MessageAlert.showErrorMessage(editPageView.getFrame(), "A aparut o eroare la incarcarea datelor pentru editare!");
        }

        //Setez datele utilizatorului in view
        editPageView.getUsernameTextField().setText(user.getUsername());
        editPageView.getNameTextField().setText(user.getName());
        editPageView.getOccupationTextField().setText(user.getOcupation() != null ? user.getOcupation() : "");
        editPageView.getDescriptionTextArea().setText(user.getProfileDescription() != null ? user.getProfileDescription() : "");

        //Incarc poza de profil
        if (user.getPicture() == null) {
            editPageView.setProfilePicture(null);  // Imagine de profil implicită
        } else {
            System.out.println(user.getPicture());
            Image img = new ImageIcon(user.getPicture()).getImage();
            ImageIcon icon = new ImageIcon(user.getPicture());
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                editPageView.setProfilePicture(img);
            } else {
                System.out.println("Imaginea nu s-a încărcat corect.");
                editPageView.setProfilePicture(null);
            }
            //pageView.setProfilePicture(img);
        }

        selectedProfilePicturePath = user.getPicture();
    }

    private void handleSaveButton() {
        String name = editPageView.getNameTextField().getText();
        String username2 = editPageView.getUsernameTextField().getText();
        String password = new String(editPageView.getPasswordField().getPassword());
        String description = editPageView.getDescriptionTextArea().getText();
        String ocupation = editPageView.getOccupationTextField().getText();

        // Validare câmpuri obligatorii
        if (username2.isEmpty() || name.isEmpty()) {
            MessageAlert.showMessage(editPageView.getFrame(), "Edit profile", "Campurile username si name nu pot fi goale!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (password.isEmpty()) {
            MessageAlert.showMessage(editPageView.getFrame(), "Edit profile", "Trebuie introdusa parola pentru a salva modificarile!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Validare parolă
        try {
            service.isLoginDataValid(username, password);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(editPageView.getFrame(), e.getMessage());
            return;
        }

        // Validare username unic
        if (!username2.equals(username) && service.getUser(username2) != null) {
            MessageAlert.showErrorMessage(editPageView.getFrame(), "Username-ul este deja utilizat!");
            return;
        }

        // Salvare date
        Utilizator user = new Utilizator();
        user.setName(name);
        user.setUsername(username2);
        user.setHashedPassword(password);
        user.setProfileDescription(description);
        user.setOcupation(ocupation);
        user.setPicture(selectedProfilePicturePath);

        try {
            service.update(user, username);
            MessageAlert.showMessage(editPageView.getFrame(), "Edit profile", "Profilul a fost actualizat cu succes!", JOptionPane.INFORMATION_MESSAGE);

            openPage(username2);
            editPageView.getFrame().dispose();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(editPageView.getFrame(), e.getMessage());
        }
    }

    private void openPage(String username2) {
        PageView pageView = new PageView();
        new PageController(pageView, service, username2);
    }

    private void handleChooseProfilePictureButton(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecteaza o imagine de profil");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg"));

        int result = fileChooser.showOpenDialog(editPageView.getFrame());
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedProfilePicturePath = selectedFile.getAbsolutePath();
            editPageView.setProfilePicture(new ImageIcon(selectedProfilePicturePath).getImage());
        }
    }

}
