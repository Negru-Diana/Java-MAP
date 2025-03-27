package views;

import javax.swing.*;
import java.awt.*;

public class LoginView {

    private JFrame frame;
    private JTextField usernameTextField;
    private JPasswordField passwordField;
    private JButton signUpButton;
    private JButton loginButton;

    public LoginView() {
        initView();
    }

    public void initView() {
        //Creez fereastra principala
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(475, 330); // Dimensiunea ferestrei
        frame.setLayout(null); // Layout absolut
        frame.getContentPane().setBackground(new Color(239, 182, 200));

        //Label pentru username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("System Bold Italic", Font.BOLD | Font.ITALIC, 15));
        usernameLabel.setBounds(50, 44, 100, 20);
        usernameLabel.setForeground(Color.BLACK);
        frame.add(usernameLabel);

        //Label pentru password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("System Bold Italic", Font.BOLD | Font.ITALIC, 15));
        passwordLabel.setBounds(50, 122, 100, 20);
        passwordLabel.setForeground(Color.BLACK);
        frame.add(passwordLabel);

        //TextField pentru username
        usernameTextField = new JTextField();
        usernameTextField.setBounds(51, 74, 367, 25);
        frame.add(usernameTextField);

        //PasswordField pentru password
        passwordField = new JPasswordField();
        passwordField.setBounds(51, 154, 367, 25);
        frame.add(passwordField);

        //Button pentru SignUp
        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("System Bold", Font.BOLD, 15));
        signUpButton.setBounds(51, 222, 160, 31);
        signUpButton.setBackground(new Color(244, 108, 151));
        signUpButton.setForeground(Color.BLACK);
        signUpButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(signUpButton);

        //Button pentru Login
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("System Bold", Font.BOLD, 15));
        loginButton.setBounds(258, 222, 160, 31);
        loginButton.setForeground(Color.BLACK);
        loginButton.setBackground(new Color(244, 108, 151));
        loginButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(loginButton);

        //Centrez fereastra pe ecran
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    //Getteri pentru componente
    public JFrame getFrame() {
        return frame;
    }

    public JTextField getUsernameTextField() {
        return usernameTextField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getSignUpButton() {
        return signUpButton;
    }

    public JButton getLoginButton() {
        return loginButton;
    }
}
