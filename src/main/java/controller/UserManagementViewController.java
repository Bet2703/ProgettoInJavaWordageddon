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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.GameSession;
import service.UsersManagement;

public class UserManagementViewController implements Initializable {

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private PasswordField pfConfirm;
    @FXML
    private Label lblBestScore;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnExportCsv;
    @FXML
    private Label lblStatus;

    private UsersManagement usersManagement;
    private int userId = 1; // Simulazione: userId corrente (in un'app vera, da sessione)

    /**
     * Initializes the controller class.
     *
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param rb
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usersManagement = new UsersManagement();
        lblStatus.setText("");
    }

    /**
     * Updates the user profile with the new values entered in the fields
     *
     * @param event the event that triggered the method call
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

        // Per ora, supponiamo che l'email non venga usata: passiamo username e password
        boolean updated = usersManagement.updateUser(userId, username, password);
        if (updated) {
            lblStatus.setText("Profilo aggiornato con successo!");
        } else {
            lblStatus.setText("Errore durante l'aggiornamento.");
        }
    }

    /**
     * Cancels the profile update and restores the fields to their original values
     *
     * @param event
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
     * Exports the user sessions with results in a CSV file
     *
     * @param event the event that triggered the method call
     */
    @FXML
    private void onExportCsv(ActionEvent event) {
        String username = service.GameSessionManagement.getInstance().getUsername();
        List<GameSession> sessions = usersManagement.getSessionsByUsername(username);

        if (sessions.isEmpty()) {
            lblStatus.setText("Nessuna sessione trovata per l'esportazione.");
            return;
        }

        File file = new File("sessioni_" + username + ".csv");

        try (PrintWriter writer = new PrintWriter(file)) {
            writer.println("DocumentID,Difficulty,Score,Timestamp");

            for (GameSession s : sessions) {
                writer.printf("%d,%s,%d,%s%n",
                        s.getDocumentId(),
                        s.getDifficulty(),
                        s.getScore(),
                        s.getTimestamp());
            }

            lblStatus.setText("Sessioni esportate in " + file.getAbsolutePath());

        } catch (IOException e) {
            lblStatus.setText("Errore durante l'esportazione.");
            e.printStackTrace();
        }
    }

    /**
     * Let the player start the game by going to the difficulty selection view
     *
     * @param event
     */
    @FXML
    private void onPlayGame(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Level.fxml"));
            Parent root = loader.load();

            // Ottieni lo stage attuale a partire da un qualsiasi nodo (es. il pulsante premuto)
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            // Sostituisci la scena
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Errore durante il caricamento della schermata: " + e.getMessage());
        }
    }
}

