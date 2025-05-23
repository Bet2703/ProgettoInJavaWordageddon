    package controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.Login;
import users.Player;
import users.Role;

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

        try {
            FXMLLoader loader;
            Parent root;

            switch (player.getRole()) {
                case BASE:
                    loader = new FXMLLoader(getClass().getResource("/view/UserManagementView.fxml"));
                    root = loader.load();
                    break;
                case ADMIN:
                    loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
                    root = loader.load();
                    break;
                default:
                    messageLabel.setText("Ruolo utente non riconosciuto.");
                    return;
            }

            // Mostra la nuova scena
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Errore durante il caricamento della schermata.");
        }

    } else {
        messageLabel.setText("Username o password errati.");
    }
}

    @FXML
    private void onRegister(ActionEvent event) {

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Campi username e password obbligatori.");
            return;
        }

        if(Login.isUsernameTaken(username)) {
            messageLabel.setText("Username già esistente. Scegline un altro.");
            return;
        }

        else {
            Login.userRegister(username, password, Role.BASE);
            messageLabel.setText("Utente registrato con successo!");
        }
    }
}
