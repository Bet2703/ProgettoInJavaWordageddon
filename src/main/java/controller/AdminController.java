/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import service.DocumentsManagement;
import service.Levels;

import java.io.File;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 *
 * @author Gruppo6
 */
public class AdminController {

    @FXML
    private Button btnLoadDocs;
    @FXML
    private ListView<?> documentsList;
    @FXML
    private Label messageLabel;
    @FXML
    private TextField selectedFileField;
    @FXML
    private RadioButton easyRadio;
    @FXML
    private RadioButton mediumRadio;
    @FXML
    private RadioButton hardRadio;

    private ToggleGroup difficultyGroup;

    @FXML
    public void initialize() {
        // Crea il ToggleGroup e assegna i RadioButton al gruppo
        difficultyGroup = new ToggleGroup();

        easyRadio.setToggleGroup(difficultyGroup);
        mediumRadio.setToggleGroup(difficultyGroup);
        hardRadio.setToggleGroup(difficultyGroup);
    }
    
    @FXML
    private void onLoadDocuments(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona documento di testo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di testo", "*.txt"));

        File file = fileChooser.showOpenDialog(btnLoadDocs.getScene().getWindow());

        if (file!=null){
            Toggle selectedToggle = difficultyGroup.getSelectedToggle();
            if (selectedToggle == easyRadio) {
            DocumentsManagement.loadToDB(file, Levels.Difficulty.EASY);
            } else if (selectedToggle == mediumRadio) {
            DocumentsManagement.loadToDB(file, Levels.Difficulty.MEDIUM);
            } else if (selectedToggle == hardRadio) {
            DocumentsManagement.loadToDB(file, Levels.Difficulty.HARD);
            } else {
                System.out.println("Nessuna difficoltà selezionata");
            }
        }  
    }
}
