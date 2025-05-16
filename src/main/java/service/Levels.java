package service;

import java.util.List;

/**
 * La classe {@code Levels} gestisce la logica relativa ai livelli di difficoltà del gioco.
 * Ogni livello può influenzare il numero di domande, la complessità delle parole, o il tempo disponibile.
 * Fornisce metodi per selezionare il livello corrente e per ottenere parametri specifici in base alla difficoltà.
 * 
 * <p>Livelli supportati: EASY, MEDIUM, HARD</p>
 * 
 * Questa classe è stateless e può essere utilizzata come utility.
 * 
 * @author Gruppo6
 */
public class Levels {

    /**
     * Enum che rappresenta i livelli di difficoltà disponibili.
     */
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    /**
     * Restituisce il numero di domande per un determinato livello di difficoltà.
     *
     * @param difficulty il livello di difficoltà selezionato
     * @return il numero di domande da porre
     */
    public static int getNumberOfQuestions(Difficulty difficulty) {
        // es: EASY -> 5, MEDIUM -> 10, HARD -> 15
        return 0;
    }

    /**
     * Restituisce il tempo massimo (in secondi) per rispondere a una domanda a seconda della difficoltà.
     *
     * @param difficulty il livello selezionato
     * @return tempo massimo per ogni domanda
     */
    public static int getTimeLimitPerQuestion(Difficulty difficulty) {
        return 0;
    }

    /**
     * Restituisce il punteggio massimo assegnabile per una singola risposta corretta al livello specificato.
     *
     * @param difficulty il livello di gioco
     * @return il punteggio massimo per risposta
     */
    public static int getMaxScorePerQuestion(Difficulty difficulty) {
        return 0;
    }

    /**
     * Restituisce un insieme di categorie o caratteristiche speciali associate al livello (es. confronto, frequenza...).
     *
     * @param difficulty il livello di difficoltà
     * @return una lista di descrizioni delle caratteristiche delle domande
     */
    public static List<String> getQuestionTypesByDifficulty(Difficulty difficulty) {
        return null;
    }

    /**
     * Verifica se la difficoltà specificata è supportata.
     *
     * @param difficulty il livello da verificare
     * @return {@code true} se è un livello valido, altrimenti {@code false}
     */
    public static boolean isValidDifficulty(String difficulty) {
        return false;
    }
}
