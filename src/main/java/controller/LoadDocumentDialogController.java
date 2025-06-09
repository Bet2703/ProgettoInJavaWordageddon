package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;
import java.io.File;
import service.DocumentsManagement;
import service.Levels;

/**
 * Controller class for managing the load document dialog.
 *
 * This class handles the interactions and logic of the document loading
 * dialog, including file selection, difficulty level selection, and actions
 * associated with confirming or canceling the dialog. It also integrates with
 * the user interface components defined in the FXML file to provide functionality
 * for the dialog.
 *
 * @author Gruppo6
 */
public class LoadDocumentDialogController {

    /**
     * Label component used to display the name of the file selected by the user
     * in the file selection dialog.
     *
     * When a file is successfully chosen, its name is displayed as the text of
     * this label to provide feedback to the user.
     *
     * This label is part of the UI elements of the document loading dialog and is
     * updated dynamically when the user selects a file.
     */
    @FXML
    private Label fileNameLabel;

    /**
     * Represents the difficulty level radio button in the user interface.
     *
     * This radio button allows users to select the difficulty level when uploading
     * a document to the database. It is part of a toggle group with `easyRadio`, `mediumRadio`
     * and `hardRadio`, ensuring that only one difficulty level can be selected at a time.
     *
     * The selected difficulty level is used to categorize the uploaded document
     * appropriately. This field is initialized via dependency injection from the
     * FXML file associated with the controller.
     */
    @FXML
    private RadioButton easyRadio, mediumRadio, hardRadio;

    /**
     * Represents the file selected by the user in the file selection dialog.
     *
     * This variable stores a reference to the selected file and is used
     * in later operations such as displaying the file name in the UI
     * (via the `fileNameLabel`) or passing it for processing when the user
     * confirms their selection.
     *
     * If no file is selected, this variable remains null.
     */
    private File selectedFile;

    /**
     * Represents the stage used for displaying dialog windows in the LoadDocumentDialogController.
     * This variable holds a reference to the JavaFX Stage instance that serves as a modal dialog,
     * allowing the user to interact with the dialog independently of the main application window.
     * It is used to configure and control the behavior of the dialog, such as its modality, scene, and visibility.
     */
    private Stage dialogStage;

    /**
     * Represents a group of toggleable nodes ensuring only one of them can be selected at a time.
     * Typically used with RadioButtons for managing exclusive selection behavior.
     */
    private ToggleGroup group;

    /**
     * Initializes the controller by setting up the toggle group for difficulty level radio buttons.
     *
     * This method is automatically invoked after the FXML file has been loaded.
     * It groups the `easyRadio`, `mediumRadio`, and `hardRadio` buttons into a single `ToggleGroup`,
     * ensuring only one option can be selected at a time.
     */
    @FXML
    public void initialize() {
        group = new ToggleGroup();
        easyRadio.setToggleGroup(group);
        mediumRadio.setToggleGroup(group);
        hardRadio.setToggleGroup(group);
    }

    /**
     * Sets the dialog stage for this controller.
     * This stage is used to display the dialog managed by this controller.
     *
     * @param stage the stage to be set as the dialog stage for this controller
     */
    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    /**
     * Displays a file chooser dialog to allow the user to select a text file and updates the file name label accordingly.
     *
     * This method opens a modal file chooser dialog, filtered to display only `.txt` files (UTF-8).
     * Once a file is selected, it assigns the selected file to the `selectedFile` field and updates
     * the `fileNameLabel` with the name of the chosen file. If no file is selected, no action is taken.
     *
     * The method also ensures that the user interface reflects the current state
     * of the selected file by updating the appropriate label.
     */
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

    /**
     * Handles the confirmation action triggered by the "Confirm" button.
     *
     * This method validates that both a file and a difficulty level are selected.
     * If both are provided, it delegates to the `DocumentsManagement.loadToDB` method
     * to load the selected file and difficulty into the database. It then displays a
     * success alert to inform the user that the document has been successfully uploaded
     * and closes the dialog stage.
     *
     * If either the file or difficulty level is not selected, it displays a warning
     * alert to prompt the user to make the necessary selections.
     */
    @FXML
    private void onConfirm() {
        if (selectedFile != null && getSelectedDifficulty() != null) {
            DocumentsManagement.loadToDB(selectedFile, getSelectedDifficulty());
            new Alert(Alert.AlertType.INFORMATION, "Documento caricato con successo!").showAndWait();
            dialogStage.close();
        } else {
            new Alert(Alert.AlertType.WARNING, "Seleziona file e difficoltà.").showAndWait();
        }
    }

    /**
     * Closes the dialog stage without performing any additional actions.
     *
     * This method is primarily intended to handle cancel operations, allowing the user
     * to close the dialog without making any changes or confirming any actions. It
     * is typically invoked when the "Cancel" button in the associated UI is clicked*/
    @FXML
    private void onCancel() {
        dialogStage.close();
    }

    /**
     * Determines the selected difficulty level based on the state of the radio buttons.
     *
     * This method checks whether any of the radio buttons (`easyRadio`, `mediumRadio`, or `hardRadio`) is selected
     * and returns the corresponding difficulty level. If none of the buttons are selected, it returns null.
     *
     * @return the selected difficulty level as a {@code Levels.Difficulty} enum value,
     *         or {@code null} if no option is selected
     */
    private Levels.Difficulty getSelectedDifficulty() {
        if (easyRadio.isSelected()) return Levels.Difficulty.EASY;
        if (mediumRadio.isSelected()) return Levels.Difficulty.MEDIUM;
        if (hardRadio.isSelected()) return Levels.Difficulty.HARD;
        return null;
    }
}
