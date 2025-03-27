package controllers;

import controllers.utils.MessageAlert;
import service.Service;
import views.LoginView;
import views.SignUpView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpController {
    //SignUpView
    private SignUpView signUpView;

    private Service service;

    public SignUpController(SignUpView signUpView, Service service) {
        this.signUpView = signUpView;
        this.service = service;
        initController();
    }

    private void initController() {
        //Adaug actiune pentru butonul Create
        signUpView.getCreateButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCreateButton();
            }
        });
    }

    private void handleCreateButton() {
        // Obținem valorile din câmpuri
        String name = signUpView.getNameTextField().getText();
        String username = signUpView.getUsernameTextField().getText();
        String password = new String(signUpView.getPasswordField().getPassword());
        String confirmPassword = new String(signUpView.getConfirmPasswordField().getPassword());

        // Verificăm dacă toate câmpurile sunt completate
        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            MessageAlert.showMessage(signUpView.getFrame(), "Data incomplete", "Toate câmpurile sunt obligatorii!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Verificăm dacă parolele coincid
        if (!password.equals(confirmPassword)) {
            MessageAlert.showMessage(signUpView.getFrame(), "Data invalide", "Parolele nu coincid!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Încercăm să adăugăm contul
        try {
            service.add(name, username, password);
            MessageAlert.showMessage(signUpView.getFrame(), "Create account", "Contul a fost creat cu succes!", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            signUpView.getFrame().dispose(); // Închidem fereastra
        } catch (Exception e) {
            MessageAlert.showErrorMessage(signUpView.getFrame(), e.getMessage());
        }
    }

    private void clearFields(){
        signUpView.getNameTextField().setText("");
        signUpView.getUsernameTextField().setText("");
        signUpView.getPasswordField().setText("");
        signUpView.getConfirmPasswordField().setText("");
    }

}
