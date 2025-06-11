package management;

import java.sql.*;

/**
 * Classe responsabile della gestione delle operazioni relative agli utenti memorizzati nel database.
 * Fornisce metodi per aggiornare, eliminare e recuperare informazioni sugli utenti.
 * Può essere estesa per includere funzionalità aggiuntive come registrazione o autenticazione.
 * 
 * Autore: Gruppo6
 */
public class UsersManagement {

    /**
     * Aggiorna le informazioni di un utente nel database, modificando username e password
     * per l'utente corrispondente all'ID specificato.
     *
     * @param userId ID univoco dell'utente da aggiornare
     * @param newUsername nuovo username da assegnare all'utente (non deve essere nullo o vuoto)
     * @param newPassword nuova password da assegnare all'utente (non deve essere nulla o vuota)
     * @return {@code true} se l'aggiornamento è andato a buon fine, {@code false} altrimenti
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
     * Elimina un utente dal database in base allo username fornito.
     * Se lo username non esiste, non viene effettuata alcuna eliminazione.
     *
     * @param username username dell'utente da eliminare (non deve essere nullo o vuoto)
     * @return {@code true} se l'utente è stato eliminato correttamente, {@code false} in caso contrario
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
     * Recupera e stampa i dettagli di un utente in base allo username specificato.
     * I dati visualizzati includono ID, username e password (non consigliato in produzione).
     *
     * @param username username dell'utente da recuperare (non deve essere nullo o vuoto)
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
                System.out.println("Password: " + rs.getString("password")); // ⚠️ Attenzione: evitare di stampare la password in ambienti reali
            }
        } catch (SQLException e) {
            System.out.println("Errore nella lettura dei dati: " + e.getMessage());
        }
    }
}
