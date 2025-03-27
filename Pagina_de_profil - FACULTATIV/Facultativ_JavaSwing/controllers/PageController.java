package controllers;

import controllers.utils.MessageAlert;
import domain.Utilizator;
import service.Service;
import views.EditPageView;
import views.LoginView;
import views.PageView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PageController {

    private PageView pageView;
    private Service service;
    private String username;

    public PageController(PageView pageView, Service service, String username) {
        this.pageView = pageView;
        this.service = service;
        this.username = username;

        initController();
    }

    private void initController(){
        //Configurez datele pentru pagina de profil
        loadPageData();

        pageView.getFrame().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                openLogin();
                pageView.getFrame().dispose();
            }
        });

        pageView.getEditProfileButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openEditProfileView();
            }
        });
    }

    private void openEditProfileView(){
        EditPageView editPageView = new EditPageView();
        new EditPageController(editPageView, service, username);

        pageView.getFrame().dispose();
    }

    private void loadPageData() {
        Utilizator user = service.getUser(username);
        System.out.println(user);

        // Setează imaginea de profil
        if (user.getPicture() == null) {
            pageView.setProfilePicture(null);  // Imagine de profil implicită
        } else {
            System.out.println(user.getPicture());
            Image img = new ImageIcon(user.getPicture()).getImage();
            ImageIcon icon = new ImageIcon(user.getPicture());
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                pageView.setProfilePicture(img);
            } else {
                System.out.println("Imaginea nu s-a încărcat corect.");
                pageView.setProfilePicture(null);
            }
            //pageView.setProfilePicture(img);
        }

        // Setează numele utilizatorului și datele suplimentare
        pageView.getUsernameLabel().setText(user.getUsername());
        pageView.getNameLabel().setText(user.getName());
        pageView.getPostsLabel().setText(String.valueOf(user.getNumberOfPosts()));
        pageView.getFollowersLabel().setText(String.valueOf(user.getNumberOfFollowers()));
        pageView.getFollowingLabel().setText(String.valueOf(user.getNumberOfFollowing()));

        // Setează ocupația și descrierea profilului
        if (user.getOcupation() != null) {
            pageView.getOcupationLabel().setText(user.getOcupation());
        } else {
            pageView.getOcupationLabel().setText("");
        }

        if (user.getProfileDescription() != null) {
            pageView.getDescriptionLabel().setText(user.getProfileDescription());
        } else {
            pageView.getDescriptionLabel().setText("");
        }
    }


    private void openLogin(){
        LoginView loginView = new LoginView();
        new LoginController(loginView, service);
    }
}
