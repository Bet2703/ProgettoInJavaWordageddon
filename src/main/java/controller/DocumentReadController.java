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

public class DocumentReadController {

    @FXML
    private TextArea documentTextArea;
    @FXML
    private Label timerLabel;

    private int documentId;
    private Timeline timeline;
    private int secondsLeft = 30; // Imposta il tempo limite di lettura

    @FXML
    public void initialize() {
        String text = fetchRandomDocumentByDifficulty("EASY");
        documentTextArea.setText(text);

        startTimer();
    }

    private void startTimer() {
        timerLabel.setText("Tempo restante: " + secondsLeft + "s");
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsLeft--;
            timerLabel.setText("Tempo restante: " + secondsLeft + "s");

            if (secondsLeft <= 0) {
                timeline.stop();
                timerLabel.setText("Tempo scaduto!");
                // Passa alla schermata successiva (domande)
                goToQuestionsView();
            }
        }));
        timeline.setCycleCount(secondsLeft);
        timeline.play();
    }

    private void goToQuestionsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/quizView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Domande");
            stage.setScene(new Scene(root));
            stage.show();

            QuestionsController controller = loader.getController();
            controller.setDocumentId(documentId);

            // Chiudi la finestra attuale (quella di lettura documento)
            Stage currentStage = (Stage) documentTextArea.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            System.err.println("Errore nell'apertura della scena " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String fetchRandomDocumentByDifficulty(String difficulty) {
        String content = "";
        String query = "SELECT text FROM documents WHERE difficulty = ? ORDER BY RANDOM() LIMIT 1";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, difficulty);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                content = rs.getString("text");
                System.out.println("Documento letto: " + content);
            }
        } catch (SQLException e) {
            System.err.println("Errore nella connessione/query col database " + e.getMessage());
            e.printStackTrace();
        }
        return content;
    }
}
