package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * La classe MainMenuController gestisce le interazioni dell'utente nel menu principale dell'applicazione.
 * <p>
 * Permette il passaggio a diverse schermate: selezione della difficoltà, gestione del profilo, 
 * visualizzazione della classifica personale e uscita dall'app.
 * Ogni pulsante del menu carica una vista diversa tramite {@link FXMLLoader}.
 * </p>
 *
 * <p>Autore: Gruppo6</p>
 */
public class MainMenuController {

    /**
     * Mappa immutabile che associa i percorsi FXML ai titoli delle finestre.
     * Questo approccio centralizza la configurazione delle viste rendendo più facile la manutenzione.
     */
    private final Map<String, String> views;

    /**
     * Costruttore della classe.
     * <p>
     * Inizializza la mappa con i vari percorsi delle viste disponibili nel menu principale.
     * La mappa è resa immutabile per evitare modifiche accidentali.
     * </p>
     */
    public MainMenuController() {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("/view/Level.fxml", "Seleziona Difficoltà");
        tempMap.put("/view/UserManagementView.fxml", "Profilo");
        tempMap.put("/view/PersonalScoresView.fxml", "Classifica Personale");
        views = Collections.unmodifiableMap(tempMap); // Rende la mappa costante dopo l'inizializzazione
    }

    /**
     * Metodo generico per il caricamento di una vista FXML.
     *
     * <p>
     * Recupera il file FXML dalla risorsa, lo carica in una nuova scena e imposta la scena 
     * nello stage attuale ottenuto tramite l'evento.
     * </p>
     *
     * @param event evento associato al pulsante cliccato
     * @param fxmlPath percorso del file FXML da caricare
     * @throws IOException se il file FXML non può essere caricato
     */
    private void loadView(ActionEvent event, String fxmlPath) throws IOException {
        // Inizializza il loader con il percorso specificato
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        
        // Carica la scena dal file FXML
        Scene scene = new Scene(loader.load());

        // Ottiene lo stage corrente a partire dal nodo che ha generato l'evento
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Imposta la nuova scena nello stage corrente
        stage.setScene(scene);
        stage.show(); // Visualizza la nuova scena
    }

    /**
     * Gestisce il click sul pulsante "Gioca".
     * <p>
     * Carica la vista per la selezione della difficoltà del gioco.
     * </p>
     *
     * @param event evento generato dal pulsante "Gioca"
     */
    @FXML
    public void onPlayClicked(ActionEvent event) {
        try {
            // Carica la vista associata alla difficoltà
            loadView(event, "/view/Level.fxml");
        } catch (IOException e) {
            // Stampa lo stack trace in caso di errore
            e.printStackTrace();
        }
    }

    /**
     * Gestisce il click sul pulsante "Profilo".
     * <p>
     * Carica la vista per la gestione del profilo utente.
     * </p>
     *
     * @param event evento generato dal pulsante "Profilo"
     */
    @FXML
    public void onProfileClicked(ActionEvent event) {
        try {
            // Carica la schermata di gestione utente
            loadView(event, "/view/UserManagementView.fxml");
        } catch (IOException e) {
            // Gestione dell'eccezione nel caricamento della vista
            e.printStackTrace();
        }
    }

    /**
     * Gestisce il click sul pulsante "Classifica".
     * <p>
     * Carica la schermata della classifica personale dell’utente.
     * </p>
     *
     * @param event evento generato dal pulsante "Classifica"
     */
    @FXML
    public void onLeaderboardClicked(ActionEvent event) {
        try {
            // Carica la schermata della classifica personale
            loadView(event, "/view/PersonalScoresView.fxml");
        } catch (IOException e) {
            // Mostra l'errore a console se il file non viene caricato correttamente
            e.printStackTrace();
        }
    }

    /**
     * Gestisce il click sul pulsante "Esci".
     * <p>
     * Chiude immediatamente l'applicazione tramite {@code System.exit(0)}.
     * </p>
     */
    @FXML
    public void onExitClicked() {
        // Chiude l'applicazione
        System.exit(0);
    }
}
