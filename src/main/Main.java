package main;

import core.ApplicationContext;
import core.views.LoginView;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        System.setProperty("flatlaf.useNativeLibrary", "false");

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        ApplicationContext.getInstance();
        LoginView mainView = new LoginView();
        mainView.setVisible(true);
    }
}
