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
     * Initializes the ResultsController by setting up initial configurations
     * for the UI components associated with this controller.
     *
     * This method is automatically called after the FXML file has been loaded.
     * Specifically, it updates the `correctCountLabel` text to display the
     * initial number of correct answers.
     */
    @FXML
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserManagementView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
            // Log dell'errore
            System.err.println("Errore durante il caricamento della schermata del quiz: " + e.getMessage());
            e.printStackTrace(); // Può essere sostituito con un logger
        }
    }


}
