package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.DatabaseManagement;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Controller per la gestione della lettura dei documenti nell'applicazione.
 * Si occupa della visualizzazione del testo, della gestione del timer e della
 * transizione alla fase di quiz successiva.
 * 
 * <p>Principali funzionalità:
 * <ul>
 *   <li>Visualizzazione del contenuto dei documenti</li>
 *   <li>Gestione del timer di lettura</li>
 *   <li>Transizione alla schermata delle domande</li>
 *   <li>Recupero casuale di documenti in base alla difficoltà</li>
 * </ul>
 * </p>
 *
 * @author Gruppo6
 */
public class DocumentReadController {

    /**
     * Area di testo per la visualizzazione del contenuto del documento.
     * Viene popolata automaticamente con il testo del documento selezionato.
     */
    @FXML
    private TextArea documentTextArea;

    /**
     * Etichetta per la visualizzazione del timer di lettura.
     * Mostra il tempo rimanente in secondi in formato "Tempo restante: Xs".
     */
    @FXML
    private Label timerLabel;

    /**
     * ID univoco del documento corrente.
     * Viene utilizzato per identificare il documento nel database e per
     * recuperare le domande associate durante la fase di quiz.
     */
    private int documentId;

    /**
     * Timeline per la gestione del countdown.
     * Viene utilizzata per aggiornare il timer ogni secondo.
     */
    private Timeline timeline;

    /**
     * Tempo rimanente in secondi per la lettura del documento.
     * Il valore iniziale viene determinato in base alla difficoltà del documento.
     */
    private int secondsLeft = 30;

    /**
     * Livello di difficoltà del documento corrente.
     * Può assumere i valori "EASY", "MEDIUM" o "HARD".
     */
    private String difficulty;

    /**
     * Metodo di inizializzazione del controller.
     * Viene chiamato automaticamente dopo il caricamento del file FXML associato.
     * 
     * <p>Si occupa di:
     * <ol>
     *   <li>Recuperare un documento casuale in base alla difficoltà</li>
     *   <li>Visualizzare il testo nell'area dedicata</li>
     *   <li>Avviare il timer di lettura</li>
     * </ol>
     * </p>
     */
    @FXML
    public void initialize(){
        String text = fetchRandomDocumentByDifficulty();
        documentTextArea.setText(text);
        startTimer();
    }

    /**
     * Avvia il countdown per la lettura del documento.
     * 
     * <p>Il timer:
     * <ul>
     *   <li>Viene aggiornato ogni secondo</li>
     *   <li>Mostra il tempo rimanente nell'etichetta dedicata</li>
     *   <li>Al termine del tempo, avvia automaticamente la fase di quiz</li>
     * </ul>
     * </p>
     */
    private void startTimer() {
        timerLabel.setText("Tempo restante: " + secondsLeft + "s");
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsLeft--;
            timerLabel.setText("Tempo restante: " + secondsLeft + "s");

            if (secondsLeft <= 0) {
                timeline.stop();
                timerLabel.setText("Tempo scaduto!");
                goToQuestionsView();
            }
        }));
        timeline.setCycleCount(secondsLeft);
        timeline.play();
    }

    /**
     * Gestisce il passaggio alla schermata delle domande.
     * 
     * <p>Questa operazione:
     * <ol>
     *   <li>Carica la schermata delle domande</li>
     *   <li>Passa l'ID del documento al controller delle domande</li>
     *   <li>Avvia il quiz</li>
     *   <li>Chiude la schermata corrente</li>
     * </ol>
     * </p>
     * 
     * <p>In caso di errore durante il caricamento della schermata,
     * viene stampato lo stack trace dell'errore.</p>
     */
    private void goToQuestionsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/quizView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) documentTextArea.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();

            QuestionsController controller = loader.getController();
            Consumer<Integer> startGame = controller::startGame;
            controller.setDocumentId(documentId);
            startGame.accept(documentId);

            Optional.ofNullable(documentTextArea.getScene())
                    .map(Scene::getWindow)
                    .map(Stage.class::cast)
                    .ifPresent(Stage::close);

        } catch (IOException e) {
            System.err.println("Errore nell'apertura della scena: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Recupera un documento casuale dal database in base alla difficoltà.
     * 
     * <p>La query SQL:
     * <ul>
     *   <li>Seleziona un documento random con la difficoltà specificata</li>
     *   <li>Imposta l'ID del documento, la difficoltà e il tempo disponibile</li>
     *   <li>Restituisce il testo del documento</li>
     * </ul>
     * </p>
     * 
     * @return Il testo del documento selezionato, oppure una stringa vuota se non trovato
     * @throws SQLException in caso di errori di connessione al database
     */
    private String fetchRandomDocumentByDifficulty() {
        String query = "SELECT id, text, difficulty FROM documents WHERE difficulty = ? ORDER BY RANDOM() LIMIT 1";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, LevelsController.getDifficulty());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    documentId = rs.getInt("id");
                    difficulty = rs.getString("difficulty");
                    secondsLeft = service.Levels.getSecondsByDifficulty(difficulty.toUpperCase());
                    return rs.getString("text");
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore nella connessione/query col database: " + e.getMessage());
            e.printStackTrace();
        }

        return "";
    }
}