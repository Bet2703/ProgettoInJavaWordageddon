package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class GameSessionManagement {

    private String username;
    private int score;
    private LocalDateTime timestamp;

    public GameSessionManagement(String username) {
        this.username = username;
        this.score = 0;
        this.timestamp = LocalDateTime.now(); // Imposta la data/ora della sessione all'avvio
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore(int amount) {
        this.score += amount;
    }

    public void resetScore() {
        this.score = 0;
    }

    public void resetTimestamp() {
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "GameSession{" +
                "username='" + username + '\'' +
                ", score=" + score +
                ", timestamp=" + timestamp +
                '}';
    }

    public void saveSession(GameSessionManagement session) {
        String sql = "INSERT INTO sessions (username, score, timestamp) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, session.getUsername());
            pstmt.setInt(2, session.getScore());
            pstmt.setString(3, session.getTimestamp().toString());

            int rowsInserted = pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Errore durante il salvataggio della sessione: " + e.getMessage());
        }
    }

    public void getSession(String username) {
        String sql = "SELECT * FROM sessions WHERE username = ?";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Punteggio: " + rs.getInt("score"));
                System.out.println("Data/Ora: " + rs.getString("timestamp"));
            }

        } catch (SQLException e) {
            System.err.println("Errore durante il salvataggio della sessione: " + e.getMessage());
        }

    }
}
