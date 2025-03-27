package controllers;

import controllers.utils.MessageAlert;
import service.Service;
import views.LoginView;
import views.PageView;
import views.SignUpView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginController {
    //LoginView
    private LoginView loginView;

    private Service service;

    public LoginController(LoginView loginView, Service service) {
        this.loginView = loginView;
        this.service = service;
        initController();
    }

    private void initController() {
        //Adaug actiuni pe butoanele din LoginView
        loginView.getSignUpButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSignUpView();
            }
        });

        loginView.getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void openSignUpView() {
        try{
            //Deschid fereastra de SignUp
            SignUpView signUpView = new SignUpView();
            SignUpController signUpController = new SignUpController(signUpView, service);
        }
        catch(Exception e){
            MessageAlert.showErrorMessage(loginView.getFrame(), "A aparut o eroare la deschiderea ferestrei SignUp!");
        }
    }

    private void handleLogin(){
        String username = loginView.getUsernameTextField().getText();
        String password = new String(loginView.getPasswordField().getPassword());

        if(username.isEmpty() || password.isEmpty()){
            MessageAlert.showMessage(loginView.getFrame(), "Login failed!", "Toate campurile sunt obligatorii!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try{
            service.isLoginDataValid(username, password);
            //MessageAlert.showMessage(stage, Alert.AlertType.INFORMATION, "Login", "Login successful!");
            openPage();
            clearFields();
        }
        catch(Exception e){
            MessageAlert.showErrorMessage(loginView.getFrame(), e.getMessage());
        }
    }

    private void clearFields(){
        loginView.getUsernameTextField().setText("");
        loginView.getPasswordField().setText("");
    }

    private void openPage(){
        PageView pageView = new PageView();
        new PageController(pageView, service, loginView.getUsernameTextField().getText());

        loginView.getFrame().dispose();
    }

}
