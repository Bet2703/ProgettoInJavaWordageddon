    package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import service.Login;
import users.Player;

public class LoginController {

    @FXML
    private VBox card;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label messageLabel;

    @FXML
    private void onLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Inserisci username e password.");
            return;
        }

        Player player = Login.login(username, password);

        if (player != null) {
            messageLabel.setText("Accesso riuscito! Benvenuto " + player.getUsername());
            // TODO: Carica la schermata successiva (ad es. MainMenu)
        } else {
            messageLabel.setText("Username o password errati.");
        }
    }

    @FXML
    private void onRegister(ActionEvent event) {
        // TODO: Implementa schermata di registrazione se prevista
        messageLabel.setText("Funzionalità di registrazione non ancora disponibile.");
    }
}
