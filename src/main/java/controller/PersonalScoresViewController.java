package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import service.DatabaseManagement;
import service.GameSessionManagement;

/**
 * Handles the event of navigating back to the main menu.
 * Typically invoked when the "Back to Menu" button is clicked.
 * Calls the {@code changeWindow()} method in the {@code ParentMenu} class
 * to switch to the appropriate scene or window representing the main menu.
 */
public class PersonalScoresViewController implements Initializable {

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
    
    @FXML
    private Button sortButton;
    @FXML
    private TabPane difficultyTabs;
    @FXML
    private Tab easySelector;
    @FXML
    private TableView<service.GameSession> easyTable;
    @FXML
    private TableColumn<service.GameSession, String> easyTitleColumn;
    @FXML
    private TableColumn<service.GameSession, Integer> easyScoreColumn;
    @FXML
    private TableColumn<service.GameSession, String> easyDateColumn;
    @FXML
    private Tab mediumSelector;
    @FXML
    private TableView<service.GameSession> mediumTable;
    @FXML
    private TableColumn<service.GameSession, String> mediumTitleColumn;
    @FXML
    private TableColumn<service.GameSession, Integer> mediumScoreColumn;
    @FXML
    private TableColumn<service.GameSession, String> mediumDateColumn;
    @FXML
    private Tab hardSelector;
    @FXML
    private TableView<service.GameSession> hardTable;
    @FXML
    private TableColumn<service.GameSession, String> hardTitleColumn;
    @FXML
    private TableColumn<service.GameSession, Integer> hardScoreColumn;
    @FXML
    private TableColumn<service.GameSession, String> hardDateColumn;

    private ObservableList<service.GameSession> easySessions = FXCollections.observableArrayList();
    private ObservableList<service.GameSession> mediumSessions = FXCollections.observableArrayList();
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
    private void setupTableColumns(TableColumn<service.GameSession, String> titleCol, TableColumn<service.GameSession, Integer> scoreCol, TableColumn<service.GameSession, String> dateCol) {

        titleCol.setCellValueFactory(cellData -> {
            int docId = cellData.getValue().getDocumentID();
            String title = service.DocumentsManagement.getTitleFromId(docId);
            return new SimpleStringProperty(title);        
        });
        
        scoreCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getScore()).asObject());
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTimestamp()));
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
        service.UsersManagement userManagement = new service.UsersManagement();
        List<service.GameSession> userSessions = userManagement.getSessionsByUsername(username);
        
        if(userSessions.isEmpty()){
            System.err.println("Nessuna sessione trovata.");
            return;
        }
        
        for(service.GameSession s : userSessions) {
            switch(s.getDifficulty()) {
                case "EASY":
                    easySessions.add(s);
                    break;
                case "MEDIUM":
                    mediumSessions.add(s);
                    break;
                case "HARD":
                    hardSessions.add(s);
                    break;
            }  
        }
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
    private void applySort(TableView<service.GameSession> table, TableColumn<service.GameSession, Integer> scoreCol,TableColumn<service.GameSession, String> dateCol) {
        table.getSortOrder().clear();
        if (currentSort == SortMode.BY_SCORE) {
            table.getSortOrder().add(scoreCol);
        } else {
            table.getSortOrder().add(dateCol);
        }
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
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
     * Displays the score table for the "Easy" difficulty level.
     * Populates the easyTable with game session data stored in easySessions,
     * if the table is not null.
     *
     * @param event the event that triggers this action, typically initiated
     *              by the user interacting with the interface.
     */
    @FXML
    private void showEasyScore(Event event) {
        if (easyTable != null)
            easyTable.setItems(easySessions);
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
        if (mediumTable != null)
            mediumTable.setItems(mediumSessions);
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
        if (hardTable != null)
            hardTable.setItems(hardSessions);
    }
    
}
