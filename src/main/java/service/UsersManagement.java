package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersManagement {

    /**
     * Method that updates the username and password of the user with the given ID.
     *
     * @param userId the ID of the user whose data is to be updated; must be greater than zero and must exist in the database.
     * @param newUsername the new username of the user; must not be {@code null} or empty.
     * @param newPassword the new password of the user; must not be {@code null} or empty.
     *
     * @return {@code true} if the user data was successfully updated; {@code false} otherwise, e.g. if the user does not exist in the database.
     */
    public boolean updateUser(int userId, String newUsername, String newPassword) {
        String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newUsername);
            pstmt.setString(2, newPassword);
            pstmt.setInt(3, userId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Errore nell'aggiornamento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method that deletes the user data from the database based on the given username.
     *
     * @param username the username of the user whose data is to be deleted; must not be {@code null} or empty
     * @return {@code true} if the user data was successfully deleted; {@code false} otherwise, e.g. if the user does not exist in the database
     */
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Errore nell'eliminazione: " + e.getMessage());
            return false;
        }
    }

    /**
     *  Method that retrieves the user data from the database based on the given username.
     *
     * @param username the username of the user whose data is to be retrieved; must not be {@code null} or empty
     */
    public void getUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Username: " + rs.getString("username"));
                System.out.println("Password: " + rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println("Errore nella lettura dei dati: " + e.getMessage());
        }
    }

    /**
     * Returns a list of {@link GameSession} objects for the given username.
     *
     * @param username the username of the user whose sessions are to be retrieved; must not be {@code null} or empty
     * @return a list of {@link GameSession} objects for the given username; an empty list if no sessions are found
     */
    public List<GameSession> getSessionsByUsername(String username) {
        List<GameSession> sessions = new ArrayList<>();

        String query = "SELECT score, timestamp, difficulty, document_id FROM sessions WHERE username = ?";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                GameSession session = new GameSession(
                        rs.getInt("document_id"),
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

}
