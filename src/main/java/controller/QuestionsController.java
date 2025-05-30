/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;
import service.Word;
import service.GameSessionManagement;

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

    private final service.GameSessionManagement session = service.GameSessionManagement.getInstance();

    private int maxQuestions;

    @FXML
    public void initialize() {
        answerGroup = new ToggleGroup();
        optionA.setToggleGroup(answerGroup);
        optionB.setToggleGroup(answerGroup);
        optionC.setToggleGroup(answerGroup);
        optionD.setToggleGroup(answerGroup);

    }

    public void startGame(int documentId) {
        wordList = service.QuestionGenerator.getWords(documentId);

        if (wordList == null) {
            feedbackLabel.setText("Documento non valido o insufficiente.");
            disableInteraction();
            return;
        }

        String difficulty = LevelsController.getDifficulty();
        session.startSession(session.getCurrentPlayer(), documentId, difficulty);
        maxQuestions = session.getMaxQuestions();


        loadNextQuestion(maxQuestions);
    }

    private void disableInteraction() {
        submitButton.setDisable(true);
        skipButton.setDisable(true);
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);
        optionD.setDisable(true);
    }


    private void loadNextQuestion(int maxQuestions) {

        if (session.getQuestionsAnswered() >= maxQuestions) {
            feedbackLabel.setText("Hai completato il quiz! Punteggio: " + session.getScore());
            session.saveSession();
            System.out.println(session.toString());
            disableInteraction();
            return;
        }

        wordList = service.QuestionGenerator.getWords(documentId);
       
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

        int domanda = random.nextInt(4) + 1;
        
        switch(domanda){
            case 1: {
                // Imposta la domanda e le opzioni
                questionLabel.setText("Qual è la parola più frequente nel testo?");
                optionA.setText(options.get(0).getText());
                optionB.setText(options.get(1).getText());
                optionC.setText(options.get(2).getText());
                optionD.setText(options.get(3).getText());
                break;
            }
            case 2:{
                questionLabel.setText("Quante volte appare la parola " + correctWord.getText() + " nella testo?");
                optionA.setText(options.get(0).getFrequencyString());
                optionB.setText(options.get(1).getFrequencyString());
                optionC.setText(options.get(2).getFrequencyString());
                optionD.setText(options.get(3).getFrequencyString());
                break;
            }
            case 3: {
                questionLabel.setText("Qual è la parola più frequente?");
                optionA.setText(options.get(0).getText());
                optionB.setText(options.get(1).getText());
                optionC.setText(options.get(2).getText());
                optionD.setText(options.get(3).getText());
                break;
            }
            case 4: {
                questionLabel.setText("Qual è la parola più frequente?");
                optionA.setText(options.get(0).getText());
                optionB.setText(options.get(1).getText());
                optionC.setText(options.get(2).getText());
                optionD.setText(options.get(3).getText());
                break;
            }
                
        }
        feedbackLabel.setText("");
        answerGroup.selectToggle(null);
    }


    /**
     * Handles the event triggered when the user clicks the "Submit" button.
     * This method should validate the selected answer and display feedback.
     * 
     * @param event the ActionEvent triggered by clicking the "INVIA" button
     */
    @FXML
    private void onSubmitAnswer(ActionEvent event) {
        RadioButton selected = (RadioButton) answerGroup.getSelectedToggle();

        if (selected == null) {
            feedbackLabel.setText("Seleziona una risposta prima di inviare.");
            return;
        }

        String selectedText = selected.getText();
        boolean isCorrect = selectedText.equals(correctWord.getText());

        session.recordAnswer(isCorrect);

        if (isCorrect) {
            feedbackLabel.setStyle("-fx-text-fill: green;");
            feedbackLabel.setText("Risposta corretta!");
        } else {
            feedbackLabel.setStyle("-fx-text-fill: red;");
            feedbackLabel.setText("Risposta sbagliata. La parola corretta era: " + correctWord.getText());
        }

        // Attende 2 secondi prima di caricare la prossima domanda
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(e -> loadNextQuestion(maxQuestions));
        pause.play();
    }

    /**
     * Handles the event triggered when the user clicks the "Skip" button.
     * This method should proceed to the next question without evaluation.
     * 
     * @param event the ActionEvent triggered by clicking the skip button
     */
    @FXML
    private void onSkipQuestion(ActionEvent event) {
        loadNextQuestion(maxQuestions);
    }

    public void setMaxQuestion(int maxQuestions){
        //Da implementare
    }

    public int getDocumentId(){
        return documentId;
    }

    public void setDocumentId(int id) {
        this.documentId = id;
    }

}
