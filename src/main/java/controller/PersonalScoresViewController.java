package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import service.DocumentsManagement;

/**
 * Handles the event of navigating back to the main menu.
 * Typically, invoked when the "Back to Menu" button is clicked.
 * Calls the {@code changeWindow()} method in the {@code ParentMenu} class
 * to switch to the appropriate scene or window representing the main menu.
 */
public class PersonalScoresViewController implements Initializable {

    /**
     * Represents a button in the PersonalScoresViewController interface that allows
     * the user to navigate back to the main menu. This button is associated with
     * the {@code handleBackToMenu} method, which is triggered when the button is clicked.
     *
     * It serves as a user interface element for transitioning away from the
     * personal scores view to the main menu screen.
     */
    @FXML
    private Button backToMenuButton;

    /**
     * An enumeration that represents sorting modes used within the
     * PersonalScoresViewController. The sorting can be based on either
     * the score of entries or the date.
     *
     * This enum is primarily used to define the sorting behavior
     * when displaying and organizing data in the associated tables.
     */
    private enum SortMode{
        BY_SCORE, BY_DATE
    }

    /**
     * Represents the current sorting mode used within the PersonalScoresViewController.
     * The sorting can be either by score or by date, as defined by the SortMode enumeration.
     *
     * This variable determines how game session data is organized and displayed
     * in the tables associated with different difficulty levels. It is dynamically
     * updated based on user interactions, such as clicking the sort button.
     */
    private SortMode currentSort = SortMode.BY_SCORE;

    /**
     * The sortButton is a private instance variable annotated with {@code @FXML},
     * representing a button in the user interface responsible for triggering the sorting functionality
     * of game session data displayed in the score tables. Interaction with this button
     * typically initiates the process of toggling between sorting modes, such as sorting
     * by score or by date.
     */
    @FXML
    private Button sortButton;

    /**
     * Represents a TabPane component used to manage and display multiple tabs
     * corresponding to different difficulty levels (e.g., Easy, Medium, Hard).
     * Each tab can contain a TableView that displays the game sessions and scores
     * for the associated difficulty level. This UI element forms part of the
     * PersonalScoresViewController interface, allowing users to navigate between
     * the different difficulty-specific score tables.
     *
     * The `difficultyTabs` component is linked to the FXML file via the `@FXML` annotation
     * and is automatically loaded during the initialization of the controller.
     */
    @FXML
    private TabPane difficultyTabs;

    /**
     * Represents the "Easy" tab in the difficulty selection interface of the PersonalScoresViewController.
     * This tab allows the user to view and interact with the game session data filtered by the "Easy" difficulty level.
     * It is part of a tab pane that organizes game scores by difficulty.
     */
    @FXML
    private Tab easySelector;

    /**
     * Represents a table view displaying game session data for the "Easy" difficulty level.
     * This table is part of the user interface controlled by the {@code PersonalScoresViewController}.
     *
     * The {@code easyTable} is populated with instances of {@code service.GameSession} representing
     * the game sessions completed on the easy difficulty. Each row corresponds to a single session,
     * displaying data such as the session's title, score, and timestamp. The table supports sorting
     * and interaction functionalities defined in the controller.
     *
     * FXML injection is used for this field to link it to the corresponding TableView component
     * defined in the FXML file loaded by {@code PersonalScoresViewController}.
     */
    @FXML
    private TableView<service.GameSession> easyTable;

    /**
     * Represents the table column in the "Easy" difficulty score table that displays
     * the titles of documents associated with game sessions.
     *
     * This column is configured to extract and display the title information
     * from the {@link service.GameSession} object, specifically as a String value.
     * The title corresponds to the document associated with the game session.
     *
     * It is part of the user interface managed by the {@code PersonalScoresViewController} class
     * and is used to present data in a structured tabular format for "Easy" difficulty.
     */
    @FXML
    private TableColumn<service.GameSession, String> easyTitleColumn;

    /**
     * Represents the "Score" column in the table displaying game sessions for the "Easy" difficulty level.
     * This column is used to display the scores achieved by players during game sessions categorized under the "Easy" level.
     *
     * The column fetches and displays integer values corresponding to the `score` property of
     * {@link service.GameSession} objects.
     *
     * This field is marked with the `@FXML` annotation to indicate its association with the UI layout
     * defined in the corresponding FXML file for the PersonalScoresViewController.
     */
    @FXML
    private TableColumn<service.GameSession, Integer> easyScoreColumn;

