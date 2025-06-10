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
 * Fornisce metodi per il passaggio a diverse schermate, come la selezione della difficoltà, 
 * la gestione del profilo utente, la visualizzazione della classifica personale o l'uscita dall'applicazione.
 * 
 * Ogni pulsante nel menu è associato a una vista diversa, caricata dinamicamente tramite FXMLLoader.
 * 
 * Autore: Gruppo6
 */
public class MainMenuController {

    /**
     * Mappa immutabile che associa i percorsi FXML ai titoli delle finestre corrispondenti.
     * Utilizzata per rendere il caricamento delle viste più chiaro e centralizzato.
     */
    private final Map<String, String> views;

    /**
     * Costruttore della classe.
     * Inizializza la mappa delle viste disponibili nel menu principale.
     */
    public MainMenuController() {
        Map<String, String> tempMap = new HashMap<>();
        tempMap.put("/view/Level.fxml", "Seleziona Difficoltà");
        tempMap.put("/view/UserManagementView.fxml", "Profilo");
        tempMap.put("/view/PersonalScoresView.fxml", "Classifica Personale");
        views = Collections.unmodifiableMap(tempMap); // rende la mappa non modificabile
    }

    /**
     * Metodo generico per il caricamento di una nuova vista FXML.
     * Carica il file FXML specificato, crea una nuova scena e la imposta nello stage attuale.
     *
     * @param event evento generato dal pulsante che ha attivato l'azione
     * @param fxmlPath percorso del file FXML da caricare
     * @param title titolo da assegnare alla finestra
     * @throws IOException se il file FXML non viene trovato o non può essere caricato
     */
    private void loadView(ActionEvent event, String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    /**
     * Gestisce il click sul pulsante "Gioca".
     * Carica la schermata per la selezione della difficoltà di gioco.
     *
     * @param event evento generato dal pulsante "Gioca"
     */
    @FXML
    public void onPlayClicked(ActionEvent event) {
        try {
            loadView(event, "/view/Level.fxml", "Seleziona Difficoltà");
        } catch (IOException e) {
            e.printStackTrace(); // stampa lo stack trace per il debug
        }
    }

    /**
     * Gestisce il click sul pulsante "Profilo".
     * Carica la schermata per la gestione dell'utente.
     *
     * @param event evento generato dal pulsante "Profilo"
     */
    @FXML
    public void onProfileClicked(ActionEvent event) {
        try {
            loadView(event, "/view/UserManagementView.fxml", "Profilo");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce il click sul pulsante "Classifica".
     * Carica la schermata della classifica personale dell'utente.
     *
     * @param event evento generato dal pulsante "Classifica"
     */
    @FXML
    public void onLeaderboardClicked(ActionEvent event) {
        try {
            loadView(event, "/view/PersonalScoresView.fxml", "Classifica Personale");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce il click sul pulsante "Esci".
     * Termina l'applicazione immediatamente tramite {@code System.exit(0)}.
     */
    @FXML
    public void onExitClicked() {
        System.exit(0);
    }
}
