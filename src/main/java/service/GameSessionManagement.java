package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import users.Player;

public class GameSessionManagement {

    private static GameSessionManagement instance;

    private Player currentPlayer;
    private int documentId;
    private int score;
    private int questionsAnswered;
    private int correctAnswers;
    private LocalDateTime timestamp;
    private String difficulty;


    private GameSessionManagement() {
        // costruttore privato per singleton
    }

    public static GameSessionManagement getInstance() {
        if (instance == null) {
            instance = new GameSessionManagement();
        }
        return instance;
    }

    public void startSession(Player player, int documentId, String difficulty) {
        this.currentPlayer = player;
        this.documentId = documentId;
        this.score = 0;
        this.questionsAnswered = 0;
        this.correctAnswers = 0;
        this.timestamp = LocalDateTime.now();
        this.difficulty = difficulty;
    }

    public void recordAnswer(boolean correct) {
        questionsAnswered++;
        if (correct) {
            correctAnswers++;
            score += 10; // puoi personalizzare il punteggio
        }
    }

    public void saveSession() {
        String sql = "INSERT INTO sessions (username, score, timestamp) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, this.getUsername());
            pstmt.setInt(2, this.getScore());
            pstmt.setString(3, this.getTimestamp().toString());

            int rowsInserted = pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore durante il salvataggio della sessione: " + e.getMessage());
        }
    }

    public void getSession(String username) {
        String sql = "SELECT * FROM sessions WHERE username = ?";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Punteggio: " + rs.getInt("score"));
                System.out.println("Data/Ora: " + rs.getString("timestamp"));
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero della sessione: " + e.getMessage());
        }
    }

    public void resetSession() {
        instance = null; // oppure azzera i campi
    }

    // Getters
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getDocumentId() {
        return documentId;
    }

    public int getScore() {
        return score;
    }

    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return currentPlayer != null ? currentPlayer.getUsername() : null;
    }


    public String getDifficulty() {
        return difficulty;
    }

    public int getMaxQuestions() {
        switch (difficulty.toUpperCase()) {
            case "EASY": return 5;
            case "MEDIUM": return 10;
            case "HARD": return 15;
            default: return 10;
        }
    }

    @Override
    public String toString() {
        return "La sessione del giocatore " + getCurrentPlayer() + " col punteggio " + getCorrectAnswers() + " a difficoltà " + getDifficulty() + " ha avuto " + getQuestionsAnswered() + " domande risposte.";
    }
}
