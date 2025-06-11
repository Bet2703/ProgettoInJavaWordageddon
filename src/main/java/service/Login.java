package service;

import java.sql.*;
import users.Player;
import users.Role;

/**
 * Fornisce funzionalità per il login, la registrazione e la verifica di disponibilità dello username.
 * Questa classe interagisce con il database per gestire l'autenticazione e le operazioni sugli utenti.
 *
 * Autore: Gruppo6
 */
public class Login {

    /**
     * Autentica un utente validando lo username e la password forniti con quelli presenti nel database.
     * Se le credenziali sono valide, restituisce un oggetto {@link Player} contenente i dati dell’utente.
     *
     * @param inputUsername lo username dell’utente che tenta di accedere
     * @param inputPassword la password dell’utente che tenta di accedere
     * @return un oggetto {@link Player} se l’autenticazione ha successo, oppure {@code null} se le credenziali sono errate o si verifica un errore
     */
    public static Player login(String inputUsername, String inputPassword) {
        String sql = "SELECT username, password, role FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inputUsername);
            pstmt.setString(2, inputPassword);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String roleStr = rs.getString("role");

                Role role = Role.valueOf(roleStr.toUpperCase());

                return new Player(username, password, role);
            } else {
                System.out.println("Credenziali non valide.");
            }

        } catch (SQLException e) {
            System.out.println("Errore di connessione o query su login: " + e.getMessage());
        }

        return null;
    }

    /**
     * Verifica se un determinato username è già presente nel database.
     *
     * @param inputUsername lo username da controllare
     * @return {@code true} se lo username è già utilizzato, {@code false} altrimenti o in caso di errore
     */
    public static boolean isUsernameTaken(String inputUsername) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inputUsername);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Errore di connessione o query su controllo username: " + e.getMessage());
        }

        return false;
    }

    /**
     * Registra un nuovo utente inserendo username, password e ruolo nel database.
     * Utilizza un database SQLite per la memorizzazione.
     *
     * @param inputUsername lo username del nuovo utente
     * @param inputPassword la password del nuovo utente
     * @param role il ruolo del nuovo utente, specificato come enum {@link Role}
     */
    public static void userRegister(String inputUsername, String inputPassword, Role role) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, inputUsername);
            stmt.setString(2, inputPassword);
            stmt.setString(3, role.toString());

            stmt.executeUpdate();
            System.out.println("Registrazione avvenuta con successo.");

        } catch (SQLException e) {
            System.out.println("Errore di connessione o query su registrazione: " + e.getMessage());
        }
    }
}
