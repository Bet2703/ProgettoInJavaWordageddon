package controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.Timeline;
import service.Word;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The QuestionsController class is responsible for managing the quiz interface,
 * handling user interactions, and coordinating the state and behavior of the quiz application.
 * It provides functionality for starting the quiz, managing the timer, handling
 * user input, and displaying feedback.
 * This controller is associated with a JavaFX FXML layout
 *
 * @author Gruppo6
 */
public class QuestionsController {

    /**
     * Represents a label in the UI that displays the question
     * currently being presented to the user during the quiz.
     * The text of this label is updated dynamically to reflect
     * the content of the active question.
     */
    @FXML
    private Label questionLabel;

    /**
     * Represents the "Submit" button in the user interface of the quiz.
     * This button allows the user to submit their selected answer for evaluation.
     * When clicked, it triggers the {@link #onSubmitAnswer()} method,
     * which validates the user's response and provides feedback.
     * Usage: The button is part of the quiz view and interacts with event handlers
     * to process the user's answer during the quiz flow.
     */
    @FXML
    private Button submitButton;

    /**
     * Represents the button in the UI that allows the user to skip the current question.
     * When clicked, it triggers the action to move to the next question without evaluation.
     * This button provides users with the option to bypass any question they do not wish to answer.
     * It is wired to its corresponding event handler method in the controller.
     */
    @FXML
    private Button skipButton;

    /**
     * The `feedbackLabel` is a UI component used to display feedback to the user
     * during the quiz process. This label provides textual information, such as
     * whether an answer was correct, incorrect, or if an action cannot be performed.
     * It is primarily updated based on user interactions and quiz progression logic.
     */
    @FXML
    private Label feedbackLabel;


    /**
     * Represents the label in the user interface that displays the remaining time
     * or countdown during the quiz session.
     * This label is updated dynamically based on the quiz's timer functionality
     * and is part of the UI managed by the QuestionsController.
     */
    @FXML
    private Label timerLabel;

    /**
     * Represents a radio button in the quiz interface that allows the user
     * to select the first option (option A) as their answer.
     * This component is linked to the corresponding UI element in the FXML file.
     * It is part of the group of selectable options for a quiz question and
     * managed by the QuestionsController class.
     */
    @FXML
    private RadioButton optionA;

    /**
     * Represents a RadioButton in the quiz interface labeled as option B.
     * It is intended to allow users to select this option as their answer
     * during the quiz. This RadioButton is part of a group of options
     * displayed to the user as possible answers to a question.
     * This field is annotated with @FXML, indicating that it is linked to an
     * element defined in the associated FXML file and managed by the JavaFX framework.
     */
    @FXML
    private RadioButton optionB;

    /**
     * Represents the third selectable option in a multiple-choice question.
     * This radio button allows the user to select "Option C" as their answer.
     * Used in the quiz interface managed by the controller.
     */
    @FXML
    private RadioButton optionC;

    /**
     * Represents the fourth answer option in a multiple-choice question for the quiz interface.
     * This radio button allows the user to select option D as their chosen answer.
     * This component is part of the user interface managed by the `QuestionsController` and
     * interacts with the answer group to allow only one option to be selected at a time.
     * The radio button is initialized alongside other UI components and is enabled or disabled
     * depending on the state of the quiz.
     */
    @FXML
    private RadioButton optionD;

    /**
     * Represents a ToggleGroup used for managing the mutual exclusivity of a group
     * of toggle buttons (e.g., RadioButtons) for answer selection in the quiz interface.
     * This ensures that only one answer option can be selected at a time.
     */
    private ToggleGroup answerGroup;


    /**
     *
     */
    private List<Word> wordList;

    /**
     * Represents the text of the correct answer for the current question.
     * This variable is used to store and compare the correct answer against
     * the user's selected answer during the quiz.
     */
    private String correctAnswerText;

    /**
     * Represents the unique identifier for a specific document used in the context
     * of quiz management or document-related operations within the application.
     * This variable is primarily used to associate actions, such as loading or
     * processing, with the correct document based on its assigned ID. The document
     * ID is expected to be a unique integer value provided by the system or user.
     */
    private int documentId;

    /**
     * Manages the current state of the game session.
     * This field holds a reference to the singleton instance of {@code GameSessionManagement},
     * responsible for coordinating the overall game session's lifecycle, retrieving session data,
     * and maintaining session-specific details.
     * The {@code session} instance is used across various methods in the {@code QuestionsController}
     * class to manage and interact with the game session, such as retrieving the session's current
     * state and applying session-specific configurations for the quiz.
     */
    private final service.GameSessionManagement session = service.GameSessionManagement.getInstance();


    /**
     * Represents the maximum number of questions that will be displayed or asked
     * during the quiz session. This value limits the total questions a user can
     * encounter in a single session of the quiz.
     */
    private int maxQuestions;

    /**
     * A {@link PauseTransition} instance used to manage the timing of individual questions in the quiz.
     * This timer is responsible for controlling the duration allotted for answering each question and can trigger specific actions when the time expires.
     */
    private PauseTransition questionTimer;

