package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * La classe ResultsController gestisce la logica di interazione e comportamento 
 * della vista dei risultati dell'applicazione. Si occupa di aggiornare l'interfaccia
 * utente in base alle risposte corrette fornite e permette la navigazione al menu principale.
 *
 * @author Gruppo6
 */
public class ResultsController {

    /**
     * Etichetta che mostra il numero di risposte corrette ottenute dall'utente.
     * Questo componente UI viene aggiornato dinamicamente in base alle prestazioni dell'utente.
     */
    @FXML
    private Label correctCountLabel;

    /**
     * Etichetta utilizzata per visualizzare messaggi di feedback all'utente.
     * Viene aggiornata dinamicamente per fornire informazioni pertinenti
     * basate sull'interazione o sullo stato dell'applicazione.
     */
    @FXML
    private Label feedbackMessage;

    /**
     * Numero di risposte corrette fornite dall'utente durante una sessione.
     * Questo valore viene utilizzato per aggiornare dinamicamente i risultati visualizzati.
     */
    private int correctAnswers;

    /**
     * Numero totale di risposte fornite dall'utente in un quiz o valutazione.
     * Utile per calcolare metriche come accuratezza o completamento del quiz.
     */
    private int totalAnswers;

    /**
     * Pulsante che permette il ritorno alla schermata principale ("Home").
     * Questo pulsante viene iniettato tramite FXMLLoader e associato all'elemento UI definito nel file FXML.
     */
    @FXML
    private Button btnHomeTop;

    /**
     * Pulsante per tornare al menu principale dell'applicazione dalla schermata dei risultati.
     */
    @FXML
    private Button btnHome;

    /**
     * Pulsante che consente all'utente di riprovare il quiz.
     * Al clic, ricarica la schermata del quiz permettendo di ripeterlo per migliorare la performance.
     */
    @FXML
    private Button btnRetry;

    /**
     * Pulsante per accedere alla schermata dei punteggi personali dell'utente.
     * Permette la visualizzazione delle statistiche individuali delle sessioni di quiz.
     */
    @FXML
    private Button btnPersonalScores;

    /**
     * Metodo di inizializzazione chiamato automaticamente dopo il caricamento del file FXML.
     * Configura l'etichetta dei risultati visualizzando il numero iniziale di risposte corrette.
     */
    private void initialize() {
        // Aggiorna l'etichetta con il conteggio iniziale delle risposte corrette
        correctCountLabel.setText("Risposte corrette: " + correctAnswers + " su " + totalAnswers + " domande totali.");
    }

    /**
     * Metodo per aggiornare il numero di risposte corrette e riflettere il nuovo valore nell'interfaccia utente.
     *
     * @param correctAnswers Numero di risposte corrette da aggiornare e visualizzare.
     */
    public void setCorrectAnswers(int correctAnswers) {
        // Memorizza il nuovo valore delle risposte corrette
        this.correctAnswers = correctAnswers;
        
        // Se l'etichetta è già stata inizializzata, aggiorna il testo
        if (correctCountLabel != null) {
            correctCountLabel.setText("Risposte corrette: " + correctAnswers + " su " + totalAnswers + " domande totali.");
        }
    }

    /**
     * Metodo per aggiornare il numero totale di risposte e modificare l'etichetta UI di conseguenza.
     *
     * @param totalAnswers Numero totale di risposte da impostare e mostrare.
     */
    public void setTotalAnswers(int totalAnswers) {
        // Memorizza il nuovo valore del totale delle risposte
        this.totalAnswers = totalAnswers;
        
        // Se l'etichetta è già stata inizializzata, aggiorna il testo
        if (correctCountLabel != null) {
            correctCountLabel.setText("Risposte corrette: " + correctAnswers + " su " + totalAnswers + " domande totali.");
        }
    }

    /**
     * Gestisce l'azione di ripetere il quiz. Carica la vista del quiz e inizializza la sessione corrente.
     *
     * @param event Evento scatenato dall'interazione con il pulsante "Ripeti Quiz".
     */
    @FXML
    private void onRetryQuiz(ActionEvent event) {
        try {
            // Carica la vista del quiz
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/quizView.fxml"));
            Parent root = loader.load();

            // Ottiene il controller e avvia una nuova sessione di gioco
            QuestionsController questionsController = loader.getController();
            questionsController.startGame(management.GameSessionManagement.getInstance().getDocumentId());

            // Mostra la nuova scena
            Stage stage = (Stage) feedbackMessage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            // Gestione degli errori durante il caricamento
            System.err.println("Errore durante il caricamento della schermata del quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gestisce l'azione di visualizzare la schermata dei punteggi personali.
     * Carica la vista PersonalScoresView.fxml e la imposta come scena attiva.
     *
     * @param event Evento scatenato dall'interazione con il pulsante "Punteggi Personali".
     */
    @FXML
    private void onPersonalScores(ActionEvent event) {
        // Utilizza il metodo generico per caricare la vista
        loadView(event, "/view/PersonalScoresView.fxml");
    }

    /**
     * Gestisce l'azione di tornare al menu principale. Carica la vista del menu e la imposta come scena attiva.
     *
     * @param event Evento scatenato dall'interazione con il pulsante "Torna al Menu".
     */
    @FXML
    private void onBackToMenu(ActionEvent event) {
        // Utilizza il metodo generico per caricare la vista del menu
        loadView(event, "/view/MainMenu.fxml");
    }

    /**
     * Metodo generico per caricare una nuova vista nell'applicazione.
     * Imposta il file FXML specificato come scena attuale.
     *
     * @param event Evento associato all'azione di cambio schermata.
     * @param fxmlPath Percorso del file FXML da caricare.
     */
    private void loadView(ActionEvent event, String fxmlPath) {
        try {
            // Carica la vista FXML specificata
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Ottiene lo stage corrente e imposta la nuova scena
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            // Gestione degli errori durante il caricamento
            System.err.println("Errore durante il caricamento della schermata: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