    /**
     * Represents the table column in the "Easy" difficulty score table that displays
     * the date and time associated with each game session.
     *
     * Each cell in this column reflects the timestamp of a specific game session,
     * indicating when the session took place. Data for this column is sourced from
     * the `timestamp` field of the corresponding {@link service.GameSession} objects.
     */
    @FXML
    private TableColumn<service.GameSession, String> easyDateColumn;

    /**
     * Represents the Tab element for the "Medium" difficulty level within the user interface.
     * This tab allows users to navigate to and view the medium difficulty scores.
     * It is part of the difficulty-based tab navigation system in the score view.
     */
    @FXML
    private Tab mediumSelector;

    /**
     * Represents the table view responsible for displaying game session data
     * filtered by the "Medium" difficulty level within the application's user interface.
     * Each entry in the table corresponds to a {@link service.GameSession} object.
     *
     * This table is dynamically populated with "Medium" difficulty game session data
     * through the application's logic and supports features such as sorting and
     * interaction as configured in the controller.
     *
     * The table is primarily managed by the {@code PersonalScoresViewController} class
     * and interacts with methods focused on loading, sorting, and displaying game sessions
     * based on the selected difficulty.
     */
    @FXML
    private TableView<service.GameSession> mediumTable;

    /**
     * Represents a TableColumn in the user interface responsible for displaying the title
     * of documents associated with game sessions present in the "Medium" difficulty level table.
     *
     * This column is configured to display string values fetched using the document ID from the
     * game session data. It is part of the mediumTable and is populated dynamically when the
     * medium difficulty score table is displayed.
     *
     * Used by methods such as {@code setupTableColumns} to define cell value factories
     * for proper data rendering and is populated with data from {@code mediumSessions}.
     *
     * Data displayed in this column is associated with {@link service.GameSession} entities.
     */
    @FXML
    private TableColumn<service.GameSession, String> mediumTitleColumn;

    /**
     * Represents the TableColumn for displaying the score of game sessions with a "Medium" difficulty level.
     * This column is configured to show the integer score values stored in the {@link service.GameSession} objects.
     * It is part of the mediumTable in the PersonalScoresViewController, which displays
     * all medium-difficulty game session data.
     */
    @FXML
    private TableColumn<service.GameSession, Integer> mediumScoreColumn;

    /**
     * Represents the table column used for displaying the date information of game sessions
     * within the "Medium" difficulty score table in the user interface. It is configured
     * to handle and display date-related data for game sessions belonging to the medium
     * difficulty level.
     *
     * The column values are expected to show the timestamp associated with each game session,
     * reflecting when the session took place. This information is bound to the `timestamp`
     * property of the {@link service.GameSession} object.
     *
     * This field is annotated with {@code @FXML} to denote that it is defined in the associated
     * FXML file and is injected at runtime by the JavaFX framework.
     */
    @FXML
    private TableColumn<service.GameSession, String> mediumDateColumn;

    /**
     * Represents the Tab component used for displaying the "Hard" difficulty level score table.
     * Associated with the "Hard" difficulty data, this tab provides an interface for
     * viewing game session information, such as scores, titles, and timestamps, for
     * sessions played at the hardest level of the game.
     *
     * This Tab is part of the difficultyTabs in the PersonalScoresViewController and,
     * when selected, interacts with the hardTable to show the corresponding data.
     */
    @FXML
    private Tab hardSelector;

    /**
     * Represents the table view used to display game session data for the "Hard" difficulty level.
     * This table is populated with instances of {@link service.GameSession}, containing details
     * such as the document title, score, and date for game sessions categorized as "Hard".
     * The data is dynamically loaded, displayed, and sorted within the table based on user interactions.
     */
    @FXML
    private TableView<service.GameSession> hardTable;

    /**
     * Represents the "Title" column in the "Hard" difficulty level table within the personal scores view.
     * This column displays the document titles associated with game sessions stored in the hard difficulty category.
     * The column is bound to the title property of the {@code GameSession} class.
     */
    @FXML
    private TableColumn<service.GameSession, String> hardTitleColumn;

