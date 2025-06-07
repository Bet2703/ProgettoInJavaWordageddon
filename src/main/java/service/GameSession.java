package service;

/**
 * Represents a game session with details such as document ID, difficulty level, score, and timestamp.
 * This class encapsulates the data related to a single instance of a game session.
 *
 * @author Gruppo6
 */
public class GameSession {

    /**
     * A unique identifier for the document associated with a game session.
     * This value is used to track and differentiate between individual sessions.
     */
    private int documentId;

    /**
     * Represents the difficulty level associated with a game session.
     * This value indicates the complexity or challenge level selected or encountered during the game.
     * Common values might include "easy", "medium", or "hard", though specific implementations may vary.
     */
    private String difficulty;

    /**
     * Represents the score achieved in a game session.
     * This value is used to record and track the points earned by the player
     * during a specific instance of gameplay.
     */
    private int score;

    /**
     * Represents the timestamp associated with a game session.
     * This value records the date and time at which the session occurred.
     * The format of the timestamp is expected to align with standard date-time representations.
     */
    private String timestamp;

    /**
     * Constructs a new GameSession with the specified details.
     *
     * @param documentId the unique identifier for the document associated with the game session
     * @param difficulty the difficulty level of the game session
     * @param score the score achieved during the game session
     * @param timestamp the timestamp indicating when the game session occurred
     */
    public GameSession(int documentId, String difficulty, int score, String timestamp) {
        this.documentId = documentId;
        this.difficulty = difficulty;
        this.score = score;
        this.timestamp = timestamp;
    }

    /**
     * Retrieves the document ID associated with the game session.
     *
     * @return the unique identifier for the document, as an integer.
     */
    public int getDocumentId() { return documentId; }

    /**
     * Retrieves the difficulty level associated with the game session.
     *
     * @return the difficulty level of the game session as a String.
     */
    public String getDifficulty() { return difficulty; }

    /**
     * Retrieves the score achieved in the game session.
     *
     * @return the score of the game session as an integer.
     */
    public int getScore() { return score; }

    /**
     * Retrieves the timestamp associated with the game session.
     *
     * @return the timestamp of the game session as a String.
     */
    public String getTimestamp() { return timestamp; }
}