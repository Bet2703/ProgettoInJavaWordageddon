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
import model.Levels;
import management.DatabaseManagement;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

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
 * Autore: Gruppo6
 */
public class DocumentReadController {

    // Interfaccia utente

    /** Area testuale per la lettura del documento */
    @FXML
    private TextArea documentTextArea;

    /** Etichetta che mostra il tempo rimanente */
    @FXML
    private Label timerLabel;

    // Variabili interne

    /** ID del documento selezionato dal DB */
    private int documentId;

    /** Oggetto per il countdown */
    private Timeline timeline;

    /** Secondi rimanenti per la lettura (default 30, modificato in base alla difficoltà) */
    private int secondsLeft = 30;

    /** Difficoltà attuale del documento ("EASY", "MEDIUM", "HARD") */
    private String difficulty;

    /**
     * Metodo chiamato all'inizializzazione del controller (automaticamente da JavaFX).
     * 1. Recupera un documento casuale in base alla difficoltà selezionata.
     * 2. Mostra il contenuto nella TextArea.
     * 3. Avvia il timer countdown.
     */
    @FXML
    public void initialize() {
        String text = fetchRandomDocumentByDifficulty();
        documentTextArea.setText(text); // Popola l'area di testo con il documento
        startTimer();                   // Avvia il conto alla rovescia
    }

    /**
     * Avvia un timer che conta i secondi rimanenti, aggiornando la label ogni secondo.
     * Quando il tempo scade, passa automaticamente alla fase di quiz.
     */
    private void startTimer() {
        timerLabel.setText("Tempo restante: " + secondsLeft + "s");

        // Crea una Timeline che ogni 1s aggiorna la label
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsLeft--; // Decrementa il tempo
            timerLabel.setText("Tempo restante: " + secondsLeft + "s");

            if (secondsLeft <= 0) {
                // Tempo finito
                timeline.stop(); // Ferma la Timeline
                timerLabel.setText("Tempo scaduto!");
                goToQuestionsView(); // Avvia la schermata successiva
            }
        }));

        timeline.setCycleCount(secondsLeft); // Numero di esecuzioni = tempo iniziale
        timeline.play(); // Avvia il countdown
    }

    /**
     * Passa alla schermata di quiz dopo la lettura del documento.
     * 1. Carica la schermata quiz.
     * 2. Passa l'ID del documento al controller successivo.
     * 3. Avvia la generazione delle domande.
     * 4. Chiude la schermata di lettura.
     */
    private void goToQuestionsView() {
        try {
            // Carica la schermata quiz da FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/quizView.fxml"));
            Parent root = loader.load();

            // Ottiene lo stage corrente
            Stage stage = (Stage) documentTextArea.getScene().getWindow();

            // Imposta la nuova scena
            stage.setScene(new Scene(root));
            stage.show();

            // Inizializza il controller
            QuestionsController controller = loader.getController();

            // Avvia il quiz passando l'ID
            controller.startGame(documentId);

            // Chiude la finestra attuale (opzionale)
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
     * Recupera un documento casuale dal database, filtrando per difficoltà.
     * Se trovato:
     * - imposta l'ID, la difficoltà
     * - aggiorna il tempo di lettura in base alla difficoltà
     * 
     * @return testo del documento oppure stringa vuota in caso di errore
     */
    private String fetchRandomDocumentByDifficulty() {
        String query = "SELECT id, text, difficulty FROM documents WHERE difficulty = ? ORDER BY RANDOM() LIMIT 1";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Ottiene la difficoltà selezionata dal controller precedente
            pstmt.setString(1, LevelsController.getDifficulty());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    documentId = rs.getInt("id"); // salva ID del documento
                    difficulty = rs.getString("difficulty"); // imposta la difficoltà
                    secondsLeft = Levels.getSecondsByDifficulty(difficulty.toUpperCase()); // imposta tempo
                    return rs.getString("text"); // ritorna il testo
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore nella connessione/query col database: " + e.getMessage());
            e.printStackTrace();
        }

        return ""; // Nessun documento trovato o errore
    }
}
