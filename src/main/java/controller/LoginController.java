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

    /**
     * Invoked when the user clicks the login button.
     * Checks if the username and password are valid and logs the user in.
     * If successful, loads the corresponding view.
     * @param event the event that triggered the method call
     */
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
            service.GameSessionManagement.getInstance().setCurrentPlayer(player);
            try {
                loadViewForRole(player.getRole());
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
                messageLabel.setText("Errore durante il caricamento della schermata.");
            }
        } else {
            messageLabel.setText("Username o password errati.");
        }
    }

    /**
     * Loads the corresponding view for the given role.
     *
     * @param role the role of the user to be loaded
     *
     * @throws IOException if an error occurs during the loading of the view
     */
    private void loadViewForRole(Role role) throws IOException {
            FXMLLoader loader;
            Parent root;

            switch (role) {
                case BASE:
                    loader = new FXMLLoader(getClass().getResource("/view/UserManagementView.fxml"));
                    break;
                case ADMIN:
                    loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
                    break;
                default:
                    throw new IllegalArgumentException("Ruolo utente non riconosciuto.");
            }

            root = loader.load();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
    }

    /**
     * Invoked when the user clicks the register button.
     * Checks if the username and password are valid and registers the user.
     *
     * @param event the event that triggered the method call
     */
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
        usernameField.clear();
        passwordField.clear();
    }

}
