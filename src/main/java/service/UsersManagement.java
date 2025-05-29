package service;

import java.sql.*;

public class UsersManagement {

    // Metodo per aggiornare i dati dell'utente
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

    // Metodo per eliminare l'utente
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

    // Metodo per ottenere i dati dell'utente (opzionale/test)
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
}
