package views;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class EditPageView {

    private JFrame frame;
    private JLabel profilePictureLabel;
    private JButton choosePictureButton;
    private JTextField usernameTextField;
    private JTextField nameTextField;
    private JTextField occupationTextField;
    private JPasswordField passwordField;
    private JTextArea descriptionTextArea;
    private JButton saveButton;

    public EditPageView() {
        initView();
    }

    public void initView() {
        // Creez fereastra principala
        frame = new JFrame("Edit Profile Page");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(620, 385);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(239, 182, 200));

        // Imaginea de profil (cerc gol initial)
        profilePictureLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();

                // Activez antialiasing pentru margini mai netede
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dimensiunea si decuparea circulara
                int diameter = Math.min(getWidth(), getHeight());
                int x = (getWidth() - diameter) / 2;
                int y = (getHeight() - diameter) / 2;

                Ellipse2D.Double circle = new Ellipse2D.Double(x, y, diameter, diameter);
                g2d.setClip(circle);

                // Desenez imaginea
                if (getIcon() != null) {
                    Image img = ((ImageIcon) getIcon()).getImage();
                    g2d.drawImage(img, x, y, diameter, diameter, this);
                } else {
                    // Daca nu exista imagine, desenez un cerc gri
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.fill(circle);
                }

                //Desenez conturul negru exact pe marginea cercului
                g2d.setClip(null); // Elimin clip-ul pentru a desena peste intreaga zona
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(3)); // Grosimea conturului
                g2d.draw(new Ellipse2D.Double(x + 1.5, y + 1.5, diameter - 3, diameter - 3));

                g2d.dispose();
            }
        };
        profilePictureLabel.setBounds(37, 29, 150, 150);
        profilePictureLabel.setOpaque(false);
        frame.add(profilePictureLabel);

        // Buton pentru alegerea unei imagini
        choosePictureButton = new JButton("Choose profile picture");
        choosePictureButton.setBounds(37, 188, 164, 25);
        frame.add(choosePictureButton);

        // Label-uri și campuri de text
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(223, 41, 80, 25);
        usernameLabel.setForeground(Color.BLACK);
        frame.add(usernameLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(309, 41, 258, 25);
        frame.add(usernameTextField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(223, 73, 80, 25);
        nameLabel.setForeground(Color.BLACK);
        frame.add(nameLabel);

        nameTextField = new JTextField();
        nameTextField.setBounds(309, 73, 258, 25);
        frame.add(nameTextField);

        JLabel occupationLabel = new JLabel("Occupation:");
        occupationLabel.setBounds(223, 106, 80, 25);
        occupationLabel.setForeground(Color.BLACK);
        frame.add(occupationLabel);

        occupationTextField = new JTextField();
        occupationTextField.setBounds(309, 106, 258, 25);
        frame.add(occupationTextField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(223, 141, 80, 25);
        passwordLabel.setForeground(Color.BLACK);
        frame.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(309, 141, 258, 25);
        frame.add(passwordField);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(223, 179, 80, 25);
        descriptionLabel.setForeground(Color.BLACK);
        frame.add(descriptionLabel);

        descriptionTextArea = new JTextArea();
        descriptionTextArea.setBounds(309, 179, 258, 70);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionTextArea.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
        frame.add(descriptionTextArea);

        // Buton pentru salvare
        saveButton = new JButton("Save");
        saveButton.setBounds(488, 286, 79, 31);
        saveButton.setBackground(new Color(244, 108, 151));
        saveButton.setForeground(Color.BLACK);
        saveButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(saveButton);

        // Setez vizibilitatea ferestrei
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Metoda pentru setarea unei imagini rotunde
    public void setProfilePicture(Image image) {
        if (image != null) {
            Image scaledImage = image.getScaledInstance(profilePictureLabel.getWidth(), profilePictureLabel.getHeight(), Image.SCALE_SMOOTH);
            profilePictureLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            profilePictureLabel.setIcon(null);
        }
        profilePictureLabel.repaint();
    }


    //Getteri pentru componente
    public JFrame getFrame() {
        return frame;
    }

    public JLabel getProfilePictureLabel() {
        return profilePictureLabel;
    }

    public JButton getChoosePictureButton() {
        return choosePictureButton;
    }

    public JTextField getUsernameTextField() {
        return usernameTextField;
    }

    public JTextField getNameTextField() {
        return nameTextField;
    }

    public JTextField getOccupationTextField() {
        return occupationTextField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JTextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public JButton getSaveButton() {
        return saveButton;
    }
}
