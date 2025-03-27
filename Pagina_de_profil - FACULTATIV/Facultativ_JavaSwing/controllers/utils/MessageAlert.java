package controllers.utils;

import javax.swing.*;
import java.awt.*;

public class MessageAlert {
    //Afiseaza un mesaj de eroare
    public static void showErrorMessage(Component owner, String text) {
        JOptionPane.showMessageDialog(owner, text, "Error", JOptionPane.ERROR_MESSAGE);
    }

    //Afiseaza un mesaj generic
    public static void showMessage(Component owner, String title, String text, int messageType) {
        JOptionPane.showMessageDialog(owner, text, title, messageType);
    }
}
