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
import model.GameSession;
import management.UsersManagement;

/**
 * La classe UserManagementViewController è responsabile della gestione dell'interfaccia utente per
 * l'aggiornamento del profilo utente, l'esportazione delle sessioni e la navigazione all'interno 
 * dell'applicazione, come il passaggio alla scena del livello di gioco. Implementa l'interfaccia 
 * Initializable per inizializzare e gestire i componenti dell'interfaccia utente e la logica dell'applicazione.
 *
 * @author Gruppo6
 */
public class UserManagementViewController implements Initializable {

    /**
     * Campo di testo per l'inserimento del nome utente nella vista di gestione utente.
     * Questo campo è collegato al file FXML e permette all'utente di inserire il proprio username.
     * Viene utilizzato in varie operazioni come la validazione e la gestione del profilo utente.
     */
    @FXML
    private TextField tfUsername;

    /**
     * Componente PasswordField nella vista FXML, rappresenta il campo di inserimento per la password dell'utente.
     * Questo campo viene utilizzato per acquisire e gestire in modo sicuro le password inserite 
     * dall'utente senza mostrare il testo in chiaro.
     */
    @FXML
    private PasswordField pfPassword;

    /**
     * Rappresenta un campo di input per la conferma della password nella vista di gestione utente.
     * Questo campo viene utilizzato per acquisire la conferma della nuova password inserita dall'utente.
     * Tipicamente viene validato rispetto al campo password principale per assicurarsi che i valori coincidano
     * prima di salvare le modifiche al profilo.
     */
    @FXML
    private PasswordField pfConfirm;

    /**
     * Componente label nel UserManagementViewController utilizzato per mostrare messaggi di stato all'utente.
     * Questa etichetta funge da meccanismo di feedback, fornendo informazioni come successo, errore o altri
     * messaggi contestuali relativi alle operazioni di gestione utente.
     */
    @FXML
    private Label lblStatus;

    /**
     * Gestisce le operazioni relative agli account utente, come l'aggiornamento,
     * la cancellazione e il recupero delle informazioni utente, oltre alla gestione
     * delle sessioni e dei profili utente.
     * <p>
     * Questa variabile serve come riferimento al servizio {@code UsersManagement}
     * che fornisce le funzionalità per interagire con il database utente e gestire
     * le azioni specifiche per l'utente richieste dal controller.
     */
    private UsersManagement usersManagement;

    /**
     * Rappresenta l'identificatore univoco dell'utente corrente all'interno del sistema.
     * Questa variabile viene utilizzata per tracciare e identificare l'utente che interagisce con l'applicazione.
     * In uno scenario reale, l'ID utente verrebbe tipicamente recuperato da un servizio di sessione o
     * autenticazione per assicurarsi che corrisponda all'utente loggato.
     */
    private int userId = 1;

    /**
     * Inizializza il UserManagementViewController configurando i componenti necessari.
     * Questo metodo viene chiamato automaticamente dopo il caricamento del file FXML.
     * Crea una nuova istanza di {@link UsersManagement} e resetta l'etichetta di stato.
     *
     * @param url la posizione utilizzata per risolvere i percorsi relativi per l'oggetto root, o {@code null} se la posizione non è nota.
     * @param rb  il resource bundle utilizzato per localizzare l'oggetto root, o {@code null} se il resource bundle non è specificato.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inizializzazione del gestore degli utenti
        usersManagement = new UsersManagement();
        // Reset del label di stato a vuoto
        lblStatus.setText("");
    }

    /**
     * Gestisce l'evento di salvataggio delle modifiche al profilo utente.
     * Valida i campi di input per username, password e conferma password.
     * Mostra messaggi di errore per input non validi o password non coincidenti.
     * Aggiorna i dati del profilo utente utilizzando il servizio usersManagement.
     *
     * @param event l'ActionEvent che ha triggerato la chiamata al metodo.
     */
    @FXML
    private void onSaveProfile(ActionEvent event) {
        // Recupero dei valori dai campi di input
        String username = tfUsername.getText().trim();
        String password = pfPassword.getText();
        String confirmPassword = pfConfirm.getText();

        // Validazione campi obbligatori
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            lblStatus.setText("Tutti i campi devono essere compilati.");
            return;
        }

