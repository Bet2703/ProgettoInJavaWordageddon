package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.DatabaseManagement;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The DocumentReadController class is responsible for managing the document reading functionality
 * of the application, including text display, timing, and navigation to a subsequent quiz phase.
 *
 * This controller interacts with UI elements defined in the associated FXML file and
 * facilitates document retrieval based on difficulty, timer management, and scene transitions.
 *
 * @author Gruppo6
 */
public class DocumentReadController {

    /**
     * The TextArea component used for displaying the document text within the
     * DocumentReadController.
     *
     * This field is bound to the corresponding UI element in the FXML layout file.
     * It allows the application to display the content of a document for the user
     * to read within the application's interface. The content of this field
     * is typically set programmatically by loading document data.
     */
    @FXML
    private TextArea documentTextArea;

    /**
     * Represents the label used to display the timer in the reading context.
     * This label is updated in real-time to show the remaining time during a reading session.
     * It is controlled and modified programmatically through associated methods in the
     * `DocumentReadController` class.
     */
    @FXML
    private Label timerLabel;

    /**
     * Represents the unique identifier for a document within the system.
     * This variable is used to track and manage a specific document entity
     * in operations performed by the controller.
     */
    private int documentId;

    /**
     * Represents the timeline that manages the reading timer for the document viewer.
     * This object controls the scheduling and execution of timed tasks related to
     * the countdown and state transitions within the context of the `DocumentReadController`.
     */
    private Timeline timeline;

    /**
     * Represents the remaining time in seconds for the user to complete a task or interaction
     * within the application. This variable is primarily used to manage and display a countdown
     * timer in the reading phase of the application.
     *
     * The default value is initialized to 30 seconds. This value may be decremented during
     * runtime as the timer elapses.
     */
    private int secondsLeft = 30;

    /**
     * Represents the difficulty level of a document or task.
     * This variable is used to differentiate or categorize content based on
     * its difficulty, such as "Easy", "Medium", or "Hard".
     * It is a private field accessible and modifiable through appropriate methods.
     */
    private String difficulty;


    /**
     * Initializes the DocumentReadController.
     *
     * This method is invoked automatically when the associated FXML file is loaded.
     * It retrieves the selected difficulty level using LevelsController.getDifficulty,
     * fetches a random document text based on the difficulty level, and displays
     * the text in the associated text area. The reading timer is then started.
     */
    @FXML
    public void initialize(){
        String text = fetchRandomDocumentByDifficulty();
        documentTextArea.setText(text);
        startTimer();
    }

    /**
     * Starts a countdown timer and updates the timer label with the time remaining.
     *
     * This method initializes a `Timeline` object that decreases the value of `secondsLeft` each second
     * and updates the `timerLabel` to reflect the remaining time. Once the time reaches zero, the timer
     * stops, the label displays a "time's up" message, and the application transitions to a question
     * view by invoking the `goToQuestionsView` method.
     *
     * The duration of the countdown is determined by the `secondsLeft` field, and the timeline is
     * configured to run for a number of cycles equal to this value.
     */
    private void startTimer() {
        timerLabel.setText("Tempo restante: " + secondsLeft + "s");
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsLeft--;
            timerLabel.setText("Tempo restante: " + secondsLeft + "s");

            if (secondsLeft <= 0) {
                timeline.stop();
                timerLabel.setText("Tempo scaduto!");
                goToQuestionsView();
            }
        }));
        timeline.setCycleCount(secondsLeft);
        timeline.play();
    }

    /**
     * Opens the quiz view scene and transitions the application to the question interface.
     *
     * This method is responsible for loading the `quizView.fxml` file using the `FXMLLoader`,
     * setting it as the root node of a new scene, and opening it in a new stage titled "Domande".
     * The `QuestionsController` associated with the view is retrieved, and the `documentId`
     * field is passed to it. The `startGame` method of the controller is invoked to initialize
     * the quiz game with the current document's ID.
     *
     * Additionally, the current window, associated with the reading document interface, is closed
     * after transitioning to the quiz view. If an error occurs while loading the FXML or transitioning
     * to the new scene, the exception is caught, logged to the console, and its stack trace is printed.
     *
     * Exceptions:
     * - Prints an error message and the stack trace if an `IOException` occurs while loading the FXML.
     */
    private void goToQuestionsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/quizView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Domande");
            stage.setScene(new Scene(root));
            stage.show();

            QuestionsController controller = loader.getController();
            Consumer<Integer> startGame = controller::startGame;
            controller.setDocumentId(documentId);
            startGame.accept(documentId);

            System.out.println("L'id del Testo nel DB e': " + controller.getDocumentId());

            Optional.ofNullable(documentTextArea.getScene())
                    .map(Scene::getWindow)
                    .map(Stage.class::cast)
                    .ifPresent(Stage::close);

        } catch (IOException e) {
            System.err.println("Errore nell'apertura della scena: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Fetches a random document from the database based on the specified difficulty level.
     *
     * This method executes a SQL query to retrieve one random document with the given difficulty
     * from a "documents" table. The text content of the document is then returned. If no document
     * is found, an empty string is returned. In case of any SQL errors, the exception details are
     * printed to the console.
     *
     * @return the text content of the selected document, or an empty string if no document is found
     */
    private String fetchRandomDocumentByDifficulty() {
        String query = "SELECT id, text, difficulty FROM documents WHERE difficulty = ? ORDER BY RANDOM() LIMIT 1";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, LevelsController.getDifficulty());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    documentId = rs.getInt("id");
                    difficulty = rs.getString("difficulty");
                    secondsLeft = service.Levels.getSecondsByDifficulty(difficulty.toUpperCase());
                    return rs.getString("text");
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore nella connessione/query col database: " + e.getMessage());
            e.printStackTrace();
        }

        return "";
    }
}
