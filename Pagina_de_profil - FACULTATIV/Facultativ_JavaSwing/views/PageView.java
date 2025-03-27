package views;

import controllers.LoginController;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class PageView {

    private JFrame frame;
    private JLabel usernameLabel;
    private JLabel postsLabel;
    private JLabel followersLabel;
    private JLabel followingLabel;
    private JLabel nameLabel;
    private JLabel ocupationLabel;
    private JLabel descriptionLabel;
    private JButton editProfileButton;
    private JLabel profilePictureLabel;

    //Constructor
    public PageView(){
        initView();
    }

    public void initView() {
        // Creez fereastra principala
        frame = new JFrame("Profile Page");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(480, 400); // Dimensiune fereastra
        frame.setLayout(null); // Folosesc un layout absolut
        frame.getContentPane().setBackground(new Color(239, 182, 200));

        // Label pentru Username
        usernameLabel = new JLabel("Label");
        usernameLabel.setFont(new Font("System Bold Italic", Font.BOLD | Font.ITALIC, 20));
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER); // Centrel textul la mijlocul label-ului
        usernameLabel.setPreferredSize(new Dimension(480, 30)); // Lățimea etichetei va fi 480px (lățimea ferestrei)

        // Centrarea etichetei în fereastră
        SwingUtilities.invokeLater(() -> {
            Dimension frameSize = frame.getSize(); // Obține dimensiunile ferestrei după ce fereastra este procesată
            int x = (frameSize.width - usernameLabel.getPreferredSize().width) / 2; // Centrarea pe axa X
            int y = 23; // Poziție pe axa Y
            usernameLabel.setBounds(x, y, usernameLabel.getPreferredSize().width, usernameLabel.getPreferredSize().height);
            frame.add(usernameLabel);
        });


        // Label pentru Posts
        postsLabel = new JLabel("0000");
        postsLabel.setFont(new Font("System Italic", Font.ITALIC, 15));
        postsLabel.setBounds(43, 96, 100, 20);
        postsLabel.setForeground(Color.BLACK);
        frame.add(postsLabel);

        // Label pentru Followers
        followersLabel = new JLabel("00000");
        followersLabel.setFont(new Font("System Italic", Font.ITALIC, 15));
        followersLabel.setBounds(43, 129, 100, 20);
        followersLabel.setForeground(Color.BLACK);
        frame.add(followersLabel);

        // Label pentru Following
        followingLabel = new JLabel("00000");
        followingLabel.setFont(new Font("System Italic", Font.ITALIC, 15));
        followingLabel.setBounds(43, 161, 100, 20);
        followingLabel.setForeground(Color.BLACK);
        frame.add(followingLabel);

        // Label pentru "Posts"
        JLabel postsTextLabel = new JLabel("Posts");
        postsTextLabel.setFont(new Font("System Bold Italic", Font.BOLD | Font.ITALIC, 15));
        postsTextLabel.setBounds(110, 98, 80, 20);
        postsTextLabel.setForeground(Color.BLACK);
        frame.add(postsTextLabel);

        // Label pentru "Followers"
        JLabel followersTextLabel = new JLabel("Followers");
        followersTextLabel.setFont(new Font("System Bold Italic", Font.BOLD | Font.ITALIC, 15));
        followersTextLabel.setBounds(110, 129, 80, 20);
        followersTextLabel.setForeground(Color.BLACK);
        frame.add(followersTextLabel);

        // Label pentru "Following"
        JLabel followingTextLabel = new JLabel("Following");
        followingTextLabel.setFont(new Font("System Bold Italic", Font.BOLD | Font.ITALIC, 15));
        followingTextLabel.setBounds(110, 161, 80, 20);
        followingTextLabel.setForeground(Color.BLACK);
        frame.add(followingTextLabel);

        // Imaginea de profil
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
        profilePictureLabel.setBounds(277, 70, 152, 140);
        profilePictureLabel.setOpaque(false);
        frame.add(profilePictureLabel);

        // Label pentru Name
        nameLabel = new JLabel("Label");
        nameLabel.setFont(new Font("System", Font.BOLD, 15));
        nameLabel.setBounds(277, 225, 152, 17);
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(nameLabel);

        // Label pentru Occupation
        ocupationLabel = new JLabel("Label");
        ocupationLabel.setFont(new Font("System", Font.BOLD | Font.ITALIC, 15));
        ocupationLabel.setBounds(277, 254, 152, 17);
        ocupationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ocupationLabel.setForeground(new Color(81, 78, 78));
        frame.add(ocupationLabel);

        // Label pentru Description
        descriptionLabel = new JLabel("<html>Label</html>");
        descriptionLabel.setFont(new Font("System", Font.PLAIN, 15));
        descriptionLabel.setBounds(42, 225, 199, 113);
        descriptionLabel.setVerticalAlignment(SwingConstants.TOP);
        descriptionLabel.setHorizontalAlignment(SwingConstants.LEFT);
        descriptionLabel.setForeground(Color.BLACK);
        frame.add(descriptionLabel);

        // Buton pentru Edit Profile
        editProfileButton = new JButton("Edit profile");
        editProfileButton.setFont(new Font("System Bold", Font.BOLD, 15));
        editProfileButton.setBounds(277, 300, 152, 31);
        editProfileButton.setBackground(new Color(244, 108, 151));
        editProfileButton.setForeground(Color.BLACK);
        editProfileButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        frame.add(editProfileButton);

        // Afișez fereastra
        frame.setLocationRelativeTo(null); // Centrarea ferestrei pe ecran
        frame.setVisible(true);
    }


    //Getteri pentru componente
    public JFrame getFrame() {
        return frame;
    }

    public JLabel getUsernameLabel() {
        return usernameLabel;
    }

    public JLabel getPostsLabel() {
        return postsLabel;
    }

    public JLabel getFollowersLabel() {
        return followersLabel;
    }

    public JLabel getFollowingLabel() {
        return followingLabel;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getOcupationLabel() {
        return ocupationLabel;
    }

    public JLabel getDescriptionLabel() {
        return descriptionLabel;
    }

    public JButton getEditProfileButton() {
        return editProfileButton;
    }

    public JLabel getProfilePictureLabel() {
        return profilePictureLabel;
    }


    //Seteri pentru componente


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





    public void setUsernameLabel(JLabel usernameLabel) {
        this.usernameLabel = usernameLabel;
    }

    public void setPostsLabel(JLabel postsLabel) {
        this.postsLabel = postsLabel;
    }

    public void setFollowersLabel(JLabel followersLabel) {
        this.followersLabel = followersLabel;
    }

    public void setFollowingLabel(JLabel followingLabel) {
        this.followingLabel = followingLabel;
    }

    public void setNameLabel(JLabel nameLabel) {
        this.nameLabel = nameLabel;
    }

    public void setOcupationLabel(JLabel ocupationLabel) {
        this.ocupationLabel = ocupationLabel;
    }

    public void setDescriptionLabel(JLabel descriptionLabel) {
        this.descriptionLabel = descriptionLabel;
    }
}