    /**
     * Represents the table column for displaying the 'Hard' difficulty level scores in the user interface.
     * This column is part of the `hardTable` in the `PersonalScoresViewController` and is bound to the
     * `score` property of the `GameSession` class. It displays the integer scores achieved by players
     * during sessions set at the "Hard" challenge level.
     *
     * The column is configured as part of the table setup to retrieve and display score-related data
     * for game sessions with "Hard" difficulty, providing a visual representation within the interface.
     */
    @FXML
    private TableColumn<service.GameSession, Integer> hardScoreColumn;

    /**
     * Represents the "Date" column in the table displaying game session data for the "Hard" difficulty level.
     * This column is configured to display the timestamp of each game session, formatted as a string.
     *
     * It is associated with the hard difficulty table and is populated with data from the {@code hardSessions} list.
     * The timestamps correspond to the date and time when the sessions occurred.
     */
    @FXML
    private TableColumn<service.GameSession, String> hardDateColumn;

    /**
     * Stores a list of game sessions specifically filtered or categorized under the "Easy" difficulty level.
     * Each session in the list is represented by an instance of {@code service.GameSession}.
     * The list is observable, allowing the user interface to automatically reflect changes to the list.
     * It is used within the context of the {@code PersonalScoresViewController} for managing and displaying
     * game session data related to the "Easy" difficulty in the appropriate score table.
     */
    private ObservableList<service.GameSession> easySessions = FXCollections.observableArrayList();

    /**
     * A collection of observable game sessions corresponding to the "Medium" difficulty level.
     * This list is used to manage and display medium-difficulty game session data within the user interface.
     *
     * The list is initialized as an empty observable array list and may be dynamically populated
     * with instances of {@code service.GameSession} depending on the application's data fetching and display logic.
     */
    private ObservableList<service.GameSession> mediumSessions = FXCollections.observableArrayList();

    /**
     * An observable list of game sessions filtered by the "Hard" difficulty level.
     * This list is specifically designed to store and manage GameSession objects
     * representing game sessions played at the "Hard" difficulty.
     * The data is dynamically managed, allowing for real-time updates and
     * reflecting changes in the underlying data set.
     *
     * This field is used primarily to populate and display the "Hard" difficulty
     * score table in the user interface.
     */
    private ObservableList<service.GameSession> hardSessions = FXCollections.observableArrayList();

    /**
     * Initializes the controller class and sets up the required configurations for the
     * user interface, such as configuring table columns, loading data from the database,
     * displaying initial score tables, and applying sorting to all tables.
     *
     * @param url the location used to resolve relative paths for the root object, or null if unknown.
     *
     * @param rb  the resource bundle that supplies localization features for this controller or null if not needed.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns(easyTitleColumn, easyScoreColumn, easyDateColumn);
        setupTableColumns(mediumTitleColumn, mediumScoreColumn, mediumDateColumn);
        setupTableColumns(hardTitleColumn, hardScoreColumn, hardDateColumn);

        loadDataFromDatabase();

        showEasyScore(null);
        showMediumScore(null);
        showHardScore(null);

        applySortToAllTables();
    }

    /**
     * Configures the cell value factories for the specified table columns to properly
     * display game session data, including the document title, score, and date.
     *
     * @param titleCol the table column that will display the title of the document
     *                 associated with the game session. The title is fetched using
     *                 the document ID from the game session.
     * @param scoreCol the table column that will display the score associated with
     *                 the game session. The score is directly retrieved as an integer value.
     * @param dateCol  the table column that will display the timestamp of the game
     *                 session, reflecting the date and time it occurred.
     */
    private void setupTableColumns(TableColumn<service.GameSession, String> titleCol,
                                   TableColumn<service.GameSession, Integer> scoreCol,
                                   TableColumn<service.GameSession, String> dateCol) {

        titleCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(DocumentsManagement.getTitleFromId(cellData.getValue().getDocumentID())));

        scoreCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getScore()).asObject());

        dateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTimestamp()));
    }

    /**
     * Loads data from the database by executing a predefined SQL query that retrieves
     * document titles, associated scores, and difficulty levels of game sessions.
     * The data is fetched by joining the "sessions" and "documents" tables, and it
     * is returned in sorted order based on document titles and difficulty levels.
     * The method establishes a connection to the database using the {@code DatabaseManagement.getConnection()}
     * method, executes the query, and iterates over the result set to process the retrieved data.
     * Any exceptions encountered during the database operation, such as SQL errors, are caught,
     * and corresponding error messages are printed to the standard error output.
     * Throws:
     * - {@link SQLException} indirectly, if any database access problem occurs during the operation.
     */
    private void loadDataFromDatabase() {
        String username = service.GameSessionManagement.getInstance().getUsername();
        List<service.GameSession> userSessions = service.GameSessionManagement.getSessionsByUsername(username);

        if (userSessions.isEmpty()) {
            System.err.println("Nessuna sessione trovata.");
            return;
        }

        userSessions.stream().forEach(session -> {
            switch (session.getDifficulty()) {
                case "EASY":
                    easySessions.add(session);
                    break;
                case "MEDIUM":
                    mediumSessions.add(session);
                    break;
                case "HARD":
                    hardSessions.add(session);
                    break;
            }
        });
    }

    /**
     * Applies the current sorting mode to all difficulty tables: easy, medium, and hard.
     * Clears existing sorting criteria for each table and re-applies sorting based on
     * the specified {@code currentSort} mode. The sorting can be applied either
     * by score or by date depending on the value of {@code currentSort}.
     *
     * This method delegates the actual sorting logic to the {@code applySort} method,
     * which is invoked separately for each difficulty table with their respective
     * score and date columns.
     */
    private void applySortToAllTables() {
        applySort(easyTable, easyScoreColumn, easyDateColumn);
        applySort(mediumTable, mediumScoreColumn, mediumDateColumn);
        applySort(hardTable, hardScoreColumn, hardDateColumn);
    }

    /**
     * Applies sorting to the given table based on the current sorting mode.
     * Clears existing sorting criteria and sets a sort order using the specified
     * score or date column depending on the value of {@code currentSort}.
     * Finally, triggers sorting on the table.
     *
     * @param table    the table view to which the sorting is applied. It displays
     *                 game session data and is sorted by the specified criteria.
     * @param scoreCol the table column representing the score of game sessions.
     *                 This column is used when sorting by score.
     * @param dateCol  the table column representing the date of game sessions.
     *                 This column is used when sorting by date.
     */
    private void applySort(TableView<service.GameSession> table,
                           TableColumn<service.GameSession, Integer> scoreCol,
                           TableColumn<service.GameSession, String> dateCol) {
        table.getSortOrder().clear();
        table.getSortOrder().add(currentSort == SortMode.BY_SCORE ? scoreCol : dateCol);
        table.sort();
    }

    /**
     * Handles the action of changing the sorting mode for displaying game session data.
     * Toggles between sorting by score and sorting by date based on the current state.
     * After updating the sorting mode, the method applies the updated sorting to all
     * difficulty-related tables by using the {@code applySortToAllTables} method.
     *
     * @param event the action event triggered when the user interacts with the sorting control,
     *              such as clicking the sort button.
     */
    @FXML
    private void handleChangeSort(ActionEvent event) {
        currentSort = (currentSort == SortMode.BY_SCORE) ? SortMode.BY_DATE : SortMode.BY_SCORE;
        applySortToAllTables();
    }

    /**
     * Handles the action of navigating back to the main menu.
     * Loads the UserManagementView FXML file, sets it as the current scene,
     * and displays it in the primary stage.
     *
     * @param event the action event triggered when the back-to-menu button is clicked.
     */
    @FXML
    private void handleBackToMenu(ActionEvent event) {
        loadView(event, "/view/MainMenu.fxml");
    }

    /**
     * Displays the score table for the "Easy" difficulty level.
     * Populates the easyTable with game session data stored in easySessions,
     * if the table is not null.
     *
     * @param event the event that triggers this action, typically initiated
     *              by the user interacting with the interface.
     */
    @FXML
    private void showEasyScore(Event event) {
        if (easyTable != null) easyTable.setItems(easySessions);
    }

    /**
     * Displays the score table for the "Medium" difficulty level.
     * Populates the mediumTable with game session data stored in mediumSessions,
     * if the table is not null.
     *
     * @param event the event that triggers this action, typically initiated
     *              by the user interacting with the interface.
     */
    @FXML
    private void showMediumScore(Event event) {
        if (mediumTable != null) mediumTable.setItems(mediumSessions);
    }

    /**
     * Displays the score table for the "Hard" difficulty level.
     * Populates the hardTable with game session data stored in hardSessions,
     * if the table is not null.
     *
     * @param event the event that triggers this action, typically initiated
     *              by the user interacting with the interface.
     */
    @FXML
    private void showHardScore(Event event) {
        if (hardTable != null) hardTable.setItems(hardSessions);
    }

    private void loadView(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
