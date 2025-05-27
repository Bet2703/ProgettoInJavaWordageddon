/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import service.Word;

import java.util.*;

/**
 * Controller class for managing the user interface where questions are presented.
 * Handles answer selection, submission, skipping, and displaying feedback.
 * 
 * This controller is associated with a JavaFX FXML layout.
 * 
 * @author Gruppo6
 */
public class QuestionsController {

    /**
     * Label used to display the question text.
     */
    @FXML
    private Label questionLabel;

    /**
     * Button used to submit the selected answer.
     */
    @FXML
    private Button submitButton;

    /**
     * Button used to skip the current question.
     */
    @FXML
    private Button skipButton;

    /**
     * Label used to show feedback to the user after submitting an answer.
     */
    @FXML
    private Label feedbackLabel;

    /**
     * Radio button for answer option A.
     */
    @FXML
    private RadioButton optionA;

    /**
     * Radio button for answer option B.
     */
    @FXML
    private RadioButton optionB;

    /**
     * Radio button for answer option C.
     */
    @FXML
    private RadioButton optionC;

    /**
     * Radio button for answer option D.
     */
    @FXML
    private RadioButton optionD;


    private ToggleGroup answerGroup;

    /**
     * List of words to use taken from the DB
     */
    private List<Word> wordList;

    /**
     * To store the correct word
     */
    private Word correctWord;

    private int documentId;

    public void setDocumentId(int id) {
        this.documentId = id;
    }

    @FXML
    public void initialize() {
        // Crea il ToggleGroup e assegna i RadioButton al gruppo
        answerGroup = new ToggleGroup();

        optionA.setToggleGroup(answerGroup);
        optionB.setToggleGroup(answerGroup);
        optionC.setToggleGroup(answerGroup);
        optionD.setToggleGroup(answerGroup);

        loadNextQuestion();
    }


    private void loadNextQuestion() {
        wordList = service.QuestionGenerator.getWords(/*documentId*/);

        if (wordList.size() < 4) {
            feedbackLabel.setText("Non ci sono abbastanza parole per generare una domanda.");
            return;
        }

        // Ordina le parole per frequenza decrescente e seleziona la più frequente
        wordList.sort(Comparator.comparingInt(Word::getFrequency).reversed());
        correctWord = wordList.get(0);

        // Seleziona 3 parole errate casuali + quella corretta
        Set<Word> choices = new HashSet<>();
        choices.add(correctWord);
        Random random = new Random();

        while (choices.size() < 4) {
            Word randomWord = wordList.get(random.nextInt(wordList.size()));
            choices.add(randomWord);
        }

        List<Word> options = new ArrayList<>(choices);
        Collections.shuffle(options);

        // Imposta la domanda e le opzioni
        questionLabel.setText("Qual è la parola più frequente?");
        optionA.setText(options.get(0).getText());
        optionB.setText(options.get(1).getText());
        optionC.setText(options.get(2).getText());
        optionD.setText(options.get(3).getText());

        feedbackLabel.setText("");
        answerGroup.selectToggle(null);
    }


    /**
     * Handles the event triggered when the user clicks the "Submit" button.
     * This method should validate the selected answer and display feedback.
     * 
     * @param event the ActionEvent triggered by clicking the submit button
     */
    @FXML
    private void onSubmitAnswer(ActionEvent event) {
        RadioButton selected = (RadioButton) answerGroup.getSelectedToggle();

        if (selected == null) {
            feedbackLabel.setText("Seleziona una risposta prima di inviare.");
            return;
        }

        String selectedText = selected.getText();
        if (selectedText.equals(correctWord.getText())) {
            feedbackLabel.setStyle("-fx-text-fill: green;");
            feedbackLabel.setText("Risposta corretta!");
        } else {
            feedbackLabel.setStyle("-fx-text-fill: red;");
            feedbackLabel.setText("Risposta sbagliata. La parola corretta era: " + correctWord.getText());
        }

        loadNextQuestion();
    }

    /**
     * Handles the event triggered when the user clicks the "Skip" button.
     * This method should proceed to the next question without evaluation.
     * 
     * @param event the ActionEvent triggered by clicking the skip button
     */
    @FXML
    private void onSkipQuestion(ActionEvent event) {
        loadNextQuestion();
    }

}
