package controller;

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
 * Controller per la gestione della schermata di selezione della difficoltà.
 * 
 * <p>Questa classe si occupa di:
 * <ul>
 *   <li>Gestire la selezione del livello di difficoltà da parte dell'utente</li>
 *   <li>Avviare il gioco con la difficoltà selezionata</li>
 *   <li>Navigare tra le diverse schermate dell'applicazione</li>
 * </ul>
 * </p>
 *
 * @author Gruppo6
 */
public class LevelsController {

    /**
     * RadioButton per la selezione della difficoltà "Facile".
     * Fa parte di un gruppo di selezione esclusiva che permette all'utente
     * di scegliere un solo livello di difficoltà alla volta.
     */
    @FXML
    private RadioButton rbEasy;

    /**
     * RadioButton per la selezione della difficoltà "Medio".
     * Quando selezionato, imposta il gioco ad un livello di difficoltà intermedio.
     */
    @FXML
    private RadioButton rbMedium;

    /**
     * RadioButton per la selezione della difficoltà "Difficile".
     * Rappresenta il livello di sfida più alto disponibile nel gioco.
     */
    @FXML
    private RadioButton rbHard;

    /**
     * Etichetta per la visualizzazione di messaggi all'utente.
     * Utilizzata principalmente per:
     * <ul>
     *   <li>Confermare la selezione della difficoltà</li>
     *   <li>Segnalare errori</li>
     *   <li>Fornire feedback all'utente</li>
     * </ul>
     */
    @FXML
    private Label messageLabel;

    /**
     * Gruppo di selezione per i RadioButton delle difficoltà.
     * Garantisce che solo un RadioButton alla volta possa essere selezionato.
     */
    @FXML
    private ToggleGroup difficultyGroup;

    /**
     * Difficoltà attualmente selezionata dall'utente.
     * Può assumere i valori "EASY", "MEDIUM" o "HARD".
     * È memorizzata come variabile statica per essere accessibile
     * da altre classi dell'applicazione.
     */
    private static String selectedDifficulty;

    /**
     * Metodo di inizializzazione del controller.
     * Viene chiamato automaticamente dopo il caricamento del file FXML.
     * 
     * <p>Si occupa di:
     * <ol>
     *   <li>Inizializzare il gruppo di selezione</li>
     *   <li>Associare i RadioButton al gruppo</li>
     * </ol>
     * </p>
     */
    @FXML
    public void initialize() {
        difficultyGroup = new ToggleGroup();
        Stream.of(rbEasy, rbMedium, rbHard).forEach(rb -> rb.setToggleGroup(difficultyGroup));
    }

    /**
     * Gestisce l'azione di avvio del gioco.
     * 
     * <p>Operazioni eseguite:
     * <ol>
     *   <li>Verifica che sia stata selezionata una difficoltà</li>
     *   <li>Mappa la selezione in un codice standardizzato</li>
     *   <li>Mostra un messaggio di conferma all'utente</li>
     *   <li>Carica la schermata di lettura del documento</li>
     * </ol>
     * </p>
     * 
     * @param event L'evento generato dal click sul pulsante
     */
    @FXML
    private void onStartGame(ActionEvent event) {
        Optional<RadioButton> selected = Optional.ofNullable((RadioButton) difficultyGroup.getSelectedToggle());

        if (selected.isPresent()) {
            RadioButton radio = selected.get();
            selectedDifficulty = mapDifficulty(radio.getText());
            messageLabel.setText("Hai selezionato il livello: " + radio.getText());

            loadScene(event, "/view/DocumentReadView.fxml",
                    () -> messageLabel.setText("Errore nel caricamento del quiz."));
        } else {
            messageLabel.setText("Seleziona un livello di difficoltà per iniziare.");
        }
    }

    /**
     * Gestisce il ritorno al menu principale.
     * 
     * @param event L'evento generato dal click sul pulsante
     */
    @FXML
    private void onBack(ActionEvent event) {
        loadScene(event, "/view/MainMenu.fxml", 
                () -> messageLabel.setText("Errore nel tornare al Menu principale."));
    }

    /**
     * Carica una nuova scena sostituendo quella corrente.
     * 
     * <p>Parametri:
     * <ul>
     *   <li>event: Evento che ha originato la richiesta</li>
     *   <li>fxmlPath: Percorso del file FXML da caricare</li>
     *   <li>onError: Azione da eseguire in caso di errore</li>
     * </ul>
     * </p>
     */
    private void loadScene(ActionEvent event, String fxmlPath, Runnable onError) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            onError.run();
        }
    }

    /**
     * Mappa il testo del RadioButton nel codice di difficoltà corrispondente.
     * 
     * @param text Il testo del RadioButton selezionato
     * @return Il codice della difficoltà ("EASY", "MEDIUM" o "HARD")
     */
    private String mapDifficulty(String text) {
        List<String> labels = Arrays.asList("Facile", "Medio", "Difficile");
        List<String> codes = Arrays.asList("EASY", "MEDIUM", "HARD");

        return Stream.iterate(0, i -> i + 1)
                .limit(labels.size())
                .filter(i -> labels.get(i).equalsIgnoreCase(text))
                .findFirst()
                .map(codes::get)
                .orElse("EASY");  // valore di default
    }

    /**
     * Restituisce la difficoltà attualmente selezionata.
     * 
     * @return La difficoltà selezionata ("EASY", "MEDIUM" o "HARD")
     */
    public static String getDifficulty() {
        return selectedDifficulty;
    }
}