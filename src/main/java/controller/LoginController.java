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

/**
 * The LoginController class is responsible for managing user login
 * and registration interactions within the application. It handles
 * input validation, authentication, and UI transitions based on
 * user roles. The class is associated with an FXML view.
 *
 * @author Gruppo6
 */
public class LoginController {

    /**
     * The `usernameField` variable represents a text input field in the login form.
     * This field is used to collect the username input from*/
    @FXML
    private TextField usernameField;

    /**
     * Represents a PasswordField that captures user input for passwords.
     * This component is used to securely input and display obfuscated
     * characters to protect sensitive information like user passwords.
     * Connected to the GUI through FXML in the LoginController.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * The button component used for initiating the login action.
     * This button triggers the login process when clicked, validating the entered
     * username and password and directing the user to the appropriate view upon success.
     */
    @FXML
    private Button loginButton;

    /**
     * Label component used to display messages to the user.
     * This label is updated programmatically to provide feedback, error notifications,
     * or other contextual information within the context of the LoginController.
     */
    @FXML
    private Label messageLabel;

    /**
     * Handles the login process when the login button is clicked.
     * Validates user inputs, attempts authentication, and loads the appropriate
     * UI based on the user's role if authentication is successful.
     * Displays error messages for invalid credentials, missing fields,
     * or issues during view loading.
     *
     * @param event the action event that triggered this method
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
     * Loads the appropriate view based on the user's role and sets it as the current scene.
     *
     * The method distinguishes between the `BASE` and `ADMIN` roles to load the respective FXML file.
     * An `IllegalArgumentException` is thrown if the role is not recognized. After loading the FXML file,
     * the method updates the scene of the current stage and displays it. It also handles possible I/O
     * errors during the loading process.
     *
     * @param role the role of the user, used to determine which view to load. Accepted values are
     *             `Role.BASE` and `Role.ADMIN`.
     *
     * @throws IOException if an error occurs while loading the FXML file for the specified role.
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
     * Handles the user registration process when the register button is clicked.
     * Validates the input fields, checks for already existing usernames,
     * registers the user if the input is valid, and updates the UI with appropriate messages.
     *
     * @param event the action event that triggered this method
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
