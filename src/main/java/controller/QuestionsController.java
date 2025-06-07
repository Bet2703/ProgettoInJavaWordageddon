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

    /**
     * ToggleGroup used to manage the answer options.
     */
    private ToggleGroup answerGroup;

    /**
     * List of words to use taken from the DB
     */
    private List<Word> wordList;

    /**
     * To store the correct answer in text
     */
    private String correctAnswerText;
    /**
     * ID of the document to be used for the quiz.
     */
    private int documentId;

    /**
     * Instance of the GameSessionManagement class to manage the game session.
     */
    private final service.GameSessionManagement session = service.GameSessionManagement.getInstance();

    /**
     * Maximum number of questions to be asked in the quiz.
     */
    private int maxQuestions;

    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        answerGroup = new ToggleGroup();
        optionA.setToggleGroup(answerGroup);
        optionB.setToggleGroup(answerGroup);
        optionC.setToggleGroup(answerGroup);
        optionD.setToggleGroup(answerGroup);
    }
    
    /**
     * Starts the quiz by loading the first question and preparing the UI for the user to answer it.
     *
     * @param documentId the ID of the document to be used for the quiz
     */
    @FXML
    public void startGame(int documentId) {
        this.documentId = documentId;
        wordList = service.QuestionGenerator.getWords(documentId);

        if (wordList == null) {
            feedbackLabel.setText("Documento non valido o insufficiente.");
            disableInteraction();
            return;
        }

        if (wordList.size() < 4) {
            feedbackLabel.setText("Non ci sono abbastanza parole per generare una domanda.");
            return;
        }

        String difficulty = LevelsController.getDifficulty();
        session.startSession(session.getCurrentPlayer(), documentId, difficulty);
        maxQuestions = session.getMaxQuestions();
        loadNextQuestion(maxQuestions);
    }
    
    /**
     * Disables the user interaction with the UI.
     * This method should be called when the quiz is over.
     */
    private void disableInteraction() {
        submitButton.setDisable(true);
        skipButton.setDisable(true);
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);
        optionD.setDisable(true);
    }
    
    /**
     * Loads the next question and prepares the UI for the user to answer it.
     *
     * @param maxQuestions the maximum number of questions to be asked
     */
    private void loadNextQuestion(int maxQuestions) {

        feedbackLabel.setStyle("-fx-text-fill: black;");
        feedbackLabel.setText("");

        if (session.getQuestionsAnswered() >= maxQuestions) {
            feedbackLabel.setText("Hai completato il quiz! Punteggio: " + session.getScore());
            session.saveSession();
            System.out.println(session.toString());
            disableInteraction();
            return;
        }

        Random random = new Random();
        int domanda = random.nextInt(4) + 1;
        feedbackLabel.setText("");
        answerGroup.selectToggle(null);

        switch (domanda) {
            case (1): { // Più frequente
                wordList.sort(Comparator.comparingInt(Word::getFrequency).reversed());
                Word correctWord = wordList.get(0);
                correctAnswerText = correctWord.getText();

                Set<String> options = new LinkedHashSet<>();
                options.add(correctAnswerText);
                while (options.size() < 4) {
                    options.add(wordList.get(random.nextInt(wordList.size())).getText());
                }

                List<String> shuffled = new ArrayList<>(options);
                Collections.shuffle(shuffled);
                questionLabel.setText("Qual è la parola più frequente nel testo?");
                setOptions(shuffled);
                break;
            }

            case (2): { // Frequenza di una parola random
                Word selectedWord = wordList.get(random.nextInt(wordList.size()));
                correctAnswerText = selectedWord.getFrequencyString();

                Set<String> options = new LinkedHashSet<>();
                options.add(correctAnswerText);
                while (options.size() < 4) {
                    String freq = String.valueOf(wordList.get(random.nextInt(wordList.size())).getFrequency());
                    options.add(freq);
                }

                List<String> shuffled = new ArrayList<>(options);
                Collections.shuffle(shuffled);
                questionLabel.setText("Quante volte appare la parola \"" + selectedWord.getText() + "\" nel testo?");
                setOptions(shuffled);
                break;
            }

            case (3): { // Meno frequente
                wordList.sort(Comparator.comparingInt(Word::getFrequency));
                Word correctWord = wordList.get(0);
                correctAnswerText = correctWord.getText();

                Set<String> options = new LinkedHashSet<>();
                options.add(correctAnswerText);
                while (options.size() < 4) {
                    options.add(wordList.get(random.nextInt(wordList.size())).getText());
                }

                List<String> shuffled = new ArrayList<>(options);
                Collections.shuffle(shuffled);
                questionLabel.setText("Quale parola ha la frequenza più bassa?");
                setOptions(shuffled);
                break;
            }

            case (4): { // Lunghezza parola
                Word selectedWord = wordList.get(random.nextInt(wordList.size()));
                correctAnswerText = String.valueOf(selectedWord.getText().length());

                Set<String> options = new LinkedHashSet<>();
                options.add(correctAnswerText);
                while (options.size() < 4) {
                    String len = String.valueOf(wordList.get(random.nextInt(wordList.size())).getText().length());
                    options.add(len);
                }

                List<String> shuffled = new ArrayList<>(options);
                Collections.shuffle(shuffled);
                questionLabel.setText("Qual è la lunghezza della parola \"" + selectedWord.getText() + "\"?");
                setOptions(shuffled);
                break;
            }
        }
    }

    /**
     * Set the answers for the question
     * @param options 
     */
    private void setOptions(List<String> options) {
        optionA.setText(options.get(0));
        optionB.setText(options.get(1));
        optionC.setText(options.get(2));
        optionD.setText(options.get(3));
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
        boolean isCorrect = selectedText.equals(correctAnswerText);
        session.recordAnswer(isCorrect);

        if (isCorrect) {
            feedbackLabel.setStyle("-fx-text-fill: green;");
            feedbackLabel.setText("Risposta corretta!");
        } else {
            feedbackLabel.setStyle("-fx-text-fill: red;");
            feedbackLabel.setText("Risposta sbagliata. La risposta corretta era: " + correctAnswerText);
        }

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

    public void setMaxQuestion(int maxQuestions) {
        this.maxQuestions = maxQuestions;
    }

    public int getDocumentId() {
        return documentId;
    }

    /**
     * Sets the ID of the document associated with the quiz.
     * @param id
     */
    @FXML
    public void setDocumentId(int id) {
        this.documentId = id;
    }
}
