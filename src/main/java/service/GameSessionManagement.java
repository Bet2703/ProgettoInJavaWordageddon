package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import users.Player;

/**
 * The GameSessionManagement class provides a mechanism to manage game sessions for a player.
 * It ensures that only one session is active at a time using the singleton pattern. The class
 * handles the initialization, tracking, and storage of game session data, including player
 * information, session metrics, and difficulty level.
 *
 * @author Gruppo6
 */
public class GameSessionManagement {

    /**
     * Singleton instance of the GameSessionManagement class.
     * This variable holds the single instance of the class to ensure
     * that only one instance is created and shared across the application.
     */
    private static GameSessionManagement instance;

    /**
     * Represents the player currently logged into the game session.
     * This variable holds the reference to the Player object that is
     * actively participating in the session. It is updated when a new
     * session starts for a different player.
     *
     * This field can be null if no player is currently logged into the
     * session. It is used in various methods to retrieve information
     * about the active player, such as their username, score, or answers.
     */
    private Player currentPlayer;

    /**
     * Represents the unique identifier for a document associated with the game session.
     * This variable is used to track which document the current session is tied to.
     */
    private int documentId;

    /**
     * Represents the score for the current game session.
     * This field is updated based on the player's performance during the session.
     */
    private int score;

    /**
     * Represents the number of questions answered by the current player during a game session.
     * This field is used to track the progress of the session and is updated when a new question
     * is answered, regardless of whether the answer is correct or incorrect.
     */
    private int questionsAnswered;

    /**
     * Represents the number of correct answers recorded during a game session.
     *
     * This variable is used to track the success rate of the player by counting
     * the total number of correctly answered questions. It is incremented
     * whenever a correct answer is recorded and provides insights into the
     * player's performance.
     */
    private int correctAnswers;

    /**
     * Represents the timestamp of the game session.
     * This variable stores the date and time when the session was created or updated.
     */
    private LocalDateTime timestamp;

    /**
     * Represents the difficulty level of a game session.
     *
     * This variable specifies the difficulty of the current session and can influence
     * various aspects of the session, such*/
    private String difficulty;


    /**
     * Private constructor for the GameSessionManagement class.
     * This constructor is used to ensure that the class operates as a singleton,
     * preventing the instantiation of multiple instances of the class from outside.
     */
    private GameSessionManagement() {
        // costruttore privato per singleton
    }

    /**
     * Returns the singleton instance of the GameSessionManagement class.
     * This method ensures that only one instance of the class exists throughout the application.
     *
     * @return the singleton instance of the GameSessionManagement class.
     */
    public static GameSessionManagement getInstance() {
        if (instance == null) {
            instance = new GameSessionManagement();
        }
        return instance;
    }

    /**
     * Initializes and starts a new game session for the specified player with the given document ID and difficulty level.
     * This method sets up the session by resetting session-specific variables such as score, questions answered,
     * and correct answers to their initial states.
     *
     * @param player the player for whom the session is being started; must not be null
     * @param documentId the ID of the document associated with the session
     * @param difficulty the difficulty level of the session; typically a string like "easy", "medium", or "hard"
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
     * Records the result of a player's answer by incrementing the total number of questions answered
     * and updating the score and count of correct answers if the answer is correct.
     *
     * @param correct a boolean indicating whether the player's answer was correct. If true, the score
     *                and the count of correct answers are updated.
     */
    public void recordAnswer(boolean correct) {
        questionsAnswered++;
        if (correct) {
            correctAnswers++;
            score += 10;
        }
    }

