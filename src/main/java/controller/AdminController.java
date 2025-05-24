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
    private void onLoadDocuments(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona documento di testo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di testo", "*.txt"));

        File file = fileChooser.showOpenDialog(btnLoadDocs.getScene().getWindow());

        if (file!=null){

            DocumentsManagement.loadToDB(file, Levels.Difficulty.EASY);

        }
    }
}
