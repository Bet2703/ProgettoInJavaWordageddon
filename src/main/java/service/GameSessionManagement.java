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
 * La classe GameSessionManagement gestisce le sessioni di gioco per un giocatore.
 * Utilizza il pattern Singleton per garantire che una sola sessione sia attiva alla volta.
 * Gestisce inizializzazione, tracciamento e salvataggio dei dati di una sessione,
 * come il punteggio, la difficoltà e l'ora di inizio.
 */
public class GameSessionManagement {

    /** Istanza Singleton della classe GameSessionManagement */
    private static GameSessionManagement instance;

    /** Giocatore attualmente attivo nella sessione di gioco */
    private Player currentPlayer;

    /** Identificativo del documento associato alla sessione */
    private int documentId;

    /** Punteggio corrente della sessione */
    private int score;

    /** Numero di domande a cui il giocatore ha risposto */
    private int questionsAnswered;

    /** Numero di risposte corrette fornite dal giocatore */
    private int correctAnswers;

    /** Timestamp di creazione o aggiornamento della sessione */
    private LocalDateTime timestamp;

    /** Livello di difficoltà della sessione di gioco */
    private String difficulty;

    /**
     * Costruttore privato per implementare il pattern Singleton.
     * Impedisce l'istanziazione esterna della classe.
     */
    private GameSessionManagement() {
    }

    /**
     * Restituisce l'unica istanza di GameSessionManagement.
     * Crea una nuova istanza se non esiste ancora.
     *
     * @return istanza Singleton di GameSessionManagement
     */
    public static GameSessionManagement getInstance() {
        if (instance == null) {
            instance = new GameSessionManagement();
        }
        return instance;
    }

    /**
     * Avvia una nuova sessione di gioco per il giocatore specificato.
     * Inizializza il punteggio, le risposte e imposta il timestamp corrente.
     *
     * @param player il giocatore per cui avviare la sessione
     * @param documentId ID del documento associato alla sessione
     * @param difficulty livello di difficoltà ("easy", "medium", "hard")
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
     * Registra il risultato di una risposta.
     * Aggiorna il numero di risposte e, se corretta, aumenta anche il punteggio.
     *
     * @param correct true se la risposta è corretta, false altrimenti
     */
    public void recordAnswer(boolean correct) {
        questionsAnswered++;
        if (correct) {
            correctAnswers++;
            score += 10;
        }
    }

    /**
     * Salva la sessione corrente nel database.
     * Inserisce i dati della sessione come username, punteggio, orario, difficoltà e documento.
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
     * Recupera le sessioni dal database per un determinato username
     * e ne stampa i dettagli su console.
     *
     * @param username nome utente del giocatore
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
     * Resetta la sessione corrente.
     * Elimina l'istanza Singleton per azzerare la sessione.
     */
    public void resetSession() {
        instance = null;
    }

    /**
     * Restituisce il giocatore attuale della sessione.
     *
     * @return oggetto Player, o null se non impostato
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Restituisce l'ID del documento associato alla sessione.
     *
     * @return ID documento come intero
     */
    public int getDocumentId() {
        return documentId;
    }

    /**
     * Restituisce il punteggio corrente della sessione.
     *
     * @return punteggio attuale
     */
    public int getScore() {
        return score;
    }

    /**
     * Restituisce il numero di domande a cui è stato risposto.
     *
     * @return domande totali risposte
     */
    public int getQuestionsAnswered() {
        return questionsAnswered;
    }

    /**
     * Restituisce il numero di risposte corrette.
     *
     * @return risposte corrette totali
     */
    public int getCorrectAnswers() {
        return correctAnswers;
    }

    /**
     * Calcola la percentuale di risposte corrette.
     *
     * @return percentuale di correttezza (0 se nessuna risposta)
     */
    public double getCorrectPercentage() {
        if (questionsAnswered == 0) {
            return 0;
        }
        return (double) correctAnswers / questionsAnswered * 100;
    }

    /**
     * Restituisce il timestamp della sessione.
     *
     * @return oggetto LocalDateTime con data e ora
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Restituisce lo username del giocatore attuale.
     *
     * @return username o null se non presente
     */
    public String getUsername() {
        return currentPlayer != null ? currentPlayer.getUsername() : null;
    }

    /**
     * Restituisce il livello di difficoltà della sessione.
     *
     * @return difficoltà come stringa
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Determina il numero massimo di domande consentite
     * in base alla difficoltà corrente.
     *
     * @return numero massimo di domande
     */
    public int getMaxQuestions() {
        Levels.Difficulty level = Levels.Difficulty.valueOf(difficulty.toUpperCase());
        return Levels.getNumberOfQuestions(level);
    }

    /**
     * Recupera tutte le sessioni di un utente dal database.
     *
     * @param username nome utente
     * @return lista di oggetti GameSession relativi all'utente
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
     * Calcola il numero di domande rimanenti nella sessione corrente.
     *
     * @return domande rimanenti
     */
    public int getRemainingQuestions() {
        return getMaxQuestions() - getQuestionsAnswered();
    }

    /**
     * Imposta il giocatore attuale per la sessione di gioco.
     *
     * @param player oggetto Player da associare
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    /**
     * Restituisce una rappresentazione testuale della sessione corrente.
     *
     * @return stringa descrittiva della sessione
     */
    @Override
    public String toString() {
        return "La sessione del giocatore " + getCurrentPlayer() + " col punteggio " + getCorrectAnswers() +
               " a difficoltà " + getDifficulty() + " ha avuto " + getQuestionsAnswered() + " domande risposte.";
    }
}
