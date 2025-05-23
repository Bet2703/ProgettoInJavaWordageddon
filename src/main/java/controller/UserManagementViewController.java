package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usersManagement = new UsersManagement();
        lblStatus.setText("");
    }

    @FXML
    private void onSaveProfile(ActionEvent event) {
        String username = tfUsername.getText();
        String password = pfPassword.getText();
        String confirmPassword = pfConfirm.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            lblStatus.setText("⚠️ Tutti i campi devono essere compilati.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            lblStatus.setText("❌ Le password non coincidono.");
            return;
        }

        // Per ora, supponiamo che l'email non venga usata: passiamo username e password
        boolean updated = usersManagement.updateUser(userId, username, password);
        if (updated) {
            lblStatus.setText("✅ Profilo aggiornato con successo!");
        } else {
            lblStatus.setText("❌ Errore durante l'aggiornamento.");
        }
    }

    @FXML
    private void onCancel(ActionEvent event) {
        // Ripristina i campi vuoti o da DB
        tfUsername.clear();
        pfPassword.clear();
        pfConfirm.clear();
        lblStatus.setText("Modifiche annullate.");
    }

    @FXML
    private void onExportCsv(ActionEvent event) {
        // TODO: esportazione CSV se necessaria
        lblStatus.setText("⚙️ Funzione di esportazione non implementata.");
    }

    @FXML
    private void onPlayGame(ActionEvent event) {
    }
}