        // Controllo corrispondenza password
        if (!password.equals(confirmPassword)) {
            lblStatus.setText("Le password non coincidono.");
            return;
        }

        // Chiamata al servizio per l'aggiornamento
        boolean updated = usersManagement.updateUser(userId, username, password);
        
        // Feedback all'utente basato sull'esito
        if (updated) {
            lblStatus.setText("Profilo aggiornato con successo!");
        } else {
            lblStatus.setText("Errore durante l'aggiornamento.");
        }
    }

    /**
     * Gestisce l'azione di annullamento delle modifiche apportate ai campi di input utente.
     * Questo metodo svuota i campi di input per username, password e conferma password,
     * e resetta l'etichetta di stato con un messaggio che indica che le modifiche sono state annullate.
     *
     * @param event l'ActionEvent che ha triggerato la chiamata al metodo.
     */
    @FXML
    private void onCancel(ActionEvent event) {
        // Pulizia di tutti i campi di input
        tfUsername.clear();
        pfPassword.clear();
        pfConfirm.clear();
        
        // Notifica all'utente dell'avvenuto annullamento e dei campi svuotati
        lblStatus.setText("Modifiche annullate e campi svuotati.");
    }

    /**
     * Gestisce l'esportazione dei dati delle sessioni di gioco in un file CSV.
     * Questo metodo recupera le sessioni di gioco associate all'utente corrente,
     * le formatta in contenuto CSV e le scrive in un file.
     * Se non vengono trovate sessioni, o se si verifica un errore durante la scrittura,
     * viene mostrato un messaggio di stato appropriato.
     *
     * @param event l'ActionEvent che ha triggerato l'operazione di esportazione.
     */
    @FXML
    private void onExportCsv(ActionEvent event) {
        // Recupero username corrente da GameSessionManagement
        String username = management.GameSessionManagement.getInstance().getUsername();
        
        // Recupero tutte le sessioni per l'utente
        List<GameSession> sessions = management.GameSessionManagement.getSessionsByUsername(username);

        // Controllo presenza sessioni da esportare
        if (sessions.isEmpty()) {
            lblStatus.setText("Nessuna sessione trovata per l'esportazione.");
            return;
        }

        // Creazione file CSV con nome personalizzato
        File file = new File("sessioni_" + username + ".csv");

        try (PrintWriter writer = new PrintWriter(file)) {
            // Scrittura intestazione CSV
            writer.println("DocumentID,Difficulty,Score,Timestamp");

            // Scrittura di ogni sessione come riga nel file
            for (model.GameSession session : sessions) {
                writer.printf("%d,%s,%d,%s%n",
                    session.getDocumentId(),
                    session.getDifficulty(),
                    session.getScore(),
                    session.getTimestamp());
            }

            // Notifica successo con path completo del file
            lblStatus.setText("Sessioni esportate in " + file.getAbsolutePath());

        } catch (IOException e) {
            // Gestione errori di I/O
            lblStatus.setText("Errore durante l'esportazione.");
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'azione di ritorno al menu principale.
     * Questo metodo carica la vista del menu principale dal file FXML associato
     * e aggiorna la scena dello stage corrente per mostrare il menu principale.
     * Se si verifica un'IOException durante il caricamento del file FXML,
     * viene stampato un messaggio di errore nello standard error stream.
     *
     * @param event l'ActionEvent che ha triggerato la navigazione.
     */
    @FXML
    private void onGoBack(ActionEvent event) {
        try {
            // Caricamento della scena del menu principale
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            
            // Creazione della nuova scena
            Scene scene = new Scene(loader.load());
            
            // Recupero dello stage corrente
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Impostazione della nuova scena e visualizzazione
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // Log dell'errore nel caso il caricamento fallisca
            System.err.println("Errore durante il ritorno al menu: " + e.getMessage());
        }
    }
}