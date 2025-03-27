import controllers.LoginController;
import domain.Utilizator;
import repository.Repository;
import repository.UtilizatorRepository;
import service.Service;
import views.EditPageView;
import views.LoginView;
import views.PageView;
import views.SignUpView;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        Repository<Utilizator> userRepo = new UtilizatorRepository();
        Service service = new Service(userRepo);

        LoginView loginView = new LoginView();
        new LoginController(loginView, service);
    }
}