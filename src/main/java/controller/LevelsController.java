package controller;

// Import di una classe personalizzata per mostrare messaggi di errore all’utente
import alert.Messages;

// Import delle classi JavaFX necessarie per gestire eventi, GUI e scene
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Controller associato alla schermata per la selezione del livello di difficoltà del gioco.
 * Permette all’utente di scegliere tra "Facile", "Medio" o "Difficile",
 * avvia il gioco o torna al menu principale.
 */
public class LevelsController {

    // RadioButton che rappresenta la scelta del livello "Facile"
    @FXML
    private RadioButton rbEasy;

    // RadioButton per la difficoltà "Medio"
    @FXML
    private RadioButton rbMedium;

    // RadioButton per la difficoltà "Difficile"
    @FXML
    private RadioButton rbHard;

    // Label per mostrare messaggi di feedback all’utente (es. errori o conferme)
    @FXML
    private Label messageLabel;

    // Gruppo di Toggle per garantire che solo un RadioButton sia selezionato alla volta
    @FXML
    private ToggleGroup difficultyGroup;

    // Variabile statica che conserva la difficoltà selezionata tra le schermate
    private static String selectedDifficulty;

    /**
     * Metodo chiamato automaticamente da JavaFX subito dopo il caricamento del file FXML.
     * Inizializza il gruppo di RadioButton per garantire selezione esclusiva.
     */
    @FXML
    public void initialize() {
        // Crea un nuovo gruppo di toggle
        difficultyGroup = new ToggleGroup();

        // Assegna tutti i RadioButton al gruppo creato
        Stream.of(rbEasy, rbMedium, rbHard).forEach(rb -> rb.setToggleGroup(difficultyGroup));
    }

    /**
     * Metodo eseguito quando l'utente clicca sul pulsante "Avvia".
     * Controlla che sia stata selezionata una difficoltà,
     * la mappa in un codice interno (EASY, MEDIUM, HARD),
     * mostra un messaggio e passa alla schermata successiva.
     */
    @FXML
    private void onStartGame(ActionEvent event) {
        // Ottiene il RadioButton selezionato, se presente
        Optional<RadioButton> selected = Optional.ofNullable((RadioButton) difficultyGroup.getSelectedToggle());

        if (selected.isPresent()) {
            // Ottiene il RadioButton selezionato
            RadioButton radio = selected.get();

            // Traduce il testo del RadioButton in un codice standard ("EASY", ecc.)
            selectedDifficulty = mapDifficulty(radio.getText());

            // Mostra all'utente la difficoltà scelta
            messageLabel.setText("Hai selezionato il livello: " + radio.getText());

            // Carica la schermata di lettura del documento (prossima fase del gioco)
            loadScene(event, "/view/DocumentReadView.fxml",
                    () -> messageLabel.setText("Errore nel caricamento del quiz."));
        } else {
            // Nessuna difficoltà selezionata: mostra errore
            messageLabel.setText("Seleziona un livello di difficoltà per iniziare.");
        }
    }

    /**
     * Metodo chiamato quando si clicca sul pulsante "Indietro".
     * Torna alla schermata principale del menu.
     */
    @FXML
    private void onBack(ActionEvent event) {
        // Carica la schermata del menu principale
        loadScene(event, "/view/MainMenu.fxml", 
                () -> messageLabel.setText("Errore nel tornare al Menu principale."));
    }

    /**
     * Metodo ausiliario per caricare e visualizzare una nuova scena.
     * 
     * @param event Evento generato dal click
     * @param fxmlPath Percorso al file FXML da caricare
     * @param onError Azione da eseguire in caso di errore nel caricamento
     */
    private void loadScene(ActionEvent event, String fxmlPath, Runnable onError) {
        try {
            // Carica il file FXML indicato
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Ottiene lo stage (finestra) corrente e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            // In caso di errore di caricamento, mostra messaggio e stampa lo stack trace
            Messages.showError("Errore loadScene", "Si è verificato un errore nel metodo che apre fxml della pagina");
            e.printStackTrace();
            onError.run();
        }
    }

    /**
     * Metodo che converte il testo selezionato del RadioButton (in italiano)
     * in un codice interno usato dal programma.
     * 
     * @param text Testo visualizzato nel RadioButton (es. "Facile")
     * @return Codice corrispondente ("EASY", "MEDIUM", "HARD")
     */
    private String mapDifficulty(String text) {
        List<String> labels = Arrays.asList("Facile", "Medio", "Difficile");
        List<String> codes = Arrays.asList("EASY", "MEDIUM", "HARD");

        // Cerca l'indice del testo corrispondente e restituisce il codice relativo
        return Stream.iterate(0, i -> i + 1)
                .limit(labels.size())
                .filter(i -> labels.get(i).equalsIgnoreCase(text))
                .findFirst()
                .map(codes::get)
                .orElse("EASY");  // Default in caso di mancata corrispondenza
    }

    /**
     * Metodo statico che restituisce la difficoltà selezionata dall’utente.
     * Utile per accedere a questa informazione da altre classi.
     * 
     * @return Codice della difficoltà selezionata ("EASY", "MEDIUM", "HARD")
     */
    public static String getDifficulty() {
        return selectedDifficulty;
    }
}
