/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gruppo6
 */
public class QuestionGenerator {

    //Creata nuova classe Word per essere più flessibili ed avere più facilità per generare random la domanda
    public static List<Word> getWords() {
        List<Word> words = new ArrayList<>();
        String sql = "SELECT word, frequency FROM words";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

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

    /* Esempio di utilizzo per domande future
    *
    * List<Word> allWords = getWords();
    * Collections.shuffle(allWords);
    * Word random = allWords.get(0);
    * System.out.println("La parola '" + random.getText() + "' compare " + random.getFrequency() + " volte?");
     */
}
