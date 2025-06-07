/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

/**
 * FXML Controller class
 *
 * @author Benedetta
 */
public class PersonalScoresViewController implements Initializable {

    @FXML
    private Button backToMenuButton;
   
    private enum SortMode{
        BY_SCORE, BY_DATE
    }
    
    private SortMode currentSort = SortMode.BY_SCORE;
    
    @FXML
    private Button sortButton;
    @FXML
    private TabPane difficultyTabs;
    @FXML
    private Tab easySelector;
    @FXML
    private TableView<service.GameSessionManagement> easyTable;
    @FXML
    private TableColumn<service.GameSessionManagement, String> easyNameColumn;
    @FXML
    private TableColumn<service.GameSessionManagement, Integer> easyScoreColumn;
    @FXML
    private TableColumn<service.GameSessionManagement, LocalDate> easyDateColumn;
    @FXML
    private Tab mediumSelector;
    @FXML
    private TableView<service.GameSessionManagement> mediumTable;
    @FXML
    private TableColumn<service.GameSessionManagement, String> mediumNameColumn;
    @FXML
    private TableColumn<service.GameSessionManagement, Integer> mediumScoreColumn;
    @FXML
    private TableColumn<service.GameSessionManagement, LocalDate> mediumDateColumn;
    @FXML
    private Tab hardSelector;
    @FXML
    private TableView<service.GameSessionManagement> hardTable;
    @FXML
    private TableColumn<service.GameSessionManagement, String> hardNameColumn;
    @FXML
    private TableColumn<service.GameSessionManagement, Integer> hardScoreColumn;
    @FXML
    private TableColumn<service.GameSessionManagement, LocalDate> hardDateColumn;

    private ObservableList<service.GameSessionManagement> easySessions = FXCollections.observableArrayList();
    private ObservableList<service.GameSessionManagement> mediumSessions = FXCollections.observableArrayList();
    private ObservableList<service.GameSessionManagement> hardSessions = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns(easyNameColumn, easyScoreColumn, easyDateColumn);
        setupTableColumns(mediumNameColumn, mediumScoreColumn, mediumDateColumn);
        setupTableColumns(hardNameColumn, hardScoreColumn, hardDateColumn);

        loadDataFromDatabase();

        showEasyScore(null);
        showMediumScore(null);
        showHardScore(null);
        
        applySortToAllTables();
    }    

    private void setupTableColumns(TableColumn<service.GameSessionManagement, String> nameCol, TableColumn<service.GameSessionManagement, Integer> scoreCol, TableColumn<service.GameSessionManagement, LocalDate> dateCol) {
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
        scoreCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getScore()).asObject());
        dateCol.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getTimestamp() != null ?
                    cellData.getValue().getTimestamp().toLocalDate() : null;
            return new SimpleObjectProperty<>(date);
        });
    }
    
    private void loadDataFromDatabase() {
        
    }
    
    private void applySortToAllTables() {
        applySort(easyTable, easyScoreColumn, easyDateColumn);
        applySort(mediumTable, mediumScoreColumn, mediumDateColumn);
        applySort(hardTable, hardScoreColumn, hardDateColumn);
    }
    
    private void applySort(TableView<service.GameSessionManagement> table, TableColumn<service.GameSessionManagement, Integer> scoreCol,TableColumn<service.GameSessionManagement, LocalDate> dateCol) {
        table.getSortOrder().clear();
        if (currentSort == SortMode.BY_SCORE) {
            table.getSortOrder().add(scoreCol);
        } else {
            table.getSortOrder().add(dateCol);
        }
        table.sort();
    }
    
    @FXML
    private void handleChangeSort(ActionEvent event) {
        currentSort = (currentSort == SortMode.BY_SCORE) ? SortMode.BY_DATE : SortMode.BY_SCORE;
        applySortToAllTables();
    }
    
    @FXML
    private void handleBackToMenu(ActionEvent event) {
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

    @FXML
    private void showEasyScore(Event event) {
        if (easyTable != null)
            easyTable.setItems(easySessions);
    }

    @FXML
    private void showMediumScore(Event event) {
        if (mediumTable != null)
            mediumTable.setItems(mediumSessions);
    }

    @FXML
    private void showHardScore(Event event) {
        if (hardTable != null)
            hardTable.setItems(hardSessions);
    }
    
}
