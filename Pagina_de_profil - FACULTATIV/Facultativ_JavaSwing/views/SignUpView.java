package views;

import javax.swing.*;
import java.awt.*;

public class SignUpView {
    private JFrame frame;
    private JTextField usernameTextField;
    private JTextField nameTextField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton createButton;

    //Constructor
    public SignUpView() {
        initView();
    }

    public void initView() {
        // Creez fereastra principală
        frame = new JFrame("Sign Up");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(475, 380); // Dimensiunea ferestrei
        frame.setLayout(null); // Folosesc layout absolut
        frame.getContentPane().setBackground(new Color(239, 182, 200));

        // Label pentru "Username"
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("System Bold Italic", Font.BOLD | Font.ITALIC, 15));
        usernameLabel.setBounds(51, 50, 100, 20);
        usernameLabel.setForeground(Color.BLACK);
        frame.add(usernameLabel);

        // Label pentru "Name"
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("System Bold Italic", Font.BOLD | Font.ITALIC, 15));
        nameLabel.setBounds(51, 100, 100, 20);
        nameLabel.setForeground(Color.BLACK);
        frame.add(nameLabel);

        // Label pentru "Password"
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("System Bold Italic", Font.BOLD | Font.ITALIC, 15));
        passwordLabel.setBounds(51, 150, 100, 20);
        passwordLabel.setForeground(Color.BLACK);
        frame.add(passwordLabel);

        // Label pentru "Confirm password"
        JLabel confirmPasswordLabel = new JLabel("Confirm password:");
        confirmPasswordLabel.setFont(new Font("System Bold Italic", Font.BOLD | Font.ITALIC, 15));
        confirmPasswordLabel.setBounds(51, 200, 150, 20);
        confirmPasswordLabel.setForeground(Color.BLACK);
        frame.add(confirmPasswordLabel);

        // TextField pentru "Username"
        usernameTextField = new JTextField();
        usernameTextField.setBounds(51, 75, 350, 25);
        frame.add(usernameTextField);

        // TextField pentru "Name"
        nameTextField = new JTextField();
        nameTextField.setBounds(51, 125, 350, 25);
        frame.add(nameTextField);

        // PasswordField pentru "Password"
        passwordField = new JPasswordField();
        passwordField.setBounds(51, 175, 350, 25);
        frame.add(passwordField);

        // PasswordField pentru "Confirm Password"
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(51, 225, 350, 25);
        frame.add(confirmPasswordField);

        // Button pentru "Create"
        createButton = new JButton("Create");
        createButton.setFont(new Font("System Bold", Font.BOLD, 15));
        createButton.setBounds(277, 270, 124, 31);
        createButton.setBackground(new Color(244, 108, 151));
        createButton.setForeground(Color.BLACK);
        createButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(createButton);

        // Afișez fereastra
        frame.setLocationRelativeTo(null); // Centrez fereastra pe ecran
        frame.setVisible(true);
    }


    //Getteri pentru componente
    public JFrame getFrame() {
        return frame;
    }

    public JTextField getUsernameTextField() {
        return usernameTextField;
    }

    public JTextField getNameTextField() {
        return nameTextField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JPasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }

    public JButton getCreateButton() {
        return createButton;
    }
}