    /**
     * Represents the duration of time allowed for answering a single question in the quiz.
     * This variable is used to control the timeout interval, ensuring the user has a limited amount of time to respond to each question.
     */
    private Duration questionTimeout;

    /**
     * Represents a JavaFX Timeline used for managing and animating
     * the countdown timer during the quiz. This Timeline is responsible
     **/
    private Timeline countdownTimeline;

    /**
     * Holds the remaining time in seconds for the current quiz or question.
     * This variable is primarily used to track and manage the countdown timer that
     * limits the time a user can take to answer a question or complete the quiz.
     * It is dynamically updated to reflect the remaining duration in real-time.
     */
    private int remainingTimeSeconds;

    /**
     * Initializes the controller's components and settings.
     * This method sets up the `ToggleGroup` for the answer options (optionA, optionB, optionC, optionD)
     * to ensure that only one option can be selected at a time.
     * It is automatically called during the loading of the FXML file.
     */
    @FXML
    public void initialize() {
        answerGroup = new ToggleGroup();
        Stream.of(optionA, optionB, optionC, optionD).forEach(rb -> rb.setToggleGroup(answerGroup));
    }

    /**
     * Starts the game by initializing the necessary configurations for the session
     * and loading the first question. It retrieves the words based on the provided
     * document ID and determines the difficulty level to set the question timeout.
     * If the document ID is invalid or there are not enough words, it provides
     * appropriate feedback to the user.
     *
     * @param documentId the ID of the document used to load the words for the game
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

        switch (difficulty.toUpperCase()) {
            case "EASY":
                questionTimeout = Duration.seconds(30);
                break;
            case "MEDIUM":
                questionTimeout = Duration.seconds(20);
                break;
            case "HARD":
                questionTimeout = Duration.seconds(10);
                break;
        }

        loadNextQuestion(maxQuestions);
    }

    /**
     * Disables user interaction with key UI elements.
     * This method prevents interaction by disabling the following UI components:
     * - The "Submit" button
     * - The "Skip" button
     * - Answer options (optionA, optionB, optionC, and optionD)
     * Typically, this method is used to prevent user interaction during actions
     * such as processing an answer or transitioning to the next question.
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
     * Loads the next question in the quiz session or concludes the session if all questions
     * have been answered or the timer expires.
     *
     * This method:
     * - Resets the feedback label and prepares the UI for a new question.
     * - Checks if the maximum number of questions has been answered:
     *   - If all questions are answered, displays the final score, saves the session,
     *     disables interaction, and concludes the quiz.
     *   - Otherwise, it generates and displays the next question and its options.
     * - Handles the countdown timer for the question and updates the timer label.
     * - Processes the event when the timer expires, providing feedback and scheduling
     *   the loading of the next question.
     *
     * @param maxQuestions the total number of questions allowed in the session.
     */
    private void loadNextQuestion(int maxQuestions) {
        feedbackLabel.setStyle("-fx-text-fill: black;");
        feedbackLabel.setText("");

        if (session.getQuestionsAnswered() >= maxQuestions) {
            feedbackLabel.setText("Hai completato il quiz! Punteggio: " + session.getScore());
            session.saveSession();
            disableInteraction();
            concludeQuiz();
            return;
        }

        service.Question question = service.QuestionGenerator.generateNextQuestion(wordList, session.getQuestionsAnswered());

        questionLabel.setText(question.getQuestionText());
        correctAnswerText = question.getCorrectAnswer();

        setOptions(question.getOptions());

        answerGroup.selectToggle(null);

        if (questionTimer != null) {
            questionTimer.stop();
        }

        remainingTimeSeconds = (int) questionTimeout.toSeconds();

        countdownTimeline = new Timeline(new javafx.animation.KeyFrame(Duration.seconds(1), e -> {
            remainingTimeSeconds--;
            timerLabel.setText("Tempo Rimasto: " + remainingTimeSeconds + " secondi");
            if (remainingTimeSeconds <= 0) {
                countdownTimeline.stop();
                feedbackLabel.setStyle("-fx-text-fill: red;");
                feedbackLabel.setText("Tempo scaduto! La risposta corretta era: " + correctAnswerText);
                session.recordAnswer(false);
                setInteractionEnabled(false);

                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(ev -> {
                    setInteractionEnabled(true);
                    loadNextQuestion(maxQuestions);
                });
                pause.play();
            }
        }));
        countdownTimeline.setCycleCount(remainingTimeSeconds);
        countdownTimeline.play();

        timerLabel.setText("Tempo Rimasto: " + remainingTimeSeconds + " secondi");
        setInteractionEnabled(true);
    }

