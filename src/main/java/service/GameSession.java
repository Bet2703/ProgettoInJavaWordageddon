package service;

public class GameSession {
    private int documentId;
    private String difficulty;
    private int score;
    private String timestamp;

    public GameSession(int documentId, String difficulty, int score, String timestamp) {
        this.documentId = documentId;
        this.difficulty = difficulty;
        this.score = score;
        this.timestamp = timestamp;
    }

    public int getDocumentId() { return documentId; }
    public String getDifficulty() { return difficulty; }
    public int getScore() { return score; }
    public String getTimestamp() { return timestamp; }
}