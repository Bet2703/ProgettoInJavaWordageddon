package model;

/**
 * Rappresenta una sessione di gioco con dettagli come l'ID del documento, 
 * il livello di difficoltà, il punteggio e il timestamp.
 * Questa classe incapsula i dati relativi a una singola istanza di sessione di gioco.
 * 
 * @author Gruppo6
 */
public class GameSession {

    /**
     * Identificatore univoco per l'username associato alla sessione di gioco.
     * Questo valore viene utilizzato per determinare le sessioni del singolo utente.
     */
    private String username;
    /**
     * Identificatore univoco per il documento associato a una sessione di gioco.
     * Questo valore viene utilizzato per tracciare e distinguere le singole sessioni.
     */
    private int documentId;

    /**
     * Indica il livello di difficoltà associato a una sessione di gioco.
     * Questo valore rappresenta la complessità o il livello di sfida selezionato o affrontato durante il gioco.
     * I valori comuni possono includere "facile", "medio" o "difficile", anche se possono variare in base all'implementazione.
     */
    private String difficulty;

    /**
     * Indica il punteggio ottenuto durante una sessione di gioco.
     * Questo valore viene utilizzato per registrare e tracciare i punti guadagnati dal giocatore
     * durante una specifica sessione di gioco.
     */
    private int score;

    /**
     * Rappresenta il timestamp associato a una sessione di gioco.
     * Questo valore registra la data e l'ora in cui si è svolta la sessione.
     * Il formato del timestamp dovrebbe essere conforme agli standard di rappresentazione data/ora.
     */
    private String timestamp;

    /**
     * Costruisce una nuova istanza di GameSession con i dettagli specificati.
     *
     * @param username l'identificatore univoco dell'attuale utente della sessione di gioco
     * @param documentId l'identificatore univoco del documento associato alla sessione di gioco
     * @param difficulty il livello di difficoltà della sessione di gioco
     * @param score il punteggio ottenuto durante la sessione di gioco
     * @param timestamp il timestamp che indica quando si è svolta la sessione di gioco
     */
    public GameSession(String username, int documentId, String difficulty, int score, String timestamp) {
        this.username = username;
        this.documentId = documentId;
        this.difficulty = difficulty;
        this.score = score;
        this.timestamp = timestamp;
    }

    /**
     * Restituisce l'username del giocatore associato alla sessione di gioco.
     *
     * @return l'username univoco del giocatore
     */
    public String getUsername() { return username; }

    /**
     * Restituisce l'ID del documento associato alla sessione di gioco.
     *
     * @return l'identificatore univoco del documento, come intero.
     */
    public int getDocumentId() { return documentId; }

    /**
     * Restituisce il livello di difficoltà associato alla sessione di gioco.
     *
     * @return il livello di difficoltà della sessione di gioco come stringa.
     */
    public String getDifficulty() { return difficulty; }

    /**
     * Restituisce il punteggio ottenuto nella sessione di gioco.
     *
     * @return il punteggio della sessione di gioco come intero.
     */
    public int getScore() { return score; }

    /**
     * Restituisce il timestamp associato alla sessione di gioco.
     *
     * @return il timestamp della sessione di gioco come stringa.
     */
    public String getTimestamp() { return timestamp; }

    /**
     * Restituisce l'ID del documento associato alla sessione di gioco.
     * (Metodo duplicato di getDocumentId; può essere considerato superfluo.)
     *
     * @return l'identificatore univoco del documento come intero.
     */
    public int getDocumentID() { return documentId; }
}
