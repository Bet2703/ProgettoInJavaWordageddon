package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;
import java.io.File;
import service.DocumentsManagement;
import service.Levels;

public class LoadDocumentDialogController {

    @FXML private Label fileNameLabel;
    @FXML private RadioButton easyRadio, mediumRadio, hardRadio;
    private File selectedFile;
    private Stage dialogStage;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
        ToggleGroup group = new ToggleGroup();
        easyRadio.setToggleGroup(group);
        mediumRadio.setToggleGroup(group);
        hardRadio.setToggleGroup(group);
    }

    @FXML
    private void onChooseFile() {
        FileChooser fileChooser = new FileChooser();

        // Imposta il filtro per accettare solo file .txt
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
            "File di testo (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            // Verifica aggiuntiva sull'estensione
            if (selectedFile.getName().toLowerCase().endsWith(".txt")) {
                fileNameLabel.setText(selectedFile.getName());
            } else {
                // Mostra un alert se il file non è .txt
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Tipo file non valido");
                alert.setHeaderText(null);
                alert.setContentText("Seleziona solo file con estensione .txt");
                alert.showAndWait();
                selectedFile = null;  // Resetta la selezione
            }
        }
    }

    @FXML
    private void onConfirm() {
        if (selectedFile != null && getSelectedDifficulty() != null) {
            // Passa file e difficoltà al metodo di caricamento documenti
            DocumentsManagement.loadToDB(selectedFile, getSelectedDifficulty());
            dialogStage.close();
        } else {
            new Alert(Alert.AlertType.WARNING, "Seleziona file e difficoltà.").showAndWait();
        }
    }

    @FXML
    private void onCancel() {
        dialogStage.close();
    }

    private Levels.Difficulty getSelectedDifficulty() {
        if (easyRadio.isSelected()) return Levels.Difficulty.EASY;
        if (mediumRadio.isSelected()) return Levels.Difficulty.MEDIUM;
        if (hardRadio.isSelected()) return Levels.Difficulty.HARD;
        return null;
    }
}
