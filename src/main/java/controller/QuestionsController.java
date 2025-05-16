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
import javafx.scene.control.ToggleGroup;

/**
 *
 * @author Gruppo6
 */
public class QuestionsController {

    @FXML
    private Label questionLabel;
    @FXML
    private RadioButton optionA;
    @FXML
    private RadioButton optionB;
    @FXML
    private RadioButton optionC;
    @FXML
    private RadioButton optionD;
    @FXML
    private Button submitButton;
    @FXML
    private Button skipButton;
    @FXML
    private Label feedbackLabel;

    @FXML
    private void onSubmitAnswer(ActionEvent event) {
    }
 
    @FXML
    private void onSkipQuestion(ActionEvent event) {
    }
    
}