    /**
     * Saves the current game session to the database by inserting the session's details
     * such as username, score, timestamp, difficulty, and document ID.
     *
     * This method establishes a connection to the database and executes an SQL INSERT
     * statement using a prepared statement to prevent SQL injection. The session details
     * are retrieved using getter methods for the current username, score, timestamp,
     * difficulty, and document ID.
     *
     * If an SQLException occurs during the operation, an error message is printed to
     * standard error.
     */
    public void saveSession() {
        String sql = "INSERT INTO sessions (username, score, timestamp, difficulty, id_document) VALUES (?, ?, ?, ?, ?)";

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
     * Retrieves session information for the specified username from the database
     * and prints details such as username, score, and timestamp.
     *
     * @param username the username of the player whose session information is to be retrieved; must not be null
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
     * Resets the current game session by clearing the instance or resetting its fields.
     * This method is typically used to terminate or clear the current session data,
     * ensuring that all session-related fields are either nullified or set to their
     * default initial states.
     */
    public void resetSession() {
        instance = null;
    }

    /**
     * Retrieves the current player of the game session.
     *
     * @return the current Player object managing the game session; may return null if no player is set.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Retrieves the ID of the document associated with the current game session.
     *
     * @return the document ID as an integer; {@code -1} if no document is associated with the session.
     */
    public int getDocumentId() {
        return documentId;
    }

    /**
     * Retrieves the score of the current game session.
     *
     * @return the score of the game session as an integer.
     */
    public int getScore() {
        return score;
    }

    /**
     * Retrieves the total number of questions answered in the current game session.
     *
     * @return the number of questions answered as an integer.
     */
    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    /**
     * Retrieves the total number of correct answers in the current game session.
     *
     * @return the number of correct answers as an integer.
     */
    public int getCorrectAnswers() {
        return correctAnswers;
    }

    /**
     * Calculates the percentage of correctly answered questions in the current game session.
     * If no questions have been answered, the method returns 0.
     *
     * @return the percentage of correct answers as a double value, or 0 if no questions have been answered.
     */
    public double getCorrectPercentage() {
        if (questionsAnswered == 0) {
            return 0;
        }
        return (double) correctAnswers / questionsAnswered * 100;
    }

    /**
     * Retrieves the timestamp of the current game session.
     *
     * @return the timestamp of the game session as a LocalDateTime object.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Retrieves the username of the current player in the game session.
     * If there is no current player, this method returns null.
     *
     * @return the username of the current player as a string, or null if no player is set.
     */
    public String getUsername() {
        return currentPlayer != null ? currentPlayer.getUsername() : null;
    }

    /**
     * Retrieves the difficulty level of the current game session.
     *
     * @return the difficulty level of the game session as a string, or null if no difficulty is set.
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Determines and retrieves the maximum number of questions allowed
     * for the current game session based on the difficulty level.
     *
     * @return the maximum number of questions as an integer for the
     *         current game's difficulty level.
     */
    public int getMaxQuestions() {
        Levels.Difficulty level = Levels.Difficulty.valueOf(difficulty.toUpperCase());
        return Levels.getNumberOfQuestions(level);
    }

    /**
     * Retrieves a list of game sessions for a given username from the database.
     * Each session includes details such as score, timestamp, difficulty, and document ID.
     *
     * @param username the username of the player whose game sessions are to be retrieved; must not be null
     * @return a list of GameSession objects representing the game sessions associated with the specified username;
     *         an empty list if no sessions are found or in case of an error
     */
    public static List<GameSession> getSessionsByUsername(String username) {
        List<GameSession> sessions = new ArrayList<>();

        String query = "SELECT score, timestamp, difficulty, id_document FROM sessions WHERE username = ?";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                GameSession session = new GameSession(
                        rs.getInt("id_document"),
                        rs.getString("difficulty"),
                        rs.getInt("score"),
                        rs.getString("timestamp")
                );
                sessions.add(session);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il recupero delle sessioni: " + e.getMessage());
        }
        return sessions;
    }


    /**
     * Calculates the number of remaining questions in the current game session.
     * This is determined by subtracting the number of questions answered from
     * the total number of questions allowed for the session.
     *
     * @return the remaining number of questions as an integer.
     */
    public int getRemainingQuestions() {
        return getMaxQuestions() - getQuestionsAnswered();
    }

    /**
     * Sets the current player for the game session.
     *
     * @param player the Player object to be set as the current player; must not be null
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    /**
     * Returns a string representation of the game session.
     * The string includes details about the current player, score, difficulty level,
     * and the total number of questions answered.
     *
     * @return a formatted string describing the game session.
     */
    @Override
    public String toString() {
        return "La sessione del giocatore " + getCurrentPlayer() + " col punteggio " + getCorrectAnswers() + " a difficoltà " + getDifficulty() + " ha avuto " + getQuestionsAnswered() + " domande risposte.";
    }

}
