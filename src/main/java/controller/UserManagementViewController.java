package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.GameSession;
import service.UsersManagement;

/**
 * The UserManagementViewController class is responsible for managing the user interface for
 * user profile updates, session exports, and navigation within the application, such as
 * transitioning to the game level scene. It implements the Initializable interface to
 * initialize and manage UI components and application logic.
 *
 * @author Gruppo6
 */
public class UserManagementViewController implements Initializable {

    /**
     * Represents the input field for the username in the User Management view.
     * This field is bound to the FXML file and allows the user to enter their username.
     * It is used in various operations such as user profile validation and management.
     */
    @FXML
    private TextField tfUsername;

    /**
     * A PasswordField component in the FXML view, representing the input field for the user's password.
     * This field is used for securely capturing and handling user-entered passwords without displaying visible text.
     */
    @FXML
    private PasswordField pfPassword;

    /**
     * Represents a password confirmation input field in the User Management View.
     * This field is used to capture the confirmation of the new password entered by the user.
     * It is typically validated against the main password field to ensure the values match
     * before saving the profile changes.
     */
    @FXML
    private PasswordField pfConfirm;

    /**
     * Represents the label component in the UserManagementViewController used to display status messages to the user.
     * This label serves as a feedback mechanism, providing information such as success, error, or other
     * contextual messages related to user management tasks.
     */
    @FXML
    private Label lblStatus;

    /**
     * Manages operations related to user accounts, such as updating,
     * deleting, and retrieving user information, as well as managing
     * user sessions and profiles.
     * <p>
     * This variable serves as a reference to the {@code UsersManagement} service
     * which provides functionalities to interact with the user database and handle
     * user-specific actions required by the controller.
     */
    private UsersManagement usersManagement;

    /**
     * Represents the unique identifier of the current user within the system.
     * This variable is used to track and identify the user interacting with the application.
     * In a real-world scenario, the user ID would typically be retrieved from a session or
     * authentication service to ensure it corresponds to the logged-in user.
     */
    private int userId = 1;

    /**
     * Initializes the UserManagementViewController by setting up the required components.
     * This method is automatically called after the FXML file has been loaded.
     * It creates a new instance of {@link UsersManagement} and resets the status label.
     *
     * @param url the location used to resolve relative paths for the root object, or {@code null} if the location is not known.
     * @param rb  the resource bundle used to localize the root object, or {@code null} if the resource bundle is not specified.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usersManagement = new UsersManagement();
        lblStatus.setText("");
    }

    /**
     * Handles the event of saving the user's profile changes.
     * Validates the input fields for username, password, and confirm password.
     * Displays error messages for invalid input or mismatched passwords.
     * Updates the user's profile data using the usersManagement service.
     *
     * @param event the ActionEvent that triggered the method call.
     */
    @FXML
    private void onSaveProfile(ActionEvent event) {
        String username = tfUsername.getText();
        String password = pfPassword.getText();
        String confirmPassword = pfConfirm.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            lblStatus.setText("Tutti i campi devono essere compilati.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            lblStatus.setText("Le password non coincidono.");
            return;
        }

        boolean updated = usersManagement.updateUser(userId, username, password);
        if (updated) {
            lblStatus.setText("Profilo aggiornato con successo!");
        } else {
            lblStatus.setText("Errore durante l'aggiornamento.");
        }
    }

    /**
     * Handles the action of cancelling the changes made in the user input fields.
     * This method clears the input fields for username, password, and confirm password,
     * and resets the status label with a message indicating that the changes have been canceled.
     *
     * @param event the ActionEvent that triggered the method call.
     */
    @FXML
    private void onCancel(ActionEvent event) {
        // Ripristina i campi vuoti o da DB
        tfUsername.clear();
        pfPassword.clear();
        pfConfirm.clear();
        lblStatus.setText("Modifiche annullate.");
    }

    /**
     * Handles the export of game session data to a CSV file.
     * This method retrieves the game sessions associated with the current user,
     * formats them into CSV content, and writes it to a file.
     * If no sessions are found, or if an error occurs during writing,
     * an appropriate status message is displayed.
     *
     * @param event the ActionEvent that triggered the export operation.
     */
    @FXML
    private void onExportCsv(ActionEvent event) {
        String username = service.GameSessionManagement.getInstance().getUsername();
        List<GameSession> sessions = service.GameSessionManagement.getSessionsByUsername(username);

        if (sessions.isEmpty()) {
            lblStatus.setText("Nessuna sessione trovata per l'esportazione.");
            return;
        }

        File file = new File("sessioni_" + username + ".csv");

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("DocumentID,Difficulty,Score,Timestamp");

            sessions.stream()
                    .map(s -> String.format("%d,%s,%d,%s",
                            s.getDocumentId(),
                            s.getDifficulty(),
                            s.getScore(),
                            s.getTimestamp()))
                    .forEach(writer::println);

            lblStatus.setText("Sessioni esportate in " + file.getAbsolutePath());

        } catch (IOException e) {
            lblStatus.setText("Errore durante l'esportazione.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the action of navigating back to the main menu.
     * This method loads the main menu view from the associated FXML file
     * and updates the current stage's scene to display the main menu.
     * If an IOException occurs during the loading of the FXML file,
     * an error message is printed to the standard error stream.
     *
     * @param event the ActionEvent that triggered the navigation.
     */
    @FXML
    private void onGoBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Menu Principale");
            stage.show();

        } catch (IOException e) {
            System.err.println("Errore durante il ritorno al menu: " + e.getMessage());
        }
    }
}
