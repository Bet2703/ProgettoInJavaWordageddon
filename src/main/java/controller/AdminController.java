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
 * Controller per la gestione delle funzionalità amministrative dell'applicazione.
 * Si occupa della gestione dei documenti (caricamento, eliminazione) e della navigazione tra schermate.
 * Interagisce con i componenti dell'interfaccia grafica definiti nel file FXML associato.
 */
public class AdminController {

    /**
     * ListView che mostra l'elenco dei titoli dei documenti disponibili.
     * Viene popolata dinamicamente con i titoli dei documenti presenti nel sistema.
     */
    @FXML
    private ListView<String> documentsList;

    /**
     * Etichetta per la visualizzazione di messaggi all'utente.
     * Utilizzata per mostrare informazioni, avvisi o messaggi di errore.
     */
    @FXML
    private Label messageLabel;

    /**
     * Metodo di inizializzazione del controller.
     * Viene chiamato automaticamente dopo il caricamento del file FXML associato.
     * Si occupa di caricare l'elenco dei titoli dei documenti all'avvio.
     */
    @FXML
    public void initialize() {
        loadDocumentTitles();
    }

    /**
     * Gestisce l'azione di caricamento di un nuovo documento.
     * Apre una finestra di dialogo modale per la selezione del file e l'impostazione
     * del livello di difficoltà. Al termine dell'operazione, aggiorna la lista dei documenti.
     * 
     * @throws IOException se si verifica un errore durante il caricamento della finestra di dialogo
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

            loader.<LoadDocumentDialogController>getController().setDialogStage(dialogStage);
            dialogStage.showAndWait();

            loadDocumentTitles();
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento della schermata: " + e.getMessage());
        }
    }

    /**
     * Gestisce l'eliminazione di un documento selezionato.
     * Mostra una finestra di conferma prima di procedere all'eliminazione.
     * Se l'utente conferma, rimuove il documento dal database e aggiorna la lista.
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
     * Gestisce il ritorno alla schermata di login.
     * Carica la schermata di login e sostituisce la scena corrente.
     * 
     * @param event l'evento di azione che ha scatenato questo metodo
     */
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
     * Carica l'elenco dei titoli dei documenti presenti nel sistema.
     * Pulisce la lista corrente e la riempie con i titoli recuperati dal database,
     * applicando filtri per rimuovere duplicati e valori nulli, e ordinando alfabeticamente.
     */
    public void loadDocumentTitles() {
        List<String> titles = DocumentsManagement.getAllDocumentTitles();

        documentsList.getItems().clear();

        titles.stream()
                .filter(title -> title != null)
                .distinct()
                .sorted()
                .forEach(documentsList.getItems()::add);
    }
}