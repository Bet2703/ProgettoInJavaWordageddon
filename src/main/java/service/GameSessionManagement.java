package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import users.Player;

public class GameSessionManagement {

    /**
     * Singleton pattern implementation.
     */
    private static GameSessionManagement instance;

    /**
     * Implementation of the current player.
     */
    private Player currentPlayer;

    /**
     * Implementation of the document ID.
     */
    private int documentId;

    /**
     * Implementation of the score.
     */
    private int score;

    /**
     * Implementation of the number of questions answered.
     */
    private int questionsAnswered;

    /**
     * Implementation of the number of correct answers.
     */
    private int correctAnswers;

    /**
     * Implementation of the timestamp.
     */
    private LocalDateTime timestamp;

    /**
     * Implementation of the difficulty.
     */
    private String difficulty;


    /**
     * Private constructor for singleton pattern implementation.
     */
    private GameSessionManagement() {
        // costruttore privato per singleton
    }

    /**
     * Method that returns the instance of the singleton class.
     *
     * @return the instance of the singleton class; if the instance is not yet created, it is created and returned.
     */
    public static GameSessionManagement getInstance() {
        if (instance == null) {
            instance = new GameSessionManagement();
        }
        return instance;
    }

    /**
     * Method that initializes the session for the given player and document ID.
     *
     * @param player the player who is starting the session; must not be {@code null}
     * @param documentId the ID of the document for which the session is being started; must be greater than zero and must exist in the database.
     * @param difficulty the difficulty of the session; must not be {@code null} or empty.
     */
    public void startSession(Player player, int documentId, String difficulty) {
        this.currentPlayer = player;
        this.documentId = documentId;
        this.score = 0;
        this.questionsAnswered = 0;
        this.correctAnswers = 0;
        this.timestamp = LocalDateTime.now();
        this.difficulty = difficulty;
    }

    /**
     * Method that records the answer of the current player.
     *
     * @param correct {@code true} if the answer was correct; {@code false} otherwise, e.g. if the answer was wrong.
     */
    public void recordAnswer(boolean correct) {
        questionsAnswered++;
        if (correct) {
            correctAnswers++;
            score += 10; // puoi personalizzare il punteggio
        }
    }

    /**
     * Method that saves the session data to the database.
     */
    public void saveSession() {
        String sql = "INSERT INTO sessions (username, score, timestamp, difficulty, document_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, this.getUsername());
            pstmt.setInt(2, this.getScore());
            pstmt.setString(3, this.getTimestamp().toString());
            pstmt.setString(4, this.getDifficulty());
            pstmt.setInt(5, this.getDocumentId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore durante il salvataggio della sessione: " + e.getMessage());
        }
    }

    /**
     * Method that retrieves the session data from the database.
     *
     * @param username the username of the player whose session is to be retrieved; must not be {@code null} or empty
     */
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

    /**
     * Method that resets the session data.
     */
    public void resetSession() {
        instance = null; // oppure azzera i campi
    }

    /**
     * Method that returns the current player.
     *
     * @return the current player; {@code null} if no player is currently logged in.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Method that returns the document ID.
     * @return the document ID; {@code -1} if no player is currently logged in.
     */
    public int getDocumentId() {
        return documentId;
    }

    /**
     * Method that returns the score.
     *
     * @return the score; {@code -1} if no player is currently logged in.
     */
    public int getScore() {
        return score;
    }

    /**
     * Method that returns the number of questions answered.
     *
     * @return the number of questions answered; {@code -1} if no player is currently logged in.
     */
    public int getQuestionsAnswered() {
        return questionsAnswered;
    }
    /**
     * Method that returns the number of correct answers.
     *
     * @return the number of correct answers; {@code -1} if no player is currently logged in.
     */
    public int getCorrectAnswers() {
        return correctAnswers;
    }

    /**
     * Method that returns the percentage of correct answers.
     *
     * @return the percentage of correct answers; {@code -1} if no player is currently logged in.
     */
    public double getCorrectPercentage() {
        if (questionsAnswered == 0) {
            return 0;
        }
        return (double) correctAnswers / questionsAnswered * 100;
    }

    /**
     * Method that returns the timestamp.
     *
     * @return the timestamp; {@code null} if no player is currently logged in.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Method that returns the username of the current player.
     *
     * @return the username of the current player; {@code null} if no player is currently logged in.
     */
    public String getUsername() {
        return currentPlayer != null ? currentPlayer.getUsername() : null;
    }


    /**
     * Method that returns the difficulty of the current session.
     *
     * @return the difficulty of the current session; {@code null} if no player is currently logged in.
     */
    public String getDifficultyAsString() {
        return getDifficulty().toString();
    }

    /**
     * Method that returns the difficulty of the current session.
     *
     * @return the difficulty of the current session; {@code null} if no player is currently logged in.
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Method that returns the maximum number of questions for the given difficulty.
     *
     * @return the maximum number of questions for the given difficulty; {@code 10} if no player is currently logged in.
     */
    public int getMaxQuestions() {
        switch (difficulty.toUpperCase()) {
            case "EASY": return 5;
            case "MEDIUM": return 10;
            case "HARD": return 15;
            default: return 10;
        }
    }

    /**
     * Method that returns the number of remaining questions for the current session.
     *
     * @return the number of remaining questions for the current session; {@code -1} if no player is currently logged in.
     */
    public int getRemainingQuestions() {
        return getMaxQuestions() - getQuestionsAnswered();
    }

    /**
     * Method that returns a string representation of the current session.
     *
     * @return a string representation of the current session; {@code null} if no player is currently logged in.
     */
    @Override
    public String toString() {
        return "La sessione del giocatore " + getCurrentPlayer() + " col punteggio " + getCorrectAnswers() + " a difficoltà " + getDifficulty() + " ha avuto " + getQuestionsAnswered() + " domande risposte.";
    }
}