    /**
     * Sets the text for the available answer options for a question.
     * This method assigns the provided list of option texts to the respective
     * answer options (optionA, optionB, optionC, and optionD).
     *
     * @param options a list of strings representing the text for the options.
     *                 This list must contain at least four elements where:
     *                 - The first element sets the text for optionA.
     *                 - The second element sets the text for optionB.
     *                 - The third element sets the text for optionC.
     *                 - The fourth element sets the text for optionD.
     *                 If the list contains fewer than four elements, an exception may occur.
     */
    private void setOptions(List<String> options) {
        List<RadioButton> optionsButtons = Arrays.asList(optionA, optionB, optionC, optionD);
        IntStream.range(0, optionsButtons.size())
                .forEach(i -> optionsButtons.get(i).setText(options.get(i)));
    }

    /**
     * Handles the submission of an answer during the quiz.
     * This method evaluates the user's selected answer, provides appropriate feedback,
     * disables interactions until the transition to the next question, and schedules the
     * next question to be loaded after a brief delay. If the question timer is active, it is stopped.
     */
    @FXML
    private void onSubmitAnswer() {
        Optional<RadioButton> selectedOpt = Stream.of(optionA, optionB, optionC, optionD)
                .filter(RadioButton::isSelected)
                .findFirst();

        if (!selectedOpt.isPresent()) {
            feedbackLabel.setText("Seleziona una risposta prima di inviare.");
            return;
        }

        String selectedText = selectedOpt.get().getText();
        boolean isCorrect = selectedText.equals(correctAnswerText);
        session.recordAnswer(isCorrect);

        Map<Boolean, String> messages = new HashMap<>();
        messages.put(true, "Risposta corretta!");
        messages.put(false, "Risposta sbagliata. La risposta corretta era: " + correctAnswerText);

        Map<Boolean, String> styles = new HashMap<>();
        styles.put(true, "-fx-text-fill: green;");
        styles.put(false, "-fx-text-fill: red;");

        feedbackLabel.setText(messages.get(isCorrect));
        feedbackLabel.setStyle(styles.get(isCorrect));

        setInteractionEnabled(false);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            setInteractionEnabled(false);
            loadNextQuestion(maxQuestions);
        });

        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }

        pause.play();
    }

    /**
     * Enables or disables user interaction with specific UI components.
     * This method is used to control whether the user can interact with
     * interactive elements such as buttons and options during the quiz.
     *
     * @param enabled a boolean value indicating whether to enable or disable
     *                interaction. If set to true, elements will be enabled.
     *                If set to false, elements will be disabled.
     */
    private void setInteractionEnabled(boolean enabled) {
        List<javafx.scene.control.Control> controls = Arrays.asList(submitButton, skipButton, optionA, optionB, optionC, optionD);
        controls.forEach(c -> c.setDisable(!enabled));
    }

    /**
     * Handles the event triggered when the "Skip" button is clicked during the quiz.
     *
     * This method performs the following operations:
     * 1. Records the skipped question as not answered correctly by calling `session.recordAnswer(false)`.
     * 2. Stops the countdown timer if it is active to prevent further updates.
     * 3. Checks if the maximum number of questions (`maxQuestions`) has been answered:
     *    - If all questions are answered, displays the final score using `feedbackLabel`,
     *      saves the session using `session.saveSession()`, disables user interaction by
     *      calling `disableInteraction()`, and ends the quiz using `concludeQuiz()`.
     *    - If there are remaining questions, loads the next question by calling
     *      `loadNextQuestion(maxQuestions)`.
     */
    @FXML
    public void onSkipQuestion() {

        session.recordAnswer(false);

        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }

        if (session.getQuestionsAnswered() >= maxQuestions) {
            feedbackLabel.setText("Hai completato il quiz! Punteggio: " + session.getScore());
            session.saveSession();
            disableInteraction();
            concludeQuiz();
            return;
        }

        loadNextQuestion(maxQuestions);
    }

    /**
     * Concludes the current quiz session by displaying the result screen
     * and closing the current question window.
     * This method is responsible for:
     * - Loading the result screen from the FXML file.
     * - Passing the number of correct answers to the ResultsController.
     * - Creating and displaying a new stage to show the results.
     * - Closing the current stage that hosts the quiz questions.
     * - Displaying an error message through `feedbackLabel` in case of an exception.
     * The method interacts with the `ResultsController` to set the correct
     * answers for the session and leverages JavaFX components like `FXMLLoader`
     * and `Stage` for UI management.
     * Any errors encountered during the loading or display of the result
     * screen are caught and handled by logging the exception and updating
     * the `feedbackLabel` with a user-facing error message.
     */
    private void concludeQuiz() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ResultView.fxml"));
            Parent root = loader.load();
            ResultsController resultsController = loader.getController();
            resultsController.setCorrectAnswers(session.getCorrectAnswers());
            resultsController.setTotalAnswers(session.getQuestionsAnswered());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Risultati del Quiz");
            stage.show();

            Stage currentStage = (Stage) questionLabel.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the document ID associated with the current session or configuration.
     *
     * @return the document ID as an integer
     */
    public int getDocumentId() {
        return documentId;
    }

    /**
     * Sets the document ID for the session or configuration.
     *
     * @param id the ID of the document to be assigned
     */
    @FXML
    public void setDocumentId(int id) {
        this.documentId = id;
    }
}
