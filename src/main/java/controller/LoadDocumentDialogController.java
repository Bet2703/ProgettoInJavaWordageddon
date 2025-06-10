package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;
import java.io.File;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import service.DocumentsManagement;
import service.Levels;

/**
 * Controller responsabile della gestione della finestra di dialogo per il caricamento di documenti.
 *
 * Questa classe gestisce le interazioni e la logica associate alla finestra di caricamento dei documenti,
 * inclusa la selezione del file, la scelta del livello di difficoltà e le azioni associate ai pulsanti
 * di conferma e annullamento. Integra i componenti dell'interfaccia grafica definiti nel file FXML.
 */
public class LoadDocumentDialogController {

    /**
     * Etichetta utilizzata per mostrare il nome del file selezionato dall’utente.
     *
     * Una volta scelto un file, il nome viene visualizzato per fornire un riscontro all’utente.
     */
    @FXML
    private Label fileNameLabel;

    /**
     * Pulsanti radio per la selezione del livello di difficoltà.
     *
     * Appartengono a un gruppo esclusivo che consente di selezionarne solo uno alla volta.
     * Il livello scelto viene usato per classificare il documento caricato.
     */
    @FXML
    private RadioButton easyRadio, mediumRadio, hardRadio;

    /**
     * Riferimento al file selezionato dall’utente.
     *
     * Se nessun file è stato selezionato, rimane null.
     */
    private File selectedFile;

    /**
     * Finestra di dialogo corrente associata a questo controller.
     *
     * Serve per gestire e chiudere la finestra al termine delle operazioni.
     */
    private Stage dialogStage;

    /**
     * Gruppo di selezione esclusiva per i pulsanti radio (un solo elemento selezionabile).
     */
    private ToggleGroup group;

    /**
     * Inizializza il controller impostando il gruppo di toggle per i livelli di difficoltà.
     *
     * Metodo invocato automaticamente dopo il caricamento del file FXML.
     */
    @FXML
    public void initialize() {
        group = new ToggleGroup();
        List<RadioButton> radios = Arrays.asList(easyRadio, mediumRadio, hardRadio);
        radios.forEach(rb -> rb.setToggleGroup(group));
    }

    /**
     * Imposta lo stage (finestra) per il dialogo.
     *
     * @param stage lo stage da associare al controller.
     */
    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    /**
     * Apre un file chooser per permettere la selezione di un file di testo.
     *
     * Aggiorna l’etichetta con il nome del file selezionato.
     */
    @FXML
    private void onChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di testo (*.txt)", "*.txt"));

        selectedFile = fileChooser.showOpenDialog(dialogStage);
        Optional.ofNullable(selectedFile)
                .map(File::getName)
                .ifPresent(fileNameLabel::setText);
    }

    /**
     * Gestisce l’azione di conferma (pulsante "Conferma").
     *
     * Verifica che siano stati selezionati sia un file che un livello di difficoltà.
     * Se sì, carica il documento nel database e mostra un messaggio di successo.
     * Altrimenti, mostra un avviso per informare l’utente che mancano dati.
     */
    @FXML
    private void onConfirm() {
        Optional<File> fileOpt = Optional.ofNullable(selectedFile);
        Optional<Levels.Difficulty> diffOpt = Optional.ofNullable(getSelectedDifficulty());

        if (fileOpt.isPresent() && diffOpt.isPresent()) {
            fileOpt.ifPresent(file -> DocumentsManagement.loadToDB(file, diffOpt.get()));
            new Alert(Alert.AlertType.INFORMATION, "Documento caricato con successo!").showAndWait();
            Optional.ofNullable(dialogStage).ifPresent(Stage::close);
        } else {
            new Alert(Alert.AlertType.WARNING, "Seleziona file e difficoltà.").showAndWait();
        }
    }

    /**
     * Chiude la finestra di dialogo senza eseguire ulteriori operazioni.
     *
     * Utilizzato principalmente per l’azione del pulsante "Annulla".
     */
    @FXML
    private void onCancel() {
        Optional.ofNullable(dialogStage).ifPresent(Stage::close);
    }

    /**
     * Restituisce il livello di difficoltà selezionato tramite i pulsanti radio.
     *
     * @return livello di difficoltà selezionato, oppure null se nessuno è selezionato.
     */
    private Levels.Difficulty getSelectedDifficulty() {
        return Arrays.asList(
                        new AbstractMap.SimpleEntry<>(easyRadio, Levels.Difficulty.EASY),
                        new AbstractMap.SimpleEntry<>(mediumRadio, Levels.Difficulty.MEDIUM),
                        new AbstractMap.SimpleEntry<>(hardRadio, Levels.Difficulty.HARD)
                ).stream()
                .filter(entry -> entry.getKey().isSelected())
                .map(AbstractMap.SimpleEntry::getValue)
                .findFirst()
                .orElse(null);
    }
}
