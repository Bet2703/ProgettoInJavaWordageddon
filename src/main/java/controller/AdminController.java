/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.DocumentsManagement;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * The AdminController class handles the administration functionalities of the application.
 * It provides functionality to load, delete, and manage document metadata,
 * as well as the ability to navigate back to the login screen.
 * This controller is responsible for interacting with the associated FXML view components.
 *
 * @author Gruppo6
 */
public class AdminController {


    /**
     * A ListView component used to display a list of document titles in the AdminController.
     * This control allows users to view and select documents from the list.
     * It is populated and managed programmatically through controller methods.
     */
    @FXML
    private ListView<String> documentsList;

    /**
     * Label used to display messages to the user in the AdminController context.
     * This can include notifications, error messages, or other information
     * relevant to the actions performed within the Admin interface.
     */
    @FXML
    private Label messageLabel;

    /**
     * Initializes the AdminController.
     *
     * This method is automatically called after the associated FXML file has been loaded.
     * It sets up the initial state of the controller by populating the list of document titles
     * in the `documentsList`. The `loadDocumentTitles` method is invoked to fetch and display
     * the current list of available documents.
     */
    @FXML
    public void initialize() {

        loadDocumentTitles();

    }

    /**
     * Handles the action of loading documents when triggered.
     * This method opens a dialog window for document upload,
     * provides tools for file selection and difficulty level assignment,
     * and processes the user input upon confirmation.
     *
     * The method loads the "LoadDocumentView.fxml" file to render the dialog
     * and initializes a new stage with modality set to `APPLICATION_MODAL`.
     * The `LoadDocumentDialogController` is used in the dialog to manage user interactions.
     *
     * Upon closing the dialog, it updates the list of document titles.
     * Any file input/output errors during the loading of the dialog are caught and logged.
     */
    @FXML
    private void onLoadDocuments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoadDocumentView.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Carica Documento");
            dialogStage.setScene(new Scene(root));
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            LoadDocumentDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            loadDocumentTitles();

        } catch (IOException e) {
            System.err.println("Errore durante il caricamento della schermata: " + e.getMessage());
        }
    }

    /**
     * Handles the action of deleting a selected document.
     *
     * This method checks if a document is selected from the `documentsList`. If no document is selected,
     * it updates the `messageLabel` with a prompt requesting the user to select a document.
     * Otherwise, it prompts the user with a confirmation dialog.
     * If the user confirms the deletion, the selected document is removed from the database using the
     * `DocumentsManagement.deleteFromDB` method.
     *
     * After a successful deletion, the method updates the displayed list of documents by invoking
     * the `loadDocumentTitles` method.
     */
    @FXML
    private void onDeleteDocument() {
        String selectedTitle = documentsList.getSelectionModel().getSelectedItem();

        if (selectedTitle == null) {
            messageLabel.setText("Seleziona un documento da cancellare.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma cancellazione");
        alert.setHeaderText(null);
        alert.setContentText("Sei sicuro di voler cancellare \"" + selectedTitle + "\"?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            DocumentsManagement.deleteFromDB(selectedTitle);
        }

        loadDocumentTitles();
    }

    /**
     * Handles the action of navigating back to the login screen.
     * This method is triggered by the associated user action, such as clicking a button.
     * It loads the "Login.fxml" file, sets up the new scene, and transitions to the login view.
     * If an error occurs during the loading process, an error message is displayed on the message label.
     *
     * @param event the ActionEvent*/
    @FXML
    private void onBackToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            messageLabel.setText("Errore nel caricamento della schermata di login.");
        }
    }

    /**
     * Loads the titles of all documents into the `documentsList` observable list.
     *
     * This method retrieves a list of document titles from the `DocumentsManagement` class.
     * It then clears any existing items in the `documentsList` and populates it with the newly
     * retrieved list of document titles.
     *
     * Designed to ensure the `documentsList` accurately reflects the current state of documents
     * managed by the system.
     */
    public void loadDocumentTitles() {
        List<String> titles = DocumentsManagement.getAllDocumentTitles();

        documentsList.getItems().clear();
        documentsList.getItems().addAll(titles);
    }

}


