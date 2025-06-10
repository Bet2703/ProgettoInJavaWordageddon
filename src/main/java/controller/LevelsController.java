package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Controller class for managing the difficulty selection screen.
 * This class handles user interactions, such as selecting a difficulty level,
 * starting the game, and navigating back to the login screen.
 *
 * @author Gruppo6
 */
public class LevelsController {

    /**
     * Represents the "Easy" difficulty level selection RadioButton in the LevelsController.
     * This button is part of the difficulty selection group and allows the user to
     * choose the "Easy" mode for the game.
     *
     * The state of this RadioButton can be used to determine the selected difficulty level
     * and is linked to the corresponding FXML UI component via the @FXML annotation.
     */
    @FXML
    private RadioButton rbEasy;

    /**
     * A RadioButton representing the medium difficulty option in the level selection screen.
     * This control is part of a group of RadioButtons that allows the user to choose
     * a difficulty level for the game. The selection is used to determine the difficulty
     * of the quiz or game being initiated.
     *
     * Associated logic for this control includes handling the user's choice
     * and updating the application state accordingly to reflect the selected
     * difficulty level.
     */
    @FXML
    private RadioButton rbMedium;

    /**
     * Represents the RadioButton UI component corresponding to the "Hard" difficulty selection.
     *
     * This button allows the user to select the "Hard" difficulty level in the game's settings.
     * It is part of the difficulty selection group managed by the LevelsController class
     * and is associated with the difficultyGroup toggle group.
     *
     * Used to capture the user's preference for playing the game at the highest difficulty level.
     */
    @FXML
    private RadioButton rbHard;

    /**
     * A Label component used in the LevelsController to display messages or feedback to the user.
     * The label is dynamically updated during runtime to show information such as
     * error messages, prompts, or notifications related to user actions in the difficulty selection process.
     */
    @FXML
    private Label messageLabel;

    /**
     * Represents a ToggleGroup used for managing the selection among multiple
     * difficulty options in the LevelsController class.
     * This group typically contains radio buttons (e.g., rbEasy, rbMedium, rbHard),
     * allowing the user to select one difficulty level at a time.
     * It ensures that only one button in the group can be selected, providing
     * mutually exclusive selection behavior.
     */
    @FXML
    private ToggleGroup difficultyGroup;

    /**
     * Represents the currently selected difficulty level in the application.
     * This variable holds the value of the difficulty level chosen by the user
     * (e.g., "Easy", "Medium", "Hard") during gameplay setup. It is updated
     * when the user makes a choice and is used to determine the level of
     * challenge in the quiz or game functionality.
     */
    private static String selectedDifficulty;

    @FXML
    public void initialize() {
        difficultyGroup = new ToggleGroup();

        Stream.of(rbEasy, rbMedium, rbHard).forEach(rb -> rb.setToggleGroup(difficultyGroup));
    }

    /**
     * Handles the start of the game by retrieving the selected difficulty level,
     * updating the label with the selected level, and transitioning to the quiz view.
     * If no difficulty level is selected, a message is displayed to prompt the user.
     * If an error occurs during the loading process of the quiz view, an error message is displayed.
     *
     * @param event the ActionEvent triggered by user interaction, typically a button click
     */
    @FXML
    private void onStartGame(ActionEvent event) {
        Optional<RadioButton> selected = Optional.ofNullable((RadioButton) difficultyGroup.getSelectedToggle());

        if (selected.isPresent()) {
            RadioButton radio = selected.get();
            selectedDifficulty = mapDifficulty(radio.getText());
            messageLabel.setText("Hai selezionato il livello: " + radio.getText());

            loadScene(event, "/view/DocumentReadView.fxml",
                    () -> messageLabel.setText("Errore nel caricamento del quiz."));
        } else {
            messageLabel.setText("Seleziona un livello di difficoltà per iniziare.");
        }
    }

    /**
     * Handles the action of navigating back to the login screen.
     * This method loads the "Login.fxml" file, sets up the new scene onto the application stage,
     * and transitions the user to the login view. If an error occurs during the loading process,
     * an error message is displayed in the `messageLabel`.
     *
     * @param event the ActionEvent triggered by the user interaction, usually a button click.
     */
    @FXML
    private void onBack(ActionEvent event) {
        loadScene(event, "/view/MainMenu.fxml", () -> messageLabel.setText("Errore nel tornare al Menu principale."));
    }


    private void loadScene(ActionEvent event, String fxmlPath, Runnable onError) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            onError.run();
        }
    }


    private String mapDifficulty(String text) {
        List<String> labels = Arrays.asList("Facile", "Medio", "Difficile");
        List<String> codes = Arrays.asList("EASY", "MEDIUM", "HARD");

        return Stream.iterate(0, i -> i + 1)
                .limit(labels.size())
                .filter(i -> labels.get(i).equalsIgnoreCase(text))
                .findFirst()
                .map(codes::get)
                .orElse("EASY");  // default
    }

    /**
     * Retrieves the currently selected difficulty level.
     * This is a static method that provides access to the `selectedDifficulty`
     * variable, representing the difficulty level selected by the user.
     *
     * @return the selected difficulty level as a String
     */
    public static String getDifficulty() {
        return selectedDifficulty;
    }
}