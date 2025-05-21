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
        //chiama loadToDB di DocumentsManagement
    }

    
}
