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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.DocumentsManagement;
import service.Levels;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Gruppo6
 */
public class AdminController {

    @FXML
    private Button btnLoadDocs;
    @FXML
    private ListView<String> documentsList;
    @FXML
    private Label messageLabel;
    @FXML
    private TextField selectedFileField;
    @FXML
    private RadioButton easyRadio;
    @FXML
    private RadioButton mediumRadio;
    @FXML
    private RadioButton hardRadio;

    private ToggleGroup difficultyGroup;

    @FXML
    public void initialize() {

        //Popola la lista dei documenti caricati
        loadDocumentTitles();

    }

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

            // Dopo la chiusura, aggiorna la lista dei documenti
            loadDocumentTitles();

        } catch (IOException e) {
            System.err.println("Errore durante il caricamento della schermata: " + e.getMessage());
        }
    }

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

        // Dopo la cancellazione, aggiorna la lista dei documenti
        loadDocumentTitles();
    }

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


    public void loadDocumentTitles() {
        List<String> titles = DocumentsManagement.getAllDocumentTitles();

        documentsList.getItems().clear();
        documentsList.getItems().addAll(titles);
    }

}


