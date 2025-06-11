package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * La classe {@code QuestionGenerator} fornisce funzionalità per recuperare parole da un database
 * in base all'ID di un documento e per generare domande basate su tali parole.
 * Le domande possono essere utilizzate in quiz o attività educative basate sul contenuto testuale analizzato.
 * 
 * @author Gruppo6
 */
public class QuestionGenerator {

    /**
     * Recupera una lista di oggetti {@link Word} dal database relativi a un documento specifico.
     * Ogni oggetto Word contiene una parola e la sua frequenza di apparizione nel testo.
     *
     * @param id_document l'ID del documento da cui recuperare le parole
     * @return una lista di oggetti {@code Word} che rappresentano le parole e le loro frequenze
     */
    public static List<Word> getWords(int id_document) {
        List<Word> words = new ArrayList<>();
        String sql = "SELECT word, frequency FROM words WHERE id_document = ?";

        System.out.println("Document ID: " + id_document);
        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id_document);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String word = rs.getString("word");
                int frequency = rs.getInt("frequency");
                words.add(new Word(word, frequency));
            }

        } catch (SQLException e) {
            System.err.println("Errore di connessione o query su words: " + e.getMessage());
        }

        return words;
    }

    /**
     * Genera una domanda basata sulla lista di parole fornita. La domanda viene selezionata casualmente
     * tra quattro tipologie disponibili:
     * <ul>
     *     <li>Qual è la parola più frequente?</li>
     *     <li>Quante volte appare una parola casuale?</li>
     *     <li>Qual è la parola meno frequente?</li>
     *     <li>Qual è la lunghezza di una parola casuale?</li>
     * </ul>
     * Per ciascun tipo di domanda vengono generate 4 opzioni (una corretta e tre distrattori), e la risposta
     * corretta viene salvata nell’oggetto {@link Question}.
     *
     * @param wordList la lista di parole da cui generare le domande
     * @param questionIndex indice della domanda (non usato direttamente ma utile per estensioni future)
     * @return un oggetto {@link Question} che rappresenta la domanda generata, la risposta corretta e le opzioni disponibili
     */
    public static Question generateNextQuestion(List<Word> wordList, int questionIndex) {
        Random random = new Random();
        int domanda = random.nextInt(4) + 1; // Seleziona casualmente un tipo di domanda da 1 a 4

        Question question = new Question();
        Set<String> options = new LinkedHashSet<>();
        Word correctWord;

        switch (domanda) {
            case 1: // Parola più frequente
                wordList.sort(Comparator.comparingInt(Word::getFrequency).reversed());
                correctWord = wordList.get(0);
                question.setQuestionText("Qual è la parola più frequente nel testo?");
                options.add(correctWord.getText());
                question.setCorrectAnswer(correctWord.getText());
                break;

            case 2: // Frequenza di una parola casuale
                correctWord = wordList.get(random.nextInt(wordList.size()));
                question.setQuestionText("Quante volte appare la parola \"" + correctWord.getText() + "\" nel testo?");
                options.add(String.valueOf(correctWord.getFrequency()));
                question.setCorrectAnswer(String.valueOf(correctWord.getFrequency()));
                break;

            case 3: // Parola meno frequente
                wordList.sort(Comparator.comparingInt(Word::getFrequency));
                correctWord = wordList.get(0);
                question.setQuestionText("Quale parola ha la frequenza più bassa?");
                options.add(correctWord.getText());
                question.setCorrectAnswer(correctWord.getText());
                break;

            case 4: // Lunghezza di una parola
                correctWord = wordList.get(random.nextInt(wordList.size()));
                question.setQuestionText("Qual è la lunghezza della parola \"" + correctWord.getText() + "\"?");
                options.add(String.valueOf(correctWord.getText().length()));
                question.setCorrectAnswer(String.valueOf(correctWord.getText().length()));
                break;

            default:
                throw new IllegalStateException("Tipo di domanda non previsto: " + domanda);
        }

        // Generazione delle opzioni errate (distrattori) fino a un totale di 4 opzioni
        while (options.size() < 4) {
            String newOption;
            if (domanda == 2) { // Distrattori per frequenza
                newOption = String.valueOf(wordList.get(random.nextInt(wordList.size())).getFrequency());
            } else if (domanda == 4) { // Distrattori per lunghezza
                newOption = String.valueOf(wordList.get(random.nextInt(wordList.size())).getText().length());
            } else { // Distrattori per parole
                newOption = wordList.get(random.nextInt(wordList.size())).getText();
            }
            options.add(newOption); // Set evita duplicati
        }

        // Mischia le opzioni per non rendere ovvia la risposta corretta
        List<String> shuffledOptions = new ArrayList<>(options);
        Collections.shuffle(shuffledOptions);
        question.setOptions(shuffledOptions);

        return question;
    }
}
