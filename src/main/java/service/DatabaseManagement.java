
package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import alert.Messages;

/**
 * Fornisce funzionalità per la gestione della connessione a un database SQLite.
 * Facilita l'instaurazione e la chiusura di una connessione al file di database {@code wordageddon.db}.
 * Utilizza il pattern singleton per gestire la connessione e fornisce feedback all'utente
 * tramite messaggi di dialogo grafici.
 *
 * @author Gruppo6
 */
public class DatabaseManagement {

    /**
     * URL che rappresenta la stringa di connessione al database SQLite.
     * Questa costante specifica il percorso relativo del file di database "wordageddon.db"
     * da utilizzare in congiunzione con il driver JDBC per SQLite.
     *
     * Formato: jdbc:sqlite:&lt;percorso_relativo_al_file_db&gt;
     * Esempio: "jdbc:sqlite:./wordageddon.db"
     */
    private static final String URL = "jdbc:sqlite:./wordageddon.db";

    /**
     * Rappresenta l'unica istanza condivisa della connessione utilizzata per interagire
     * con il database SQLite {@code wordageddon.db}. Questa variabile viene inizializzata
     * e gestita all'interno della classe {@code DatabaseManagement}.
     *
     * La {@code connection} viene inizializzata in modo lazy e riutilizzata durante tutto
     * il ciclo di vita dell'applicazione, garantendo un utilizzo efficiente delle risorse
     * ed evitando multiple istanze per la stessa connessione al database.
     *
     * Inizialmente impostata a {@code null}, dovrebbe essere accessibile o modificata solo
     * tramite i metodi appropriati all'interno della classe {@code DatabaseManagement},
     * come {@link DatabaseManagement#getConnection()} e {@link DatabaseManagement#closeConnection()}.
     */
    private static Connection connection = null;

    /**
     * Stabilisce una connessione al database SQLite specificato dall'{@code URL}.
     * Se l'oggetto connection è null o già chiuso, inizializza una nuova connessione.
     * La connessione è gestita come singleton per garantire un uso efficiente delle risorse.
     * In caso di successo, mostra un messaggio informativo all'utente.
     *
     * @return un oggetto {@link Connection} che rappresenta la connessione al database,
     *         oppure {@code null} se si è verificato un errore durante il processo di connessione.
     * @see Messages#showInfo(String, String) Messaggio di successo visualizzato
     * @see Messages#showError(String, String) Messaggio di errore visualizzato
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(URL);
            }
        } catch (ClassNotFoundException e) {
            Messages.showError("Errore Driver JDBC", "Driver SQLite non trovato: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            Messages.showError("Errore Connessione", "Errore nella connessione al database: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Chiude la connessione attiva al database SQLite se esiste ed è aperta.
     * Questo metodo garantisce che qualsiasi connessione stabilita al database venga
     * chiusa correttamente, rilasciando le risorse associate.
     * In caso di successo, mostra un messaggio informativo all'utente.
     *
     * Se la connessione è già chiusa o null, il metodo non fa nulla.
     * Se si verifica un errore durante la chiusura, mostra un messaggio di errore
     * all'utente e lo registra nello standard error stream.
     *
     * Sebbene ridondante con l'uso di try-with-resources, questo metodo è implementato
     * per garantire un controllo esplicito della chiusura della connessione.
     *
     * @see Messages#showInfo(String, String) Messaggio di successo visualizzato
     * @see Messages#showError(String, String) Messaggio di errore visualizzato
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                Messages.showError("Errore Chiusura", "Errore durante la chiusura della connessione: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}