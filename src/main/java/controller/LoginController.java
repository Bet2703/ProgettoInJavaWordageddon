package controller;

import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.Login;
import users.Player;
import users.Role;

/**
 * Controller responsabile della gestione dell'autenticazione e della registrazione degli utenti.
 *
 * Questa classe è associata alla schermata di login e consente di:
 * <ul>
 *     <li>Effettuare il login mediante verifica delle credenziali.</li>
 *     <li>Registrare nuovi utenti come giocatori di ruolo BASE.</li>
 *     <li>Caricare la schermata corrispondente in base al ruolo dell'utente autenticato.</li>
 * </ul>
 *
 * Collegata a una vista FXML, integra componenti grafici come campi di testo, pulsanti e messaggi di stato.
 *
 * @author Gruppo6
 */
public class LoginController {

    /**
     * Campo di input per l'inserimento dello username da parte dell'utente.
     */
    @FXML
    private TextField usernameField;

    /**
     * Campo di input per l'inserimento della password da parte dell'utente (mascherato).
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Pulsante per avviare il processo di login.
     */
    @FXML
    private Button loginButton;

    /**
     * Etichetta per mostrare messaggi informativi o di errore all'utente.
     */
    @FXML
    private Label messageLabel;

    /**
     * Metodo invocato quando l'utente preme il pulsante di login.
     *
     * <p>
     * Verifica la presenza di username e password, tenta l'autenticazione tramite il servizio {@link Login},
     * e in caso di successo carica la vista corrispondente al ruolo dell'utente.
     * Se l'autenticazione fallisce, viene mostrato un messaggio di errore.
     * </p>
     *
     * @param event evento generato dal click sul pulsante di login.
     */
    @FXML
    private void onLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Optional<String> usernameOpt = Optional.ofNullable(username).filter(s -> !s.trim().isEmpty());
        Optional<String> passwordOpt = Optional.ofNullable(password).filter(s -> !s.trim().isEmpty());

        if (!usernameOpt.isPresent() || !passwordOpt.isPresent()) {
            messageLabel.setText("Inserisci username e password.");
            return;
        }

        Optional<Player> playerOpt = Optional.ofNullable(Login.login(username, password));

        if (playerOpt.isPresent()) {
            Player player = playerOpt.get();
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
     * Carica la schermata associata al ruolo dell'utente autenticato.
     *
     * <p>
     * I ruoli gestiti sono:
     * <ul>
     *     <li>{@link Role#BASE}: viene caricata la vista principale dell'utente (MainMenu).</li>
     *     <li>{@link Role#ADMIN}: viene caricata la vista amministratore (AdminView).</li>
     * </ul>
     * Se il ruolo è nullo o sconosciuto, viene sollevata un'eccezione.
     * </p>
     *
     * @param role ruolo dell'utente autenticato.
     * @throws IOException se si verifica un errore nel caricamento della vista.
     * @throws IllegalArgumentException se il ruolo è nullo o non riconosciuto.
     */
    private void loadViewForRole(Role role) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        String fxmlPath = Optional.ofNullable(role)
                .map(r -> {
                    switch (r) {
                        case BASE: return "/view/MainMenu.fxml";
                        case ADMIN: return "/view/AdminView.fxml";
                        default: throw new IllegalArgumentException("Ruolo utente non riconosciuto.");
                    }
                }).orElseThrow(() -> new IllegalArgumentException("Ruolo utente non specificato."));

        loader.setLocation(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Metodo invocato quando l'utente preme il pulsante "Registrati".
     *
     * <p>
     * Esegue la registrazione di un nuovo utente dopo aver verificato:
     * <ul>
     *     <li>Che entrambi i campi username e password siano compilati.</li>
     *     <li>Che lo username non sia già registrato nel sistema.</li>
     * </ul>
     * Se la registrazione ha successo, viene mostrato un messaggio e i campi vengono puliti.
     * </p>
     *
     * @param event evento generato dal click sul pulsante di registrazione.
     */
    @FXML
    private void onRegister(ActionEvent event) {
        Optional<String> usernameOpt = Optional.ofNullable(usernameField.getText()).filter(s -> !s.trim().isEmpty());
        Optional<String> passwordOpt = Optional.ofNullable(passwordField.getText()).filter(s -> !s.trim().isEmpty());

        if (!usernameOpt.isPresent() || !passwordOpt.isPresent()) {
            messageLabel.setText("Campi username e password obbligatori.");
            return;
        }

        String username = usernameOpt.get();

        if (Login.isUsernameTaken(username)) {
            messageLabel.setText("Username già esistente. Scegline un altro.");
            return;
        }

        Login.userRegister(username, passwordOpt.get(), Role.BASE);
        messageLabel.setText("Utente registrato con successo!");

        // Pulisce i campi di input
        usernameField.clear();
        passwordField.clear();
    }
}
