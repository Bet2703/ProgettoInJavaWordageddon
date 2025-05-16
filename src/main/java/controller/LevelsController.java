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
import javafx.scene.control.RadioButton;

/**
 *
 * @author Gruppo6
 */
public class LevelsController {

    @FXML
    private RadioButton rbEasy;
    @FXML
    private RadioButton rbMedium;
    @FXML
    private RadioButton rbHard;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnBack;
    @FXML
    private Label messageLabel;

    @FXML
    private void onStartGame(ActionEvent event) {
    }

    @FXML
    private void onBack(ActionEvent event) {
    }
    
}
