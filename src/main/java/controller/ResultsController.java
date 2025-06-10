package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The ResultsController class manages the behavior and interaction logic for
 * the results view in the application. It interacts with the associated FXML
 * components to display the user's performance and handles navigation actions.
 * This controller is responsible for updating the UI based on the number of
 * correct answers and navigating back to the main menu when prompted.
 *
 * @author Gruppo6
 */
public class ResultsController {

    /**
     * A label that displays the number of correct answers achieved by the user.
     * This UI component is updated dynamically based on the user's performance
     * during the session. It is initialized and manipulated by the ResultsController.
     */
    @FXML
    private Label correctCountLabel;

    /**
     * Label used to display feedback messages to the user.
     * This label is updated dynamically to provide relevant
     * information or responses based on user interactions
     * or application state changes.
     */
    @FXML
    private Label feedbackMessage;

    /**
     * Tracks the number of correct answers provided by the user during a session.
     * This variable is used to dynamically update the results displayed in the UI
     * and to provide feedback on the user's performance.
     */
    private int correctAnswers;

    /**
     * Represents the total number of answers provided during a quiz or assessment.
     * This variable is used to keep track of all submitted answers, regardless of their correctness.
     * It can be used for calculating metrics such as accuracy or completion.
     */
    private int totalAnswers;

    /**
     * Represents the button located at the top of the "Home" section in the ResultsController's
     * user interface. This button is typically linked to actions or navigation related to
     * returning to or interacting with the Home view of the application.
     *
     * This variable is injected using the FXMLLoader and is associated with the corresponding
     * UI component defined in the FXML file.
     */
    @FXML
    private Button btnHomeTop;

    /**
     * The btnHome variable represents a Button UI element in the ResultsController.
     * This button is associated with the action of navigating back to the main menu
     * or home screen from the results view in the application. It is linked to its
     * corresponding element in the FXML file using the @FXML annotation.
     */
    @FXML
    private Button btnHome;

    /**
     * Represents a button in the UI that allows the user to retry the quiz.
     *
     * This button is linked to an event handler that navigates the user back
     * to the quiz interface when clicked. It provides a mechanism for the user
     * to restart the quiz and attempt to improve their performance.
     *
     * The button's behavior and appearance are defined and managed within the
     * ResultsController class.
     */
    @FXML
    private Button btnRetry;

    /**
     * Represents the UI button for navigating to the "Personal Scores" view in the application.
     * This button is tied to an FXML-defined element and allows users to access their
     * personal score records when clicked.
     *
     * The associated action method, `onPersonalScores(ActionEvent event)`, is responsible
     * for handling the logic necessary to transition to the relevant view or perform
     * the required operations related to personal scores.
     */
    @FXML
    private Button btnPersonalScores;

    /**
     * Initializes the ResultsController by setting up initial configurations
     * for the UI components associated with this controller.
     *
     * This method is automatically called after the FXML file has been loaded.
     * Specifically, it updates the `correctCountLabel` text to display the
     * initial number of correct answers.
     */
    private void initialize() {

        correctCountLabel.setText("Risposte corrette: " + correctAnswers + " su " + totalAnswers + " domande totali.");
    }

    /**
     * Updates the number of correct answers and refreshes the associated UI label
     * to reflect the updated value, if applicable.
     *
     * @param correctAnswers the number of correct answers to be set and displayed
     */
    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
        if (correctCountLabel != null) {
            correctCountLabel.setText("Risposte corrette: " + correctAnswers + " su " + totalAnswers + " domande totali.");
        }
    }

    /**
     * Updates the total number of answers and refreshes the associated UI label
     * to display the updated total and correct answers, if applicable.
     *
     * @param totalAnswers the total number of answers to be set and displayed
     */
    public void setTotalAnswers(int totalAnswers) {
        this.totalAnswers = totalAnswers;
        if (correctCountLabel != null) {
            correctCountLabel.setText("Risposte corrette: " + correctAnswers + " su " + totalAnswers + " domande totali.");
        }
    }

    /**
     * Handles the action of retrying the quiz. This method is triggered when the user interacts
     * with the "Retry Quiz" button in the UI. It loads the quiz view FXML file, initializes the
     * question controller with the current game session, and updates the application's stage
     * to display the quiz interface.
     *
     * @param event the action event triggered by the user's interaction with the "Retry Quiz" button
     */
    @FXML
    private void onRetryQuiz(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/quizView.fxml"));
            Parent root = loader.load();

            QuestionsController questionsController = loader.getController();

            questionsController.startGame(service.GameSessionManagement.getInstance().getDocumentId());

            Stage stage = (Stage) feedbackMessage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Quiz");
            stage.show();
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento della schermata del quiz: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the action of displaying the personal scores view. This method is triggered
     * when the user interacts with the "Personal Scores" button in the UI. It loads the
     * PersonalScoresView.fxml file, sets it as the current scene, and displays it in the
     * existing stage.
     *
     * @param event the action event triggered by the user's interaction with the
     *              "Personal Scores" button. Used to identify the source component
     *              and retrieve the current stage.
     */
    @FXML
    private void onPersonalScores(ActionEvent event) {
        loadView(event, "/view/PersonalScoresView.fxml", "Personal Scores");
    }

    /**
     * Handles the action of navigating back to the main menu view when triggered.
     * This method is linked to the "Back to Menu" button in the UI and is responsible
     * for loading the UserManagementView.fxml file, setting it as the current scene, and
     * displaying it in the existing stage.
     *
     * @param event the action event triggered by the user's interaction with the "Back to Menu" button
     *              in the UI. Used to identify the source component and retrieve the current stage.
     */
    @FXML
    private void onBackToMenu(ActionEvent event) {
        loadView(event, "/view/UserManagementView.fxml", "Menu Principale");
    }

    private void loadView(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento della schermata: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
