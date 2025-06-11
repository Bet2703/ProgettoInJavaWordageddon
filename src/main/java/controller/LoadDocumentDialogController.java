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
 * Controller responsabile della gestione della finestra di dialogo per il caricamento dei documenti.
 *
 * <p>Consente all'utente di:
 * <ul>
 *     <li>Selezionare un file di testo dal file system</li>
 *     <li>Scegliere un livello di difficoltà</li>
 *     <li>Caricare il file nel sistema associandolo alla difficoltà</li>
 *     <li>Chiudere la finestra confermando o annullando l'operazione</li>
 * </ul>
 * </p>
 *
 * <p>Integra i componenti grafici definiti nel file FXML associato.</p>
 */
public class LoadDocumentDialogController {

    /**
     * Etichetta che mostra all'utente il nome del file selezionato.
     */
    @FXML
    private Label fileNameLabel;

    /**
     * Pulsanti radio associati ai tre livelli di difficoltà disponibili.
     *
     * Solo uno può essere selezionato alla volta grazie al gruppo di toggle.
     */
    @FXML
    private RadioButton easyRadio, mediumRadio, hardRadio;

    /**
     * File selezionato dall'utente tramite il file chooser.
     * Inizialmente è null finché l'utente non seleziona un file.
     */
    private File selectedFile;

    /**
     * Riferimento alla finestra di dialogo corrente.
     * Utilizzato per chiudere la finestra al termine delle operazioni.
     */
    private Stage dialogStage;

    /**
     * Gruppo di toggle per garantire la selezione esclusiva tra i RadioButton.
     */
    private ToggleGroup group;

    /**
     * Metodo invocato automaticamente dopo il caricamento del file FXML.
     *
     * <p>Associa i tre RadioButton al gruppo di selezione esclusiva
     * per garantire che solo uno possa essere selezionato alla volta.</p>
     */
    @FXML
    public void initialize() {
        group = new ToggleGroup();
        List<RadioButton> radios = Arrays.asList(easyRadio, mediumRadio, hardRadio);
        radios.forEach(rb -> rb.setToggleGroup(group));
    }

    /**
     * Metodo pubblico per impostare lo stage associato a questa finestra di dialogo.
     *
     * @param stage Finestra corrente da associare.
     */
    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    /**
     * Apre una finestra di selezione file (FileChooser) per scegliere un file di testo.
     *
     * <p>Se l'utente seleziona un file, viene aggiornato il campo {@code selectedFile}
     * e l'etichetta mostra il nome del file scelto.</p>
     */
    @FXML
    private void onChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di testo (*.txt)", "*.txt"));

        selectedFile = fileChooser.showOpenDialog(dialogStage);

        // Se un file è stato selezionato, aggiorna l'etichetta con il nome del file
        Optional.ofNullable(selectedFile)
                .map(File::getName)
                .ifPresent(fileNameLabel::setText);
    }

    /**
     * Azione associata al pulsante "Conferma".
     *
     * <p>Verifica che siano stati selezionati sia un file che un livello di difficoltà.</p>
     * <ul>
     *     <li>Se entrambi sono presenti, carica il documento nel database e chiude la finestra</li>
     *     <li>Se manca uno dei due, mostra un messaggio di avviso all'utente</li>
     * </ul>
     */
    @FXML
    private void onConfirm() {
        Optional<File> fileOpt = Optional.ofNullable(selectedFile);
        Optional<Levels.Difficulty> diffOpt = Optional.ofNullable(getSelectedDifficulty());

        if (fileOpt.isPresent() && diffOpt.isPresent()) {
            // Caricamento del file nel database con la difficoltà selezionata
            fileOpt.ifPresent(file -> DocumentsManagement.loadToDB(file, diffOpt.get()));

            // Messaggio di successo
            new Alert(Alert.AlertType.INFORMATION, "Documento caricato con successo!").showAndWait();

            // Chiude la finestra di dialogo
            Optional.ofNullable(dialogStage).ifPresent(Stage::close);
        } else {
            // Avvisa l'utente che non ha selezionato file o difficoltà
            new Alert(Alert.AlertType.WARNING, "Seleziona file e difficoltà.").showAndWait();
        }
    }

    /**
     * Azione associata al pulsante "Annulla".
     *
     * <p>Chiude semplicemente la finestra senza eseguire altre operazioni.</p>
     */
    @FXML
    private void onCancel() {
        Optional.ofNullable(dialogStage).ifPresent(Stage::close);
    }

    /**
     * Recupera il livello di difficoltà selezionato dall'utente.
     *
     * <p>Controlla quale RadioButton è selezionato e lo mappa in un valore
     * dell'enumerazione {@link Levels.Difficulty}.</p>
     *
     * @return Difficoltà selezionata o {@code null} se nessuna è selezionata.
     */
    private Levels.Difficulty getSelectedDifficulty() {
        return Arrays.asList(
                        new AbstractMap.SimpleEntry<>(easyRadio, Levels.Difficulty.EASY),
                        new AbstractMap.SimpleEntry<>(mediumRadio, Levels.Difficulty.MEDIUM),
                        new AbstractMap.SimpleEntry<>(hardRadio, Levels.Difficulty.HARD)
                ).stream()
                .filter(entry -> entry.getKey().isSelected()) // Verifica quale radio è attiva
                .map(AbstractMap.SimpleEntry::getValue)       // Ottiene il valore associato (EASY, MEDIUM, HARD)
                .findFirst()
                .orElse(null); // Se nessuna radio è selezionata, restituisce null
    }
}
