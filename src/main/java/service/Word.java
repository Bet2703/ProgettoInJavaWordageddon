package service;

/**
 * Rappresenta una parola con la relativa frequenza di apparizione in un testo.
 * Fornisce metodi per accedere al testo della parola, alla sua frequenza
 * e per ottenere una rappresentazione in formato stringa dell'oggetto.
 * 
 * Autore: Gruppo6
 */
public class Word {
    // Testo della parola
    private final String text;

    // Frequenza con cui la parola appare (numero di occorrenze)
    private final int frequency;

    /**
     * Costruttore della classe Word.
     * Crea un nuovo oggetto Word con il testo e la frequenza specificati.
     *
     * @param text testo della parola
     * @param frequency frequenza di apparizione della parola
     */
    public Word(String text, int frequency) {
        this.text = text;
        this.frequency = frequency;
    }

    /**
     * Restituisce il testo della parola.
     *
     * @return testo della parola come {@code String}
     */
    public String getText() {
        return text;
    }

    /**
     * Restituisce la frequenza di apparizione della parola.
     *
     * @return frequenza come {@code int}
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Restituisce la frequenza della parola come stringa.
     * Utile per visualizzazioni testuali o UI.
     *
     * @return frequenza come {@code String}
     */
    public String getFrequencyString() {
        return Integer.toString(frequency);
    }

    /**
     * Fornisce una rappresentazione in formato stringa dell'oggetto Word.
     * Formato: "parola (frequenza)".
     *
     * @return stringa rappresentativa della parola e della sua frequenza
     */
    @Override
    public String toString() {
        return text + " (" + frequency + ")";
    }
}
