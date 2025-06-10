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
 * La classe LoginController gestisce le interazioni legate all'autenticazione
 * e alla registrazione degli utenti. Controlla i dati inseriti, avvia il processo
 * di autenticazione, carica le schermate appropriate in base al ruolo dell'utente,
 * e fornisce feedback tramite l'interfaccia utente.
 *
 * Questa classe è collegata a una vista FXML.
 * 
 * Autore: Gruppo6
 */
public class LoginController {

    /**
     * Campo di testo per l'inserimento del nome utente.
     * Associato all'interfaccia utente tramite FXML, serve per raccogliere il nome utente
     * durante le operazioni di login o registrazione.
     */
    @FXML
    private TextField usernameField;

    /**
     * Campo password per l'inserimento sicuro della password.
     * Visualizza i caratteri in forma mascherata per proteggere i dati sensibili.
     * Associato tramite FXML all'interfaccia grafica.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Pulsante che avvia il processo di login quando viene premuto.
     * Valida i dati inseriti e, se corretti, tenta di autenticare l'utente
     * e carica la schermata corrispondente al suo ruolo.
     */
    @FXML
    private Button loginButton;

    /**
     * Etichetta per mostrare messaggi all'utente.
     * Viene utilizzata per comunicare errori, avvisi o conferme
     * durante il login o la registrazione.
     */
    @FXML
    private Label messageLabel;

    /**
     * Metodo invocato quando viene premuto il pulsante di login.
     *
     * Esegue le seguenti operazioni:
     * - Verifica che i campi username e password non siano vuoti.
     * - Chiama il metodo di autenticazione dal servizio Login.
     * - Se l'autenticazione ha successo, imposta l'utente attuale nella sessione e carica la schermata adatta.
     * - In caso di errore, mostra un messaggio di errore all'utente.
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
     * Carica la schermata corretta in base al ruolo dell’utente autenticato.
     *
     * In base al ruolo restituito dall'oggetto Player, il metodo decide quale vista FXML caricare:
     * - `Role.BASE` → carica il menu principale dell’utente.
     * - `Role.ADMIN` → carica la vista amministratore.
     *
     * Se il ruolo è nullo o non riconosciuto, viene sollevata un’eccezione.
     * Dopo il caricamento, imposta la nuova scena nello stage attuale.
     *
     * @param role ruolo dell’utente autenticato.
     * @throws IOException se si verifica un errore durante il caricamento del file FXML.
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
     * Gestisce la registrazione di un nuovo utente quando viene premuto il pulsante "Registrati".
     *
     * Esegue le seguenti operazioni:
     * - Verifica che i campi username e password siano compilati.
     * - Controlla che l’username non sia già presente nel sistema.
     * - Registra il nuovo utente come giocatore base (`Role.BASE`) tramite il servizio Login.
     * - Mostra un messaggio di conferma all’utente e svuota i campi di input.
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
        Runnable clearFields = () -> {
            usernameField.clear();
            passwordField.clear();
        };
        clearFields.run();
    }
}
