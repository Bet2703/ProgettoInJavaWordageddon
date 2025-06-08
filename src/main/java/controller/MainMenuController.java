package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

/**
 * The MainMenuController class is responsible for managing the main menu of the application.
 * It handles user interactions for various menu buttons, such as navigating to the level selection view,
 * viewing the leaderboard, or exiting the application.
 *
 * @author Gruppo6
 */
public class MainMenuController {

    /**
     * Handles the action triggered when the play button is clicked.
     * This method transitions the application to the level selection screen.
     * It loads the "Level.fxml" file, sets up a new scene, and updates the stage with the new view.
     *
     * @param event the ActionEvent triggered by the user interaction, such as clicking the play button.
     * @throws IOException if the "Level.fxml" file cannot be loaded.
     */
    @FXML
    public void onPlayClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Level.fxml"));
        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Seleziona Difficoltà");
        stage.show();
    }

    /**
     * Handles the action triggered when the profile button is clicked.
     * This method transitions the application to the user profile management screen.
     * It loads the "UserManagementView.fxml" file, sets up a new scene, and updates the stage with the new view.
     *
     * @param event the ActionEvent triggered by the user interaction, such as clicking the profile button.
     * @throws IOException if the "UserManagementView.fxml" file cannot be loaded.
     */
    @FXML
    public void onProfileClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UserManagementView.fxml"));
        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Profilo");
        stage.show();
    }

    /**
     * Handles the action triggered when the leaderboard button is clicked.
     * This method transitions the application to the leaderboard screen.
     * It loads the "LeaderboardView.fxml" file, sets up a new scene, and updates the stage with the new view.
     *
     * @param event the ActionEvent triggered by the user interaction, such as clicking the leaderboard button.
     * @throws IOException if the "LeaderboardView.fxml" file cannot be loaded.
     */
    @FXML
    public void onLeaderboardClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PersonalScoresView.fxml"));
        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("PersonalScores");
        stage.show();
    }

    /**
     * Handles the action triggered when the exit button is clicked.
     * This method terminates the application by invoking the `System.exit` method,
     * which ensures that the program is closed immediately.
     */
    @FXML
    public void onExitClicked() {
        System.exit(0);
    }
}
