package service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages operations related to user data stored in the database.
 * Provides methods for updating, deleting, and retrieving user information,
 * as well as retrieving associated game sessions for a user.
 *
 * @author Gruppo6
 */
public class UsersManagement {

    /**
     * Updates the user information in the database with the provided username and password for the specified user ID.
     *
     * @param userId the unique identifier of the user whose information is to be updated
     * @param newUsername the new username to be set for the user; must not be null or empty
     * @param newPassword the new password to be set for the user; must not be null or empty
     * @return true if the user information was successfully updated in the database, false otherwise
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
     * Deletes a user from the database based on the provided username.
     *
     * @param username the username of the user to be deleted; must not be null or empty
     * @return true if the user was successfully deleted from the database, false otherwise
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
     * Retrieves and prints the user details from the database based on the provided username.
     * The method connects to the database, executes a query to fetch user data,
     * and displays the user's ID, username, and password if the user exists.
     *
     * @param username the username of the user whose details are to be retrieved; must not be null or empty
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
     * Retrieves a list of game sessions associated with the specified username.
     * The game sessions are fetched from the database and include details such
     * as score, timestamp, difficulty, and document ID.
     *
     * @param username the username for which the game sessions are to be retrieved; must not be null or empty
     * @return a list of {@link GameSession} objects representing the game sessions associated with the given username;
     *         an empty list if no sessions are found or if an error occurs during the retrieval process
     */
    public List<GameSession> getSessionsByUsername(String username) {
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

}
