package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * The QuestionGenerator class provides functionality to retrieve Word objects from a database
 * based on the given document ID. The retrieved words can then be used for generating questions
 * or other text processing tasks.
 *
 * @author Gruppo6
 */
public class QuestionGenerator {

    /**
     * Retrieves a list of Word objects from the database for a given document ID.
     * Each Word object contains a text and its associated frequency.
     *
     * @param id_document the ID of the document from which words are to be retrieved
     * @return a list of Word objects representing the words and their frequencies for the specified document
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
     * Generates the next question based on the provided list of words and a question index.
     * The method selects a random question type and constructs a corresponding question
     * with its text, correct answer, and multiple choice options.
     *
     * @param wordList the list of Word objects representing the words and their frequencies
     * @param questionIndex an index indicating the sequential order of the question (not directly used in the method)
     * @return a Question object containing the question text, correct answer, and a list of options
     */
    public static Question generateNextQuestion(List<Word> wordList, int questionIndex) {
        Random random = new Random();
        int domanda = random.nextInt(4) + 1;

        Question question = new Question();
        Set<String> options = new LinkedHashSet<>();
        Word correctWord;

        switch (domanda) {
            case 1: // Più frequente
                wordList.sort(Comparator.comparingInt(Word::getFrequency).reversed());
                correctWord = wordList.get(0);
                question.setQuestionText("Qual è la parola più frequente nel testo?");
                options.add(correctWord.getText());
                question.setCorrectAnswer(correctWord.getText());
                break;

            case 2: // Frequenza di una parola random
                correctWord = wordList.get(random.nextInt(wordList.size()));
                question.setQuestionText("Quante volte appare la parola \"" + correctWord.getText() + "\" nel testo?");
                options.add(String.valueOf(correctWord.getFrequency()));
                question.setCorrectAnswer(String.valueOf(correctWord.getFrequency()));
                break;

            case 3: // Meno frequente
                wordList.sort(Comparator.comparingInt(Word::getFrequency));
                correctWord = wordList.get(0);
                question.setQuestionText("Quale parola ha la frequenza più bassa?");
                options.add(correctWord.getText());
                question.setCorrectAnswer(correctWord.getText());
                break;

            case 4: // Lunghezza parola
                correctWord = wordList.get(random.nextInt(wordList.size()));
                question.setQuestionText("Qual è la lunghezza della parola \"" + correctWord.getText() + "\"?");
                options.add(String.valueOf(correctWord.getText().length()));
                question.setCorrectAnswer(String.valueOf(correctWord.getText().length()));
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + domanda);
        }

        while (options.size() < 4) {
            String newOption;
            if (domanda == 2) {
                newOption = String.valueOf(wordList.get(random.nextInt(wordList.size())).getFrequency());
            } else if (domanda == 4) {
                newOption = String.valueOf(wordList.get(random.nextInt(wordList.size())).getText().length());
            } else {
                newOption = wordList.get(random.nextInt(wordList.size())).getText();
            }
            options.add(newOption);
        }

        List<String> shuffledOptions = new ArrayList<>(options);
        Collections.shuffle(shuffledOptions);
        question.setOptions(shuffledOptions);

        return question;
    }

}
