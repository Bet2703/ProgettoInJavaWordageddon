package management;

import model.Question;
import model.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * La classe {@code QuestionManagement} fornisce funzionalità per recuperare parole da un database
 * in base all'ID di un documento e per generare domande basate su tali parole.
 * Le domande possono essere utilizzate in quiz o attività educative basate sul contenuto testuale analizzato.
 * 
 * @author Gruppo6
 */
public class QuestionManagement {

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
    * Genera una nuova domanda casuale basata su una lista di parole.
    * <p>
    * Il tipo di domanda viene scelto casualmente tra vari casi, come:
    * <ul>
    *   <li>Parola più frequente</li>
    *   <li>Frequenza di una parola specifica</li>
    *   <li>Parola meno frequente</li>
    *   <li>Lunghezza di una parola</li>
    *   <li>Parola più frequente tra alcune opzioni</li>
    *   <li>Parola più lunga</li>
    *   <li>Parola con una lunghezza specifica</li>
    *   <li>Prima parola nel testo</li>
    *   <li>Parola che segue una data parola</li>
    *   <li>Seconda parola più frequente</li>
    * </ul>
    * </p>
    *
    * @param wordList lista di oggetti {@code Word} da cui generare la domanda; deve contenere almeno una parola
    * @param questionIndex indice della domanda corrente (non usato nel metodo, ma potenzialmente utile per future estensioni)
    * @return un oggetto {@code Question} con testo della domanda, opzioni e risposta corretta
    * @throws IllegalStateException se il tipo di domanda generato non è previsto o se non ci sono parole corrispondenti ai criteri specifici
    */


    public static Question generateNextQuestion(List<Word> wordList, int questionIndex) {
    if (wordList == null || wordList.isEmpty()) {
        throw new IllegalArgumentException("La lista delle parole non può essere vuota");
    }

    Random random = new Random();
    int domanda = random.nextInt(10) + 1; // 1-10

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

        case 5: // Parola più frequente tra alcune opzioni
            int optionCount = Math.min(4, wordList.size());
            Set<Word> candidateWords = new HashSet<>();
            while (candidateWords.size() < optionCount) {
                candidateWords.add(wordList.get(random.nextInt(wordList.size())));
            }
            correctWord = Collections.max(candidateWords, Comparator.comparingInt(Word::getFrequency));
            question.setQuestionText("Quale parola è la più frequente fra queste?");
            candidateWords.forEach(w -> options.add(w.getText()));
            question.setCorrectAnswer(correctWord.getText());
            break;

        case 6: // Parola più lunga
            correctWord = Collections.max(wordList, Comparator.comparingInt(w -> w.getText().length()));
            question.setQuestionText("Qual è la parola più lunga nel testo?");
            options.add(correctWord.getText());
            question.setCorrectAnswer(correctWord.getText());
            break;

        case 7: // Quale parola ha la lunghezza X?
            Set<Integer> lengths = wordList.stream()
                    .map(w -> w.getText().length())
                    .collect(Collectors.toSet());
            if (lengths.isEmpty()) {
                throw new IllegalStateException("Nessuna parola trovata nel testo.");
            }
            // Scegli una lunghezza valida a caso
            int targetLength = lengths.stream()
                    .skip(random.nextInt(lengths.size()))
                    .findFirst()
                    .get();

            List<Word> matchingWords = wordList.stream()
                    .filter(w -> w.getText().length() == targetLength)
                    .collect(Collectors.toList());

            correctWord = matchingWords.get(random.nextInt(matchingWords.size()));
            question.setQuestionText("Quale parola ha lunghezza " + targetLength + "?");
            options.add(correctWord.getText());
            question.setCorrectAnswer(correctWord.getText());
            break;

        case 8: // Prima parola nel testo
            correctWord = wordList.get(0);
            question.setQuestionText("Quale tra queste parole è apparsa per prima nel testo?");
            options.add(correctWord.getText());
            question.setCorrectAnswer(correctWord.getText());
            break;

        case 9: // Parola che segue una iesima parola
            if (wordList.size() < 2) {
                // fallback se lista troppo corta
                correctWord = wordList.get(0);
                question.setQuestionText("Qual è una parola del testo?");
            } else {
                int i = random.nextInt(wordList.size() - 1) + 1; // da 1 a size-1
                correctWord = wordList.get(i);
                question.setQuestionText("Quale parola seguiva '" + wordList.get(i - 1).getText() + "'?");
            }
            options.add(correctWord.getText());
            question.setCorrectAnswer(correctWord.getText());
            break;

        case 10: // Seconda parola più frequente
            wordList.sort(Comparator.comparingInt(Word::getFrequency).reversed());
            if (wordList.size() < 2) {
                correctWord = wordList.get(0);
                question.setQuestionText("Qual è la parola più frequente nel testo?");
            } else {
                correctWord = wordList.get(1);
                question.setQuestionText("Quale tra queste è la seconda parola più frequente nel testo?");
            }
            options.add(correctWord.getText());
            question.setCorrectAnswer(correctWord.getText());
            break;

        default:
            throw new IllegalStateException("Tipo di domanda non previsto: " + domanda);
    }

    // Generazione opzioni distrattori fino a 4 opzioni totali
        int attempts = 0;
        final int MAX_ATTEMPTS = 20;

        while (options.size() < 4 && attempts < MAX_ATTEMPTS) {
            String newOption;

            if (domanda == 2) { // Distrattori per frequenza (numeri)
                int correctFreq = Integer.parseInt(question.getCorrectAnswer());
                int candidateFreq;
                int tries = 0;
                do {
                    candidateFreq = correctFreq + random.nextInt(5) - 2; // +/- 2
                    if (candidateFreq < 1) candidateFreq = 1;
                    tries++;
                    if (tries > 10) break; // evita loop infinito
                } while (options.contains(String.valueOf(candidateFreq)));
                newOption = String.valueOf(candidateFreq);

            } else if (domanda == 4) { // Distrattori per lunghezza (numeri)
                newOption = String.valueOf(wordList.get(random.nextInt(wordList.size())).getText().length());

            } else { // Distrattori per parole
                newOption = wordList.get(random.nextInt(wordList.size())).getText();
            }

            options.add(newOption);
            attempts++;
        }

        // Se ancora poche opzioni, riempi con "N/A" per evitare problemi
        while (options.size() < 4) {
            options.add("N/A");
        }

        // Mischia opzioni per non rendere ovvia la risposta corretta
        List<String> shuffledOptions = new ArrayList<>(options);
        Collections.shuffle(shuffledOptions);
        question.setOptions(shuffledOptions);

        return question;
    }

}
