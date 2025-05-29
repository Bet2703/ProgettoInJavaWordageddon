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

    private ToggleGroup group;

    @FXML
    public void initialize() {
        group = new ToggleGroup();
        easyRadio.setToggleGroup(group);
        mediumRadio.setToggleGroup(group);
        hardRadio.setToggleGroup(group);
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    @FXML
    private void onChooseFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("File di testo (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);

        selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            fileNameLabel.setText(selectedFile.getName());
        }
    }

    @FXML
    private void onConfirm() {
        if (selectedFile != null && getSelectedDifficulty() != null) {
            // Passa file e difficoltà al metodo di caricamento documenti
            DocumentsManagement.loadToDB(selectedFile, getSelectedDifficulty());
            new Alert(Alert.AlertType.INFORMATION, "Documento caricato con successo!").showAndWait();
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
