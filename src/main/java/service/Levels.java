package service;

import java.util.List;

/**
 * Fornisce metodi per gestire i diversi livelli di difficoltà disponibili in un gioco.
 * Ogni livello può definire attributi specifici come il numero di domande, limiti di tempo,
 * regole di punteggio e altre proprietà personalizzate.
 *
 * Autore: Gruppo6
 */
public class Levels {

    /**
     * Enum che rappresenta i livelli di difficoltà disponibili nell'applicazione.
     * Questo enum è usato per categorizzare i diversi livelli di sfida del gioco
     * ed è tipicamente associato a proprietà come il numero di domande, limiti di tempo
     * e meccanismi di punteggio.
     *
     * I livelli disponibili sono:
     * - EASY: rappresenta un livello base o per principianti.
     * - MEDIUM: rappresenta un livello di difficoltà intermedia.
     * - HARD: rappresenta un livello avanzato o per esperti.
     */
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    /**
     * Restituisce il numero di domande previste per uno specifico livello di difficoltà.
     *
     * @param difficulty il livello di difficoltà per cui determinare il numero di domande
     * @return il numero di domande associato al livello specificato
     */
    public static int getNumberOfQuestions(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 5;
            case MEDIUM:
                return 10;
            case HARD:
                return 15;
            default:
                return 0;
        }
    }

    /**
     * Determina il limite di tempo in secondi in base al livello di difficoltà specificato.
     * Se il livello di difficoltà non è riconosciuto, viene restituito un valore di default pari a 15 secondi.
     *
     * @param difficulty il livello di difficoltà come stringa.
     *                   I valori attesi sono "EASY", "MEDIUM" o "HARD" (il confronto è case-insensitive).
     * @return il limite di tempo in secondi per il livello specificato:
     *         30 per "EASY", 20 per "MEDIUM", 10 per "HARD", 15 per valori non validi.
     */
    public static int getSecondsByDifficulty(String difficulty) {
        switch (difficulty) {
            case "EASY":
                return 30;
            case "MEDIUM":
                return 20;
            case "HARD":
                return 10;
            default:
                return 15;
        }
    }

    /**
     * Restituisce il punteggio massimo assegnabile per una domanda, in base al livello di difficoltà.
     *
     * @param difficulty il livello di difficoltà della domanda, rappresentato dall'enum {@code Difficulty}.
     *                   I valori possibili sono {@code EASY}, {@code MEDIUM} e {@code HARD}.
     * @return il punteggio massimo per una domanda in base al livello di difficoltà.
     *         Restituisce {@code 0} se il livello non è riconosciuto o è null.
     */
    public static int getMaxScorePerQuestion(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 10;
            case MEDIUM:
                return 20;
            case HARD:
                return 30;
            default:
                return 0;
        }
    }

    /**
     * Restituisce una lista di tipi di domande associati a un determinato livello di difficoltà.
     * (Metodo da implementare)
     *
     * @param difficulty il livello di difficoltà per cui richiedere i tipi di domanda.
     *                   Deve essere un valore valido dell'enum {@code Difficulty}, come {@code EASY}, {@code MEDIUM} o {@code HARD}.
     * @return una {@code List} di {@code String} che rappresentano i tipi di domande disponibili per quel livello,
     *         oppure una lista vuota/null se il livello non ha tipi associati o non è valido.
     */
    public static List<String> getQuestionTypesByDifficulty(Difficulty difficulty) {
        return null;
    }

    /**
     * Verifica se una stringa rappresenta un livello di difficoltà valido dell'enum {@code Difficulty}.
     *
     * @param difficulty stringa che rappresenta il livello di difficoltà.
     *                   Deve corrispondere (ignorando il case) a uno dei valori validi dell'enum:
     *                   "EASY", "MEDIUM" o "HARD".
     * @return {@code true} se la stringa corrisponde a un livello valido, {@code false} altrimenti (incluso null).
     */
    public static boolean isValidDifficulty(String difficulty) {
        try {
            Difficulty.valueOf(difficulty.toUpperCase());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }


    public static int getDocumentCountByDifficulty(String difficulty) {
        switch (difficulty.toUpperCase()) {
            case "EASY":
            return 10;
            case "MEDIUM":
                return 20;
            case "HARD":
                return 30;
            default:
                return 0;
        }
    }
}
