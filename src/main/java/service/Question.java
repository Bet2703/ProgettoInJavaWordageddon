package service;

import java.util.List;

/**
 * Rappresenta una domanda con il relativo testo, la risposta corretta e una lista di opzioni di risposta possibili.
 * Questa classe fornisce metodi getter e setter per gestire il testo della domanda,
 * la risposta corretta e le opzioni disponibili per la risposta.
 */
public class Question {

    /**
     * Memorizza il testo della domanda.
     * Questa variabile rappresenta il contenuto principale della domanda,
     * che può essere un'affermazione o un quesito che richiede una risposta da parte dell’utente.
     */
    private String questionText;

    /**
     * Memorizza la risposta corretta alla domanda.
     * Questa variabile rappresenta la risposta attesa, che verrà utilizzata
     * per verificare la correttezza della risposta fornita dall’utente.
     */
    private String correctAnswer;

    /**
     * Rappresenta una lista di possibili risposte alla domanda.
     * Ogni opzione è una stringa e fornisce all’utente delle scelte predefinite
     * tra cui selezionare la risposta.
     */
    private List<String> options;

    /**
     * Restituisce il testo della domanda.
     *
     * @return il testo della domanda come stringa.
     */
    public String getQuestionText() {
        return questionText;
    }

    /**
     * Imposta o aggiorna il testo della domanda.
     *
     * @param questionText il nuovo testo da assegnare alla domanda.
     */
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    /**
     * Restituisce la risposta corretta associata alla domanda.
     *
     * @return la risposta corretta come stringa.
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Imposta o aggiorna la risposta corretta per la domanda.
     *
     * @param correctAnswer la risposta corretta da assegnare alla domanda.
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Restituisce la lista delle opzioni di risposta disponibili per la domanda.
     *
     * @return una lista di stringhe rappresentanti le possibili risposte.
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * Imposta o aggiorna la lista delle opzioni di risposta per la domanda.
     * Sostituisce l’elenco attuale con quello fornito.
     *
     * @param options la nuova lista di opzioni da assegnare alla domanda.
     */
    public void setOptions(List<String> options) {
        this.options = options;
    }
}
